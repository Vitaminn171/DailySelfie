package com.example.dailyselfie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

/*
Tên: Lý Quốc An
MSSV: 3119410002
 */


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    private static final String PROVIDER = "com.example.dailyselfie";
    private static final long INTERVAL_TEN_SEC = 10 * 1000;
    private static final String ID = "1";
    private static final String CHANNEL_ID = "123";
    private ArrayList<Selfie> selfiesList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Button buttonYes,buttonNo;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent == null) {
                            Log.d(TAG, "onActivityResult data = null");
                        } else {
                            Uri uri;
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                                uri = Uri.fromFile(new File(absolutePathCurrentFile));
                            } else {
                                uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(absolutePathCurrentFile));
                            }
                            Log.d(TAG, "Uri is " + uri);

                        }
                    }else {
                        Log.d(TAG, "Canceled");
                    }
                }
            });
    private String absolutePathCurrentFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selfiesList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(this, selfiesList);
        recyclerView.setAdapter(recyclerViewAdapter);
        scheduleRepeatingRTCNotification(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.camera) {
            getImageFromCamera();
        }
        if (id == R.id.delete) {
            onButtonShowPopupWindowClick(findViewById(R.id.recview));
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<File> getStorageDirlistFiles() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        ArrayList<File> storageDirlistFiles = new ArrayList<>();
        if (storageDir != null) {
            File[] listFiles = storageDir.listFiles((file, name) -> name.endsWith(".jpg"));
            if (listFiles != null) {
                storageDirlistFiles.addAll(Arrays.asList(listFiles));
            }
        }
        return storageDirlistFiles;
    }

    public static void scheduleRepeatingRTCNotification(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + INTERVAL_TEN_SEC);
        calendar.add(Calendar.SECOND, 5);
        Intent intent = new Intent(context, Receiver.class);
        PendingIntent alarmIntentRTC = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManagerRTC = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManagerRTC != null) {
            alarmManagerRTC.setRepeating(AlarmManager.RTC,
                    calendar.getTimeInMillis(), INTERVAL_TEN_SEC, alarmIntentRTC);
        }
    }


    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        imageUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", getCurrentFile());
        if (imageUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activityResultLauncher.launch(intent);
        } else {
            Log.d(TAG, "Uri = null");
        }
    }


    public File getCurrentFile() {
        String imageFileName = "Img_";
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    getStorageDir()      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageFile != null) {
            absolutePathCurrentFile = imageFile.getAbsolutePath();
        } else {
            Log.d(TAG, "imageFile = null");
        }
        return imageFile;
    }

    private File getStorageDir() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            if (!storageDir.exists()) {
                storageDir.mkdir();
            } else {
                Log.d(TAG, "Directory is exists");
            }
        }
        return storageDir;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        selfiesList.clear();
        for (File file : getStorageDirlistFiles()) {
            selfiesList.add(new Selfie(file.getAbsolutePath()));
        }
        recyclerViewAdapter.notifyDataSetChanged();

    }
    public void onButtonShowPopupWindowClick(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window,null);


        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        buttonYes = popupView.findViewById(R.id.buttonYes);
        buttonNo = popupView.findViewById(R.id.buttonNo);
        TextView textView = popupView.findViewById(R.id.textDel);
        String text = "Delete all image ?";
        textView.setText(text);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getStorageDirlistFiles().isEmpty()) {
                    for (File file : getStorageDirlistFiles()) {
                        file.delete();
                    }
                }
                selfiesList.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

    }
}
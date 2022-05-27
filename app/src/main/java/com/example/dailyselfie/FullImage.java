package com.example.dailyselfie;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;

/*
Tên: Lý Quốc An
MSSV: 3119410002
 */


public class FullImage extends AppCompatActivity implements Serializable {
    private ImageView imageViewFull;
    private File file;
    private static final String PROVIDER = "com.example.dailyselfie";
    private Button buttonYes,buttonNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageViewFull = findViewById(R.id.imageFull);

        setDataFromIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_full_img, menu);
        return true;
    }

    private void setDataFromIntent() {
        String selfiePath = (String) getIntent().getSerializableExtra("selfiePath");
        if (selfiePath != null) {
            file = new File(selfiePath);
        }
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", file);
        }
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(imageViewFull);
        pAttacher.update();
        imageViewFull.setImageURI(uri);

    }
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.delete) {
            onButtonShowPopupWindowClick(findViewById(R.id.view));
        }
        else{
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        String text = "Delete this image ?";
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
                file.delete();
                popupWindow.dismiss();
                finish();

            }
        });

    }
}

package com.example.dailyselfie;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/*
Tên: Lý Quốc An
MSSV: 3119410002
 */


public class FullImage extends AppCompatActivity implements Serializable {
    private ImageView imageViewFull;
    private File file;
    private static final String PROVIDER = "com.example.dailyselfie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        imageViewFull = findViewById(R.id.imageFull);
        setDataFromIntent();
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
            //FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", getCurrentFile());
        }
        imageViewFull.setImageURI(uri);
    }
}

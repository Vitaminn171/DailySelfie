package com.example.dailyselfie;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Selfie implements Serializable {

    private final transient String pathSelfieFile;

    public Selfie(String pathSelfieFile) {
        this.pathSelfieFile = pathSelfieFile;
    }

    public String getPathSelfieFile() {
        return pathSelfieFile;
    }

    @NonNull
    @Override
    public String toString() {
        return "Selfie{" +
                "pathSelfieFile='" + pathSelfieFile + '\'' +
                '}';
    }
}
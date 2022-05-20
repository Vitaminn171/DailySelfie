package com.example.dailyselfie;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/*
Tên: Lý Quốc An
MSSV: 3119410002
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Serializable {
    private final LayoutInflater layoutInflater;
    private final ArrayList<Selfie> selfies;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Selfie> selfies) {
        this.layoutInflater = LayoutInflater.from(context);
        this.selfies = selfies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = layoutInflater.inflate(R.layout.item_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.selfie = selfies.get(position);
        File file = new File(holder.selfie.getPathSelfieFile());
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        holder.textView.setText(file.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFullImage(holder.selfie.getPathSelfieFile());
            }
        });
    }

    private void openFullImage(String selfiePath) {
        Intent intentFullImage = new Intent(context, FullImage.class);
        intentFullImage.putExtra("selfiePath", selfiePath);
        String TAG = "myLogs";
        Log.d(TAG, "Path intent = " + selfiePath);
        context.startActivity(intentFullImage);
    }

    @Override
    public int getItemCount() {
        return selfies.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements Serializable {
        public final View mView;
        TextView textView;
        ImageView imageView;
        Selfie selfie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

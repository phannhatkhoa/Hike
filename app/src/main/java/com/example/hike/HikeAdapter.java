package com.example.hike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hike.activities.EditActivity;
import com.example.hike.models.Hike;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ContactViewHolder> {
    private List<Hike> hikes;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Hike hike);
    }

    public HikeAdapter(List<Hike> hikes, OnDeleteClickListener onDeleteClickListener) {
        this.hikes = hikes;
        this.onDeleteClickListener = onDeleteClickListener;
    }
    public HikeAdapter(List<Hike> hikes) {
        this.hikes = hikes;
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(com.example.hike.R.layout.item_hike, parent, false);
        return new ContactViewHolder(itemView);
        
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Hike hike = hikes.get(position);
        holder.hikeName.setText(hike.name);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditActivity.class);
            intent.putExtra("hike_position", position);

            v.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(hikes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView hikeName;
        Button deleteButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            hikeName = itemView.findViewById(R.id.hikeName);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
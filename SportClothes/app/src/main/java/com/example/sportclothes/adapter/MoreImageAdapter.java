package com.example.sportclothes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.databinding.ItemMoreImageBinding;
import com.example.sportclothes.model.Image;
import com.example.sportclothes.util.GlideUtils;

import java.util.List;

public class MoreImageAdapter extends RecyclerView.Adapter<MoreImageAdapter.MoreImageViewHolder> {

    private final List<Image> imageList;

    public MoreImageAdapter(List<Image> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public MoreImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoreImageBinding itemMoreImageBinding = ItemMoreImageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MoreImageViewHolder(itemMoreImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreImageViewHolder holder, int position) {
        Image image = imageList.get(position);
        if (image == null) {
            return;
        }
        GlideUtils.loadUrl(image.getUrl(), holder.itemMoreImageBinding.imageSportCloth);
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    public static class MoreImageViewHolder extends RecyclerView.ViewHolder{
        private final ItemMoreImageBinding itemMoreImageBinding;
        public MoreImageViewHolder(ItemMoreImageBinding itemMoreImageBinding) {
            super(itemMoreImageBinding.getRoot());
            this.itemMoreImageBinding = itemMoreImageBinding;
        }
    }
}

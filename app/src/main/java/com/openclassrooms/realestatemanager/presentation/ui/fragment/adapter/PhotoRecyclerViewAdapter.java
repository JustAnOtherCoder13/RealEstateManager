package com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.RecyclerviewPropertyDetailItemBinding;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private List<PropertyPhoto> mPhotos;

    public PhotoRecyclerViewAdapter(List<PropertyPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPropertyDetailItemBinding binding = RecyclerviewPropertyDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyPhoto photo = mPhotos.get(position);
        if (photo.getPhoto().equals(ADD_PHOTO)){
            holder.binding.propertyDetailItemPhoto.setImageResource(R.drawable.img_add_photo);
            holder.binding.propertyDetailItemPhotoDescription.setVisibility(View.GONE);
        }else{
            setPropertyPhoto(holder, photo);
            holder.binding.propertyDetailItemPhotoDescription.setVisibility(View.VISIBLE);
            holder.binding.propertyDetailItemPhotoDescription.setText(photo.getDescription());}
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewPropertyDetailItemBinding binding;

        public ViewHolder(@NonNull RecyclerviewPropertyDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updatePhotos(List<PropertyPhoto> updatedPhotos){
        mPhotos = updatedPhotos;
    }

    private void setPropertyPhoto(@NonNull ViewHolder holder, @NonNull PropertyPhoto photo) {
        Glide.with(holder.binding.propertyDetailItemPhoto.getContext())
                .load(photo.getPhoto())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.propertyDetailItemPhoto.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}

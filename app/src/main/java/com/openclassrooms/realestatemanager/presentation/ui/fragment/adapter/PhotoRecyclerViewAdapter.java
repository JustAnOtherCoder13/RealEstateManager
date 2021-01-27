package com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.RecyclerviewPropertyDetailItemBinding;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.ManageImageHelper.playLoader;
import static com.openclassrooms.realestatemanager.presentation.utils.PathUtil.isImageFileFromPath;
import static com.openclassrooms.realestatemanager.presentation.utils.PathUtil.isVideoFileFromPath;
import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private List<PropertyPhoto> mPhotos;
    private boolean isPhotoHaveBeenDeleted;

    public PhotoRecyclerViewAdapter(List<PropertyPhoto> mPhotos) {
        this.mPhotos = mPhotos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPropertyDetailItemBinding binding = RecyclerviewPropertyDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.propertyDetailItemLoader.animationView.setAnimation(R.raw.loader);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyPhoto photo = mPhotos.get(position);
        holder.binding.propertyDetailItemPlayLogo.setVisibility(isImageFileFromPath(photo.getPhotoPath()) ?
                View.GONE
                : View.VISIBLE);
        if (isPhotoHaveBeenDeleted) {
            holder.binding.propertyDetailItemCheckBox.setChecked(false);
            holder.binding.propertyDetailItemCheckBox.setVisibility(View.GONE);
        }
        if (photo.getPhotoPath().equals(ADD_PHOTO)) {
            playLoader(false,holder.binding.propertyDetailItemLoader.animationView);
            holder.binding.propertyDetailItemPlayLogo.setVisibility(View.GONE);
            holder.binding.propertyDetailItemPhoto.setImageResource(R.drawable.img_add_photo);
            holder.binding.propertyDetailItemPhotoDescription.setVisibility(View.GONE);
        } else {
            setPropertyPhoto(holder, photo);
            holder.binding.propertyDetailItemPhotoDescription.setVisibility(View.VISIBLE);
            holder.binding.propertyDetailItemPhotoDescription.setText(photo.getDescription());
        }
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

    public void updatePhotos(List<PropertyPhoto> updatedPhotos) {
        this.mPhotos = updatedPhotos;
        notifyDataSetChanged();
    }

    public void isPhotoHaveBeenDeleted(boolean isPhotoHaveBeenDeleted) {
        this.isPhotoHaveBeenDeleted = isPhotoHaveBeenDeleted;
    }

    //---------------------- HELPER -----------------------------------

    private void setPropertyPhoto(@NonNull ViewHolder holder, @NonNull PropertyPhoto photo) {
        Glide.with(holder.binding.propertyDetailItemPhoto.getContext())
                .load(photo.getPhotoPath())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        playLoader(true,holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.propertyDetailItemPhoto.setImageDrawable(resource);
                        playLoader(false,holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


}

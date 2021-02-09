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
import com.picone.core.domain.entity.PropertyMedia;

import java.util.List;

import static com.openclassrooms.realestatemanager.presentation.utils.ManageImageHelper.playLoader;
import static com.openclassrooms.realestatemanager.presentation.utils.PathUtil.isImageFileFromPath;
import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private List<PropertyMedia> mMedias;
    private boolean mIsMediaHaveBeenDeleted;

    public PhotoRecyclerViewAdapter(List<PropertyMedia> mMedias) {
        this.mMedias = mMedias;
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
        final PropertyMedia media = mMedias.get(position);
        //show play logo if is video
        holder.binding.propertyDetailItemPlayLogo.setVisibility(isImageFileFromPath(media.getMediaPath()) ?
                View.GONE
                : View.VISIBLE);
        //if media have been deleted reset media checkbox
        if (mIsMediaHaveBeenDeleted) {
            holder.binding.propertyDetailItemCheckBox.setChecked(false);
            holder.binding.propertyDetailItemCheckBox.setVisibility(View.GONE);
        }
        //if ADD_PHOTO init add view
        if (media.getMediaPath().equals(ADD_PHOTO)) {
            playLoader(false,holder.binding.propertyDetailItemLoader.animationView);
            holder.binding.propertyDetailItemPlayLogo.setVisibility(View.GONE);
            holder.binding.propertyDetailItemMedia.setImageResource(R.drawable.img_add_photo);
            holder.binding.propertyDetailItemMediaDescription.setVisibility(View.GONE);
        } else {//else init view with media
            setPropertyMedia(holder, media);
            holder.binding.propertyDetailItemMediaDescription.setVisibility(View.VISIBLE);
            holder.binding.propertyDetailItemMediaDescription.setText(media.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return mMedias.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerviewPropertyDetailItemBinding binding;
        public ViewHolder(@NonNull RecyclerviewPropertyDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateMedias(List<PropertyMedia> updatedPhotos) {
        this.mMedias = updatedPhotos;
        notifyDataSetChanged();
    }

    public void isMediaHaveBeenDeleted(boolean isMediaHaveBeenDeleted) {
        this.mIsMediaHaveBeenDeleted = isMediaHaveBeenDeleted;
    }

    //---------------------- HELPER -----------------------------------

    private void setPropertyMedia(@NonNull ViewHolder holder, @NonNull PropertyMedia media) {
        Glide.with(holder.binding.propertyDetailItemMedia.getContext())
                .load(media.getMediaPath())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        playLoader(true,holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.propertyDetailItemMedia.setImageDrawable(resource);
                        playLoader(false,holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}

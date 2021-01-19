package com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.RecyclerviewPropertyListItemBinding;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;

public class PropertyRecyclerViewAdapter extends RecyclerView.Adapter<PropertyRecyclerViewAdapter.ViewHolder> {

    private List<Property> mProperties;
    private List<PropertyPhoto> mPhotos;
    private List<View> mItems = new ArrayList<>();
    private Property selectedProperty;
    private Context context;


    public PropertyRecyclerViewAdapter(List<Property> mProperties, Context context) {
        this.mProperties = mProperties;
        this.context = context;
        this.mPhotos = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPropertyListItemBinding binding = RecyclerviewPropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mItems.add(holder.itemView);
        final Property property = mProperties.get(position);
        if (!mPhotos.isEmpty()) {
            if (position<mPhotos.size()){
                PropertyPhoto photo = mPhotos.get(position);
                setPropertyPhoto(holder, photo);
            }
        }
        holder.binding.propertyItemPrice.setText(String.valueOf(property.getPrice()));
        holder.binding.propertyItemTown.setText(property.getRegion());
        holder.binding.propertyItemType.setText(property.getPropertyType());
        TextView textView = holder.itemView.findViewById(R.id.property_item_price);

        if (selectedProperty != null)
            if (this.selectedProperty.getId() == property.getId()) {
                //reset all views
                for (View item : mItems) {
                    item.setBackgroundColor(Color.TRANSPARENT);
                    textView.setTextColor(context.getResources().getColor(R.color.custom_pink));
                }
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.custom_pink));
                textView.setTextColor(context.getResources().getColor(R.color.white));
            } else if (selectedProperty.getAddress() == null) {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(context.getResources().getColor(R.color.custom_pink));
            }
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerviewPropertyListItemBinding binding;

        public ViewHolder(@NonNull RecyclerviewPropertyListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateProperties(List<Property> updatedProperties) {
        this.mProperties = updatedProperties;
        notifyDataSetChanged();
    }

    public void updatePhotos(List<PropertyPhoto> propertyPhotos) {
        this.mPhotos = propertyPhotos;
        notifyDataSetChanged();
    }

    public void updateSelectedProperty(Property property) {
        this.selectedProperty = property;
        notifyDataSetChanged();
    }

    private void setPropertyPhoto(@NonNull PropertyRecyclerViewAdapter.ViewHolder holder, @NonNull PropertyPhoto photo) {
        Glide.with(holder.binding.propertyItemPhoto.getContext())
                .load(photo.getPhotoPath())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.propertyItemPhoto.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


}

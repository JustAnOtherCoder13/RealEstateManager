package com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.databinding.RecyclerviewPropertyListItemBinding;
import com.picone.core.domain.entity.Property;

import java.util.List;

public class PropertyRecyclerViewAdapter extends RecyclerView.Adapter<PropertyRecyclerViewAdapter.ViewHolder> {

    private List<Property> mProperties;

    public PropertyRecyclerViewAdapter(List<Property> mProperties) {
        this.mProperties = mProperties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPropertyListItemBinding binding = RecyclerviewPropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext())
        ,parent,false);
        return new PropertyRecyclerViewAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Property property = mProperties.get(position);
        holder.binding.propertyItemPrice.setText(String.valueOf(property.getPrice()));
        holder.binding.propertyItemTown.setText(property.getAddress());
        holder.binding.propertyItemType.setText(property.getPropertyType());
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

    public void updateProperties(List<Property> updatedProperties){
        mProperties = updatedProperties;
    }
}

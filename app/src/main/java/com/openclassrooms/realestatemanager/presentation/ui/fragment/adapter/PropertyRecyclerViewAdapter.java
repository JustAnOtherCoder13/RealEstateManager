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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static com.openclassrooms.realestatemanager.presentation.utils.Utils.convertDollarToEuro;

public class PropertyRecyclerViewAdapter extends RecyclerView.Adapter<PropertyRecyclerViewAdapter.ViewHolder> {

    private List<Property> mProperties;
    private List<PropertyPhoto> mPhotos;
    private List<View> mItems = new ArrayList<>();
    private Property selectedProperty;
    private Currency currency;
    private Context context;
    private Locale locale;


    public void updateLocale(Locale locale){
        this.locale = locale;
        notifyDataSetChanged();
    }
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
        if (locale==null)locale=Locale.US;
        currency = Currency.getInstance(locale);
        mItems.add(holder.itemView);
        final Property property = mProperties.get(position);
        if (!mPhotos.isEmpty()) {
            if (position<mPhotos.size()){
                PropertyPhoto photo = mPhotos.get(position);
                setPropertyPhoto(holder, photo);
            }
        }
        holder.binding.propertyItemPrice.setText(convertedPrice(property));
        holder.binding.propertyItemTown.setText(property.getRegion());
        holder.binding.propertyItemType.setText(property.getPropertyType());
        TextView textView = holder.itemView.findViewById(R.id.property_item_price);

        if (selectedProperty != null)
            if (this.selectedProperty.getId() == property.getId()) {
                //reset all views
                for (View item : mItems) {
                    TextView itemYTextView = item.findViewById(R.id.property_item_price);
                    itemYTextView.setTextColor(context.getResources().getColor(R.color.custom_pink));
                    item.setBackgroundColor(Color.TRANSPARENT);
                }
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.custom_pink));
                textView.setTextColor(context.getResources().getColor(R.color.white));
            } else if (selectedProperty.getAddress() == null) {
                textView.setTextColor(context.getResources().getColor(R.color.custom_pink));
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
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

    public String convertedPrice(Property property){
        int price;
        if (locale==Locale.US){
            price = property.getPrice();
        }
        else price = convertDollarToEuro(property.getPrice());

        return formatWithSpace().format(price).concat(" ").concat(currency.getSymbol(locale));
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

    @NonNull
    private DecimalFormat formatWithSpace(){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(symbols);
        df.setGroupingSize(3);
        return df;
    }


}

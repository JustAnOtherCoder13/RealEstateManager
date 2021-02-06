package com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
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
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyMedia;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import static com.openclassrooms.realestatemanager.presentation.utils.ManageImageHelper.playLoader;
import static com.openclassrooms.realestatemanager.presentation.utils.Utils.convertDollarToEuro;
import static com.openclassrooms.realestatemanager.presentation.utils.Utils.formatWithSpace;

public class PropertyRecyclerViewAdapter extends RecyclerView.Adapter<PropertyRecyclerViewAdapter.ViewHolder> {

    private List<Property> mProperties;
    private List<View> mItems = new ArrayList<>();
    private Property mSelectedProperty;
    private Currency mCurrency;
    private Context mContext;
    private Locale mLocale;

    public PropertyRecyclerViewAdapter(List<Property> mProperties, Context mContext) {
        this.mProperties = mProperties;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewPropertyListItemBinding binding = RecyclerviewPropertyListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.propertyDetailItemLoader.animationView.setAnimation(R.raw.loader);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        initCurrency();
        //load all holder to highlight the selected one
        mItems.add(holder.itemView);
        final Property property = mProperties.get(position);
        if (!property.medias.isEmpty())
            setPropertyMedia(holder, property.medias.get(0));
        holder.binding.propertyItemPrice.setText(convertedPrice(property.propertyInformation));
        holder.binding.propertyItemTown.setText(property.propertyLocation.getRegion());
        holder.binding.propertyItemType.setText(property.propertyInformation.getPropertyType());
        highlightSelectedProperty(holder, property);
        holder.binding.propertyItemSold.setVisibility(property.propertyInformation.getSoldFrom().trim().isEmpty() ?
                View.GONE
                : View.VISIBLE);
    }

    private void highlightSelectedProperty(@NonNull ViewHolder holder, Property property) {
        TextView textView = holder.binding.propertyItemPrice;
        if (mSelectedProperty.propertyInformation != null) {
            //if selected property is the property we are passing
            if (this.mSelectedProperty.propertyInformation.getId() == property.propertyInformation.getId()) {
                //reset all views and highlight selected property
                for (View item : mItems) {
                    TextView itemYTextView = item.findViewById(R.id.property_item_price);
                    itemYTextView.setTextColor(mContext.getResources().getColor(R.color.custom_pink));
                    item.setBackgroundColor(Color.TRANSPARENT);
                }
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.custom_pink));
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        } else {//else set base values
            textView.setTextColor(mContext.getResources().getColor(R.color.custom_pink));
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void initCurrency() {
        if (mLocale == null) mLocale = Locale.US;
        mCurrency = Currency.getInstance(mLocale);
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
    public void updateLocale(Locale locale) {
        this.mLocale = locale;
        notifyDataSetChanged();
    }

    public void updateSelectedProperty(Property selectedProperty) {
        this.mSelectedProperty = selectedProperty;
        notifyDataSetChanged();
    }


    //---------------------------HELPERS---------------------------

    private void setPropertyMedia(@NonNull PropertyRecyclerViewAdapter.ViewHolder holder, @NonNull PropertyMedia photo) {
        Glide.with(holder.binding.propertyItemPhoto.getContext())
                .load(photo.getPhotoPath())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        playLoader(true, holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.binding.propertyItemPhoto.setImageDrawable(resource);
                        playLoader(false, holder.binding.propertyDetailItemLoader.animationView);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public String convertedPrice(PropertyInformation propertyInformation) {
        int price;
        if (mLocale == Locale.US) {
            price = propertyInformation.getPrice();
        } else price = convertDollarToEuro(propertyInformation.getPrice());

        return formatWithSpace().format(price).concat(" ").concat(mCurrency.getSymbol(mLocale));
    }
}

package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.openclassrooms.realestatemanager.presentation.utils.customView.AddPropertyInformationCustomView;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyPhoto;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;
import static com.picone.core.utils.ConstantParameters.PROPERTY_TO_ADD;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> mPropertyPhotos = new ArrayList<>();
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean mIsPropertyValuesAreNull;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(false);
        initRecyclerView();
        setUpdateButtonIcon(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPropertyValuesAreNull = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).getAddress() == null;

        initView();
        initDropDownMenu();
        initClickListener();
        configureOnClickRecyclerView();

        mPropertyViewModel.isUpdateComplete.observe(getViewLifecycleOwner(), isUpdateComplete -> {
            if (!isUpdateComplete) return;
            mPropertyViewModel.setSelectedProperty(new Property());
            mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
        });
    }

    //___________________________________VIEW_____________________________________________

    private void initRecyclerView() {
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, ADD_PHOTO, "", 0);
        mPropertyPhotos.add(propertyPhoto);
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(mPropertyPhotos);
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        if (!mIsPropertyValuesAreNull)
            initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));

        EditText editText = mBinding.addPropertyInformationLayout.addPropertyInformationAddress.findViewById(R.id.add_property_information_custom_view_value);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setSingleLine(false);
        editText.setLines(6);
    }

    private void initViewValueWhenUpdate(@NonNull FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, @NonNull Property property) {
        mPropertyViewModel.setPhotosToDelete(new ArrayList<>());
        EditText descriptionEditText = mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText;
        AutoCompleteTextView propertyTypeDropDownMenu = mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView;
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationPrice, String.valueOf(property.getPrice()));
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationArea, String.valueOf(property.getPropertyArea()));
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms, String.valueOf(property.getNumberOfBathrooms()));
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms, String.valueOf(property.getNumberOfBedrooms()));
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationNumberOfRooms, String.valueOf(property.getNumberOfRooms()));
        setTextForCustomView(addPropertyInformationCustomView.addPropertyInformationAddress, property.getAddress());
        mPropertyPhotos.addAll(Objects.requireNonNull(mPropertyViewModel.getAllPropertyPhotosForProperty.getValue()));
        descriptionEditText.setText(property.getDescription());
        propertyTypeDropDownMenu.setText(property.getPropertyType());
    }

    private void initDropDownMenu() {
        String[] propertyType = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, propertyType);
        AutoCompleteTextView propertyTypeTextView = (AutoCompleteTextView) mBinding.addPropertyInformationLayout.addPropertyInformationType.getEditText();
        assert propertyTypeTextView != null;
        propertyTypeTextView.setAdapter(adapter);
    }

    //___________________________________CLICK LISTENER_____________________________________________

    private void initClickListener() {
        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v ->
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show());

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v ->
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(
                        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ?
                                View.VISIBLE : View.GONE));

        mUpdateButton.setOnClickListener(mIsPropertyValuesAreNull ?
                (v -> mPropertyViewModel.addProperty(updatedProperty(PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue())))))
                : (v -> mPropertyViewModel.updateProperty(updatedProperty(Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue())))));

    }

    public void configureOnClickRecyclerView() {
        initAddMediaClickListener();
        initSelectPhotoToDeleteOnLongClickListener();
    }

    private void initSelectPhotoToDeleteOnLongClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    if (position == 0) return false;
                    CheckBox selectToDeleteCheckBox = v.findViewById(R.id.property_detail_item_check_box);
                    selectToDeleteCheckBox.setChecked(!selectToDeleteCheckBox.isChecked());
                    selectToDeleteCheckBox.setVisibility(selectToDeleteCheckBox.isChecked() ? View.VISIBLE : View.GONE);
                    setPhotoToDeleteList(position, selectToDeleteCheckBox);
                    return true;
                });
    }

    private void initAddMediaClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (position == 0)
                        Toast.makeText(requireContext(), "add", Toast.LENGTH_SHORT).show();
                });
    }

    //___________________________________HELPERS_____________________________________________

    private void setTextForCustomView(@NonNull AddPropertyInformationCustomView customView, String text) {
        EditText editText = customView.findViewById(R.id.add_property_information_custom_view_value);
        editText.setText(text);
    }

    @NonNull
    private String getTextForCustomView(@NonNull AddPropertyInformationCustomView customView) {
        EditText editText = customView.findViewById(R.id.add_property_information_custom_view_value);
        return editText.getText().toString();
    }

    @NonNull
    @Contract("_ -> param1")
    private Property updatedProperty(@NonNull Property property) {
        property.setAddress(getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationAddress));
        property.setNumberOfRooms(Integer.parseInt(getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfRooms)));
        property.setNumberOfBathrooms(Integer.parseInt((getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBathrooms))));
        property.setNumberOfBedrooms(Integer.parseInt(getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBedrooms)));
        property.setPropertyArea(Integer.parseInt(getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationArea)));
        property.setPrice(Integer.parseInt(getTextForCustomView(mBinding.addPropertyInformationLayout.addPropertyInformationPrice)));
        property.setPropertyType(mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView.getText().toString());
        property.setDescription(mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText.getText().toString());
        property.setSold(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked());
        property.setSoldFrom(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ? mBinding.addPropertySoldLayout.addPropertySoldEditText.getText().toString() : "");
        return property;
    }

    private void setPhotoToDeleteList(int position, @NonNull CheckBox selectToDeleteCheckBox) {
        int indexForExistingMedia = position - 1;
        List<PropertyPhoto> existingMediaForProperty = mPropertyViewModel.getAllPropertyPhotosForProperty.getValue();
        assert existingMediaForProperty != null;
        if (selectToDeleteCheckBox.isChecked() && !mPhotosToDelete.contains(existingMediaForProperty.get(indexForExistingMedia)))
            mPhotosToDelete.add(existingMediaForProperty.get(indexForExistingMedia));
        else if (!mPhotosToDelete.isEmpty())
            mPhotosToDelete.remove(existingMediaForProperty.get(indexForExistingMedia));
        mPropertyViewModel.setPhotosToDelete(mPhotosToDelete);
    }
}
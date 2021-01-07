package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.UPDATE_PROPERTY_COMPLETE;
import static com.picone.core.utils.ConstantParameters.ADD_PHOTO;
import static com.picone.core.utils.ConstantParameters.CAMERA_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.PROPERTY_TO_ADD;
import static com.picone.core.utils.ConstantParameters.WRITE_PERMISSION_CODE;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> mPropertyPhotos = new ArrayList<>();
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean isNewPropertyToPersist;
    private String mPreviousSavedPropertyAddress;

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
        isNewPropertyToPersist = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).getAddress() == null;
        mPreviousSavedPropertyAddress = Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).get(mPropertyViewModel.getAllProperties.getValue().size() - 1).getAddress();
        mPropertyViewModel.isDataLoading.observe(getViewLifecycleOwner(), this::playLoader);
        setUpdateButton();
        initClickListener();
        initView();
        initViewModel();
    }


    //___________________________________VIEW_____________________________________________

    private void initViewModel() {
        if (!isNewPropertyToPersist)
            mPropertyViewModel.setPropertyLocationForProperty(Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                case ADD_PROPERTY_COMPLETE:
                    setValuesForNewProperty();
                    break;
                case ADD_POINT_OF_INTEREST_COMPLETE:
                    if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.addPropertyFragment)
                        mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
                    break;
            }
        });
    }

    private void initRecyclerView() {
        PropertyPhoto propertyPhoto = new PropertyPhoto(0, ADD_PHOTO, "", 0);
        mPropertyPhotos.add(propertyPhoto);
        PhotoRecyclerViewAdapter adapter = new PhotoRecyclerViewAdapter(mPropertyPhotos);
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        initPropertyTypeDropDownMenu();
        mPropertyViewModel.resetCompletionState();

        if (!isNewPropertyToPersist)
            initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        else mBinding.addPropertySoldLayout.getRoot().setVisibility(View.GONE);

        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));
    }

    //TODO all custom view filled with number of bedroom value when update, why?
    private void initViewValueWhenUpdate(@NonNull FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, @NonNull Property property) {
        Log.i("TAG", "initViewValueWhenUpdate: " + addPropertyInformationCustomView);
        mPropertyViewModel.setPhotosToDelete(new ArrayList<>());
        EditText descriptionEditText = mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText;
        AutoCompleteTextView propertyTypeDropDownMenu = mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView;
        addPropertyInformationCustomView.addPropertyInformationPrice.setValueText(String.valueOf(property.getPrice()));
        Log.e("TAG", "initViewValueWhenUpdate: " + getResources().getResourceName(addPropertyInformationCustomView.addPropertyInformationPrice.getId()) + property.getPrice());
        addPropertyInformationCustomView.addPropertyInformationArea.setValueText(String.valueOf(property.getPropertyArea()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms.setValueText(String.valueOf(property.getNumberOfBathrooms()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms.setValueText(String.valueOf(property.getNumberOfBedrooms()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfRooms.setValueText(String.valueOf(property.getNumberOfRooms()));
        addPropertyInformationCustomView.addPropertyInformationAddress.setValueText(property.getAddress());
        mPropertyPhotos.addAll(Objects.requireNonNull(mPropertyViewModel.getAllPropertyPhotosForProperty.getValue()));
        descriptionEditText.setText(property.getDescription());
        propertyTypeDropDownMenu.setText(property.getPropertyType());
    }

    private void initPropertyTypeDropDownMenu() {
        String[] propertyType = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, propertyType);
        AutoCompleteTextView propertyTypeTextView = (AutoCompleteTextView) mBinding.addPropertyInformationLayout.addPropertyInformationType.getEditText();
        assert propertyTypeTextView != null;
        propertyTypeTextView.setAdapter(adapter);
    }

    //___________________________________CLICK LISTENER_____________________________________________

    private void initClickListener() {
        initAddMediaClickListener();
        initSelectPhotoToDeleteOnLongClickListener();

        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v ->
                Toast.makeText(requireContext(), "delete", Toast.LENGTH_SHORT).show());

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v ->
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(
                        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ?
                                View.VISIBLE : View.GONE));

        mUpdateButton.setOnClickListener(v -> addProperty());
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
                    if (position == 0) initAlertDialog();

                });
    }

    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("add photo")
                .setMessage("get photo from folder or your phone camera ")
                .setNegativeButton("camera", (dialog, which) -> {
                    openCamera();
                })
                .setPositiveButton("folder", (dialog, which) -> {
                    Log.i("TAG", "initAlertDialog: folder");
                })
                .create()
                .show();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }


    private void openCamera() {
        if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE) && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);{
                playLoader(true);
                startActivityForResult(cameraIntent, CAMERA_INTENT_REQUEST_CODE);
            }
        } else {
            requestCameraPermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_INTENT_REQUEST_CODE) {
            playLoader(false);
            if (data!=null)
            Log.i("TAG", "onActivityResult: " + data.getExtras());
        }
    }
    //___________________________________SETTER WHEN ADD CLICK_____________________________________________

    private void addProperty() {
        if (isRequiredInformationAreFilled()) {
            hideSoftKeyboard(mBinding.addPropertyInformationLayout.getRoot());
            playLoader(true);
            if (isNewPropertyToPersist)
                mPropertyViewModel.addProperty(updateProperty(PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()))));
            else {
                Property originalProperty = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue());
                if (isOriginalPropertyAddressNotEqualEditTextAddress(originalProperty))
                    setValuesForUpdatedPropertyIfAddressIsUpdate(updateProperty(originalProperty), originalProperty);
                else {
                    mPropertyViewModel.updateProperty(updateProperty(originalProperty));
                    mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
                        if (completionState.equals(UPDATE_PROPERTY_COMPLETE))
                            mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
                    });
                }
            }
        } else
            Toast.makeText(requireContext(), R.string.information_not_filled, Toast.LENGTH_SHORT).show();
    }

    private void setValuesForNewProperty() {

        mPropertyViewModel.getAllProperties.observe(getViewLifecycleOwner(), properties -> {
            if (isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(properties.get(properties.size() - 1))) {
                mPropertyViewModel.setPropertyLocationForPropertyAddress(properties.get(properties.size() - 1));
            }
        });

        mPropertyViewModel.getLocationForAddress.observe(getViewLifecycleOwner(), propertyLocation -> {
            if (isPropertyLocationNotEmptyObject(propertyLocation) && isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(getPropertyForId(String.valueOf(propertyLocation.getPropertyId())))) {
                Property property = getPropertyForId(String.valueOf(propertyLocation.getPropertyId()));
                property.setRegion(propertyLocation.getRegion());
                mPropertyViewModel.updateProperty(property);
                mPropertyViewModel.addPropertyLocationForProperty(propertyLocation);
                mPropertyViewModel.setNearBySearchForPropertyLocation(propertyLocation);
            }
        });

        mPropertyViewModel.getMapsPointOfInterest.observe(getViewLifecycleOwner(), pointOfInterests -> {
            if (!pointOfInterests.isEmpty() && isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(getPropertyForId(String.valueOf(pointOfInterests.get(pointOfInterests.size() - 1).getPropertyId()))))
                mPropertyViewModel.addPropertyPointOfInterest(pointOfInterests);
        });
    }

    private void setValuesForUpdatedPropertyIfAddressIsUpdate(Property updatedProperty, Property originalProperty) {

        mPropertyViewModel.setAllPointOfInterestForProperty(originalProperty);
        mPropertyViewModel.setPropertyLocationForProperty(originalProperty);

        mPropertyViewModel.getPropertyLocationForProperty.observe(getViewLifecycleOwner(), propertyLocation ->
                mPropertyViewModel.setPropertyLocationForPropertyAddress(updatedProperty));

        mPropertyViewModel.getLocationForAddress.observe(getViewLifecycleOwner(), propertyLocation -> {
            if (isNewPropertyLocationLatitudeNotEqualPropertyLocationLatitudeToReplace(propertyLocation)) {
                updatedProperty.setRegion(propertyLocation.getRegion());
                mPropertyViewModel.updateProperty(updatedProperty);
                mPropertyViewModel.updatePropertyLocation(propertyLocation);
                mPropertyViewModel.setNearBySearchForPropertyLocation(propertyLocation);
            }
        });

        mPropertyViewModel.getMapsPointOfInterest.observe(getViewLifecycleOwner(), pointOfInterests -> {
            if (isPointOfInterestPropertyIdEqualUpdatedPropertyId(updatedProperty, pointOfInterests))
                mPropertyViewModel.updatePointOfInterest(pointOfInterests);
        });
    }

    //___________________________________HELPERS_____________________________________________

    @NonNull
    private Property updateProperty(@NonNull Property originalProperty) {
        Property property = new Property();
        property.setId(originalProperty.getId());
        property.setRealEstateAgentId(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()).getId());
        property.setAddress(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
        property.setNumberOfRooms(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfRooms.getValueForView()));
        property.setNumberOfBathrooms(Integer.parseInt((mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBathrooms.getValueForView())));
        property.setNumberOfBedrooms(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBedrooms.getValueForView()));
        property.setPropertyArea(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationArea.getValueForView()));
        property.setPrice(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationPrice.getValueForView()));
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

    //___________________________________BOOLEAN_____________________________________________


    private boolean isOriginalPropertyAddressNotEqualEditTextAddress(@NonNull Property originalProperty) {
        return originalProperty.getAddress() != null && !originalProperty.getAddress().equalsIgnoreCase(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
    }

    private boolean isNewPropertyLocationLatitudeNotEqualPropertyLocationLatitudeToReplace(@NonNull PropertyLocation propertyLocation) {
        return Objects.requireNonNull(mPropertyViewModel.getPropertyLocationForProperty.getValue()).getLatitude() != propertyLocation.getLatitude();
    }

    private boolean isPointOfInterestPropertyIdEqualUpdatedPropertyId(Property updatedProperty, @NonNull List<PointOfInterest> pointOfInterests) {
        return !pointOfInterests.isEmpty() && pointOfInterests.get(pointOfInterests.size() - 1).getPropertyId() == updatedProperty.getId();
    }


    private boolean isRequiredInformationAreFilled() {
        FragmentAddPropertyInformationLayoutBinding binding = mBinding.addPropertyInformationLayout;
        return !binding.addPropertyInformationAddress.isEditTextEmpty()
                && !binding.addPropertyInformationTypeCustomViewAutocompleteTextView.getText().toString().trim().isEmpty()
                && !binding.addPropertyInformationArea.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfBathrooms.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfBedrooms.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfRooms.isEditTextEmpty()
                && !binding.addPropertyInformationPrice.isEditTextEmpty();
    }

    private boolean isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(@NonNull Property property) {
        return !mPreviousSavedPropertyAddress.equals(property.getAddress());
    }

    private boolean isPropertyLocationNotEmptyObject(@NonNull PropertyLocation propertyLocation) {
        return propertyLocation.getPropertyId() != 0;
    }

}
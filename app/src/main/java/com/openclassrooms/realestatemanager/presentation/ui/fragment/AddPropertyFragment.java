package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import androidx.core.graphics.PathUtils;
import androidx.navigation.Navigation;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.ManageImageHelper;
import com.openclassrooms.realestatemanager.presentation.utils.PathUtil;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.UPDATE_PROPERTY_COMPLETE;
import static com.picone.core.utils.ConstantParameters.CAMERA_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.GALLERY_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.PROPERTY_TO_ADD;
import static com.picone.core.utils.ConstantParameters.READ_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.WRITE_PERMISSION_CODE;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private List<PropertyPhoto> mPropertyPhotos = new ArrayList<>();
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean isNewPropertyToPersist;
    private String mPreviousSavedPropertyAddress;
    private PhotoRecyclerViewAdapter mAdapter;
    private ManageImageHelper mImageHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mImageHelper = new ManageImageHelper(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CAMERA_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHelper.saveImageInGallery();
                    createPropertyPhoto();
                }
                playLoader(false);
                break;

            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Log.i("TAG", "onActivityResult: " + data.getData().getPath());
                    mImageHelper.setCurrentPhotoPath(PathUtil.getPath(requireContext(), data.getData()));
                    createPropertyPhoto();
                }
        }
    }


    //___________________________________VIEW_____________________________________________

    private void initViewModel() {
        mPropertyViewModel.resetCompletionState();

        if (!isNewPropertyToPersist)
            mPropertyViewModel.setPropertyLocationForProperty(Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        else mPropertyViewModel.setAllPhotosForProperty(new Property());

        mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                case ADD_PROPERTY_COMPLETE:
                    addNewPropertyAdditionalInformation();
                    break;
                case ADD_POINT_OF_INTEREST_COMPLETE:
                    if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.addPropertyFragment)
                        mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
                    break;
            }
        });

        mPropertyViewModel.getAllPropertyPhotosForProperty.observe(getViewLifecycleOwner(), propertyPhotos ->
                mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(propertyPhotos)));

        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));
    }

    private void initRecyclerView() {
        mAdapter = new PhotoRecyclerViewAdapter(mImageHelper.propertyPhotosWithAddButton(new ArrayList<>()));
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        initPropertyTypeDropDownMenu();

        if (!isNewPropertyToPersist)
            initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()));

        else mBinding.addPropertySoldLayout.getRoot().setVisibility(View.GONE);
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

        mUpdateButton.setOnClickListener(v -> initAddButtonClick());
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
                    if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE)
                            && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
                        mImageHelper.dispatchTakePictureIntent();
                        playLoader(true);
                    } else
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                })
                .setPositiveButton("folder", (dialog, which) -> {
                    if (isPermissionGrantedForRequestCode(READ_PERMISSION_CODE)) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                    } else
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
                })
                .create()
                .show();
    }

    //___________________________________ADD AND UPDATE LOGIC_____________________________________________

    private void initAddButtonClick() {
        if (isRequiredInformationAreFilled()) {

            hideSoftKeyboard(mBinding.addPropertyInformationLayout.getRoot());
            playLoader(true);

            if (isNewPropertyToPersist)
                mPropertyViewModel.addProperty(updateProperty(PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()))));

            else {
                Property originalProperty = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue());
                if (!mPropertyPhotos.isEmpty()) persistAllNewPhotos();

                if (isAddressHaveChanged(originalProperty))
                    UpdatePropertyWhenAddressChange(updateProperty(originalProperty), originalProperty);
                else {
                    mPropertyViewModel.updateProperty(updateProperty(originalProperty));
                    mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
                        if (completionState.equals(UPDATE_PROPERTY_COMPLETE)) {
                            playLoader(false);
                            mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
                        }
                    });
                }
            }
        } else
            Toast.makeText(requireContext(), R.string.information_not_filled, Toast.LENGTH_SHORT).show();
    }

    private void addNewPropertyAdditionalInformation() {

        mPropertyViewModel.getAllProperties.observe(getViewLifecycleOwner(), properties -> {
            if (isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(properties.get(properties.size() - 1)))
                mPropertyViewModel.setPropertyLocationForPropertyAddress(properties.get(properties.size() - 1));
        });

        persistAllNewPhotos();

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

    private void UpdatePropertyWhenAddressChange(Property updatedProperty, Property originalProperty) {

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

    private void createPropertyPhoto() {
        PropertyPhoto propertyPhoto = new PropertyPhoto();
        propertyPhoto.setPhoto(mImageHelper.getCurrentPhotoPath());
        propertyPhoto.setDescription("test");
        propertyPhoto.setPropertyId(isNewPropertyToPersist ? Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).size() + 1
                : Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).getId());
        mPropertyPhotos.add(propertyPhoto);
        mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(mPropertyPhotos));
    }

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

    private void persistAllNewPhotos() {
        for (PropertyPhoto propertyPhoto : mPropertyPhotos)
            mPropertyViewModel.addPropertyPhoto(propertyPhoto);
    }

    //___________________________________BOOLEAN_____________________________________________


    private boolean isAddressHaveChanged(@NonNull Property originalProperty) {
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
                && !binding.addPropertyInformationPrice.isEditTextEmpty()
                && !mPropertyPhotos.isEmpty();
    }

    private boolean isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(@NonNull Property property) {
        return !mPreviousSavedPropertyAddress.equals(property.getAddress());
    }

    private boolean isPropertyLocationNotEmptyObject(@NonNull PropertyLocation propertyLocation) {
        return propertyLocation.getPropertyId() != 0;
    }

}
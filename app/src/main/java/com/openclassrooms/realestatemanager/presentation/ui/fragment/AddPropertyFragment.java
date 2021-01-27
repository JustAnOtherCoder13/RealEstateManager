package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentAddPropertyInformationLayoutBinding;
import com.openclassrooms.realestatemanager.presentation.ui.fragment.adapter.PhotoRecyclerViewAdapter;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.openclassrooms.realestatemanager.presentation.utils.ManageImageHelper;
import com.openclassrooms.realestatemanager.presentation.utils.PathUtil;
import com.openclassrooms.realestatemanager.presentation.utils.RecyclerViewItemClickListener;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomMediaDialog;
import com.openclassrooms.realestatemanager.presentation.utils.customView.CustomMediaSetTitleDialog;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.presentation.utils.Utils.isInternetAvailable;
import static com.openclassrooms.realestatemanager.presentation.viewModels.BaseViewModel.CompletionState.UPDATE_PROPERTY_COMPLETE;
import static com.picone.core.utils.ConstantParameters.CAMERA_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.CAMERA_PHOTO_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.CAMERA_VIDEO_INTENT_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.GALLERY_REQUEST_CODE;
import static com.picone.core.utils.ConstantParameters.PROPERTY_TO_ADD;
import static com.picone.core.utils.ConstantParameters.READ_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.WRITE_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.getTodayDate;

public class AddPropertyFragment extends BaseFragment {

    private FragmentAddPropertyBinding mBinding;
    private Property propertyToUpdate;

    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean isNewPropertyToPersist;
    private String mPreviousSavedPropertyAddress;
    private PhotoRecyclerViewAdapter mAdapter;
    private ManageImageHelper mImageHelper;
    private CustomMediaSetTitleDialog setTitleDialog;

    //todo send notification when add or update ok
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mImageHelper = new ManageImageHelper(requireContext());
        setTitleDialog = new CustomMediaSetTitleDialog(requireContext());
        setAppBarVisibility(false);
        initRecyclerView();
        setUpdateButtonIcon(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPreviousSavedPropertyAddress = Objects.requireNonNull(mPropertyViewModel.getAllProperties_.getValue()).get(mPropertyViewModel.getAllProperties_.getValue().size() - 1).propertyLocation.getAddress();
        mPropertyViewModel.isDataLoading.observe(getViewLifecycleOwner(), this::playLoader);
        initClickListener();
        isNewPropertyToPersist = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty_.getValue()).propertyInformation == null;
        initViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        isNewPropertyToPersist = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty_.getValue()).propertyInformation == null;
        setSoldLayoutVisibility();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //manage returned media
            case CAMERA_PHOTO_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHelper.saveImageInGallery();
                    initSetTitleCustomDialog(true, data);
                }
                playLoader(false);
                break;

            case CAMERA_VIDEO_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    initSetTitleCustomDialog(false, data);
                }
                break;

            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mImageHelper.setCurrentPhotoPath(PathUtil.getPath(requireContext(), data.getData()));
                    initSetTitleCustomDialog(true, data);
                }
                break;
        }
    }
    //___________________________________VIEW_____________________________________________

    private void initViewModel() {
        mPropertyViewModel.resetCompletionState();

        if (!isNewPropertyToPersist) {
            //if is not a new property set value for selected property and assign value to propertyToUpdate
            mPropertyViewModel.getSelectedProperty_.observe(getViewLifecycleOwner(), property -> {
                propertyToUpdate = property;
                basePhotoList = property.photos;
                mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(property.photos));
            });
        }
        //else assign value to propertyToUpdate
        else propertyToUpdate = PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()));


        mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                //if add complete mean that we add new property, so add additional information
                case ADD_PROPERTY_COMPLETE:
                    mPropertyViewModel.setPropertyLocationForPropertyAddress(propertyToUpdate);
                    persistAllNewPhotos();
                    break;
                //when point of interest complete, mean that add or update is finish
                case ADD_POINT_OF_INTEREST_COMPLETE:
                    if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.addPropertyFragment) {
                        playLoader(false);
                        mPropertyViewModel.setAllProperties();
                        mNavController.navigate(getResources().getBoolean(R.bool.phone_device) ?
                                R.id.action_addPropertyFragment_to_propertyListFragment
                                : R.id.mapsFragment);
                    }
                    break;
            }
        });
        //assign value to base photoList to know if photo have changed before register
        mPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));
    }

    private List<PropertyPhoto> basePhotoList;

    private void initRecyclerView() {
        mAdapter = new PhotoRecyclerViewAdapter(mImageHelper.propertyPhotosWithAddButton(new ArrayList<>()));
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(mAdapter);
    }

    private void setSoldLayoutVisibility() {
        initPropertyTypeDropDownMenu();
        if (isNewPropertyToPersist)
            mBinding.addPropertySoldLayout.getRoot().setVisibility(View.GONE);
        else
            initViewValueWhenUpdate(mBinding.addPropertyInformationLayout, Objects.requireNonNull(mPropertyViewModel.getSelectedProperty_.getValue()));
    }

    private void initViewValueWhenUpdate(@NonNull FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, @NonNull Property property) {
        mPropertyViewModel.setPhotosToDelete(new ArrayList<>());
        EditText descriptionEditText = mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText;
        AutoCompleteTextView propertyTypeDropDownMenu = mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView;
        addPropertyInformationCustomView.addPropertyInformationPrice.setText(String.valueOf(property.propertyInformation.getPrice()));
        addPropertyInformationCustomView.addPropertyInformationArea.setText(String.valueOf(property.propertyInformation.getPropertyArea()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfBathrooms.setText(String.valueOf(property.propertyInformation.getNumberOfBathrooms()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfBedrooms.setText(String.valueOf(property.propertyInformation.getNumberOfBedrooms()));
        addPropertyInformationCustomView.addPropertyInformationNumberOfRooms.setText(String.valueOf(property.propertyInformation.getNumberOfRooms()));
        addPropertyInformationCustomView.addPropertyInformationAddress.setText(property.propertyLocation.getAddress());
        descriptionEditText.setText(property.propertyInformation.getDescription());
        propertyTypeDropDownMenu.setText(property.propertyInformation.getPropertyType());
    }

    private void initPropertyTypeDropDownMenu() {
        String[] propertyType = getResources().getStringArray(R.array.property_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, propertyType);
        AutoCompleteTextView propertyTypeTextView = (AutoCompleteTextView) mBinding.addPropertyInformationLayout.addPropertyInformationType.getEditText();
        assert propertyTypeTextView != null;
        propertyTypeTextView.setAdapter(adapter);
    }

    private void initSetTitleCustomDialog(boolean isPhoto, Intent data) {
        Objects.requireNonNull(setTitleDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTitleDialog.show();

        if (isPhoto) setTitleDialog.setPhoto(mImageHelper.getCurrentPhotoPath());
        else setTitleDialog.setVideo(data.getData());

        setTitleDialog.setAcceptOnClickListener(v -> {
            hideSoftKeyboard(mBinding.addPropertyInformationLayout.getRoot());
            if (!setTitleDialog.getText().trim().isEmpty()) {
                createPropertyPhoto(setTitleDialog.getText(), isPhoto);
                setTitleDialog.resetEditText();
                setTitleDialog.dismiss();
            } else
                Toast.makeText(requireContext(), "You have to enter a description before accept.", Toast.LENGTH_LONG).show();
        });
    }

    private void initMediaDialog() {
        CustomMediaDialog mediaDialog = new CustomMediaDialog(requireContext());
        mediaDialog.show();
        mediaDialog.okButtonSetOnClickListener(v -> {
            switch (mediaDialog.getIntentRequestCode()) {

                case GALLERY_REQUEST_CODE:
                    if (isPermissionGrantedForRequestCode(READ_PERMISSION_CODE)) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                    } else
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
                    mediaDialog.dismiss();
                    break;

                case CAMERA_PHOTO_INTENT_REQUEST_CODE:
                    if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE)
                            && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
                        startActivityForResult(mImageHelper.getTakePictureIntent(), CAMERA_PHOTO_INTENT_REQUEST_CODE);
                        playLoader(true);
                    } else
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                    mediaDialog.dismiss();
                    break;

                case CAMERA_VIDEO_INTENT_REQUEST_CODE:
                    if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE)
                            && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90);
                        startActivityForResult(takeVideoIntent, CAMERA_VIDEO_INTENT_REQUEST_CODE);
                        playLoader(true);
                    } else
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

                    mediaDialog.dismiss();
                    break;
            }
        });
    }

    //___________________________________CLICK LISTENER_____________________________________________

    private void initClickListener() {
        initAddMediaClickListener();
        initSelectPhotoToDeleteOnLongClickListener();

        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v -> {
            mAdapter.isPhotoHaveBeenDeleted(true);
            propertyToUpdate.photos.removeAll(mPhotosToDelete);
            mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(propertyToUpdate.photos));
            mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(View.GONE);
        });

        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v ->
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(
                        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ?
                                View.VISIBLE : View.GONE));

        setSaveButtonOnClickListener(v -> initAddButtonClick());
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
                    if (position == 0) initMediaDialog();
                });
    }

    //___________________________________ADD AND UPDATE LOGIC_____________________________________________

    private void initAddButtonClick() {
        hideSoftKeyboard(mBinding.addPropertyInformationLayout.getRoot());

        if (isRequiredInformationAreFilled()) {
            //look if sold is checked, if is case a date must be fill
            if (mBinding.addPropertySoldLayout.getRoot().getVisibility() == View.VISIBLE
                    && mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked()
                    && mBinding.addPropertySoldLayout.addPropertySoldEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), R.string.add_date_message, Toast.LENGTH_LONG).show();
                return;
            }

            //if save new property
            if (isNewPropertyToPersist) {
                //if no network can't add new property cause no access at position or point of interest
                if (!isInternetAvailable(requireContext())) {
                    initAlertNoNetworkAvailable();
                    return;
                }
                mPropertyViewModel.addProperty(updateProperty(propertyToUpdate));
            }
            //if is an update
            else {
                Property originalProperty = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty_.getValue());
                //check if address have changed, if case persist new address and get point of interests
                if (isAddressHaveChanged(originalProperty)) {
                    //if no network return cause can't update position or point of interest
                    if (!isInternetAvailable(requireContext())) {
                        initAlertNoNetworkAvailable();
                        return;
                    }
                    UpdatePropertyWhenAddressChange(originalProperty);
                }
                //check if photos have changed, if case persist photos
                if (!propertyToUpdate.photos.containsAll(basePhotoList)
                        || propertyToUpdate.photos.size() != basePhotoList.size())
                    persistAllNewPhotos();
                    //else update property with new value and update property
                else {
                    mPropertyViewModel.updateProperty(updateKnownProperty(originalProperty));
                    mPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
                        if (completionState.equals(UPDATE_PROPERTY_COMPLETE)) {
                            playLoader(false);
                            mPropertyViewModel.setAllProperties();
                            mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
                        }
                    });
                }
            }
        } else
            Toast.makeText(requireContext(), R.string.information_not_filled, Toast.LENGTH_SHORT).show();

        mPropertyViewModel.getLocationForAddress.observe(getViewLifecycleOwner(),propertyLocation -> {
            mPropertyViewModel.setNearBySearchForPropertyLocation(propertyLocation);
            mPropertyViewModel.addPropertyLocationForProperty(propertyLocation);
            propertyToUpdate.propertyLocation=propertyLocation;
        });

        mPropertyViewModel.getMapsPointOfInterest.observe(getViewLifecycleOwner(),pointOfInterests -> {
            mPropertyViewModel.addPropertyPointOfInterest(pointOfInterests);
            propertyToUpdate.pointOfInterests=pointOfInterests;
        });
    }

    private void initAlertNoNetworkAvailable() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_dialog_shape, null));
        builder.setMessage(R.string.no_internet_message);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        });
        builder.show();
    }

    private void addNewPropertyAdditionalInformation() {
        mPropertyViewModel.getAllProperties_.observe(getViewLifecycleOwner(), properties -> {
            // if (isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(properties.get(properties.size() - 1).propertyLocation))
            mPropertyViewModel.setPropertyLocationForPropertyAddress(properties.get(properties.size() - 1));
        });

    }

    private void UpdatePropertyWhenAddressChange(Property originalProperty) {
        Property updatedProperty = updateKnownProperty(originalProperty);
        mPropertyViewModel.updateProperty(updatedProperty);
        mPropertyViewModel.updatePropertyLocation(updatedProperty.propertyLocation);
        mPropertyViewModel.updatePointOfInterest(updatedProperty.pointOfInterests);
    }

    //___________________________________HELPERS_____________________________________________


    //todo review when remove and add new photo and change address same time
    private void createPropertyPhoto(String title, boolean isPhoto) {
        PropertyPhoto propertyPhoto = new PropertyPhoto();
        if (propertyToUpdate.photos == null) propertyToUpdate.photos = new ArrayList<>();
        if (isPhoto)
            propertyPhoto.setPhotoPath(mImageHelper.getCurrentPhotoPath());
        else propertyPhoto.setPhotoPath(setTitleDialog.getVideoPath());

        propertyPhoto.setDescription(title);
        propertyPhoto.setPropertyId(isNewPropertyToPersist ? Objects.requireNonNull(mPropertyViewModel.getAllProperties_.getValue()).size() + 1
                : Objects.requireNonNull(mPropertyViewModel.getSelectedProperty_.getValue()).propertyInformation.getId());
        propertyToUpdate.photos.add(propertyPhoto);
        mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(propertyToUpdate.photos));
    }

    @NonNull
    private Property updateProperty(@NonNull Property originalProperty) {
        if (originalProperty.propertyInformation == null) {
            originalProperty.propertyInformation = new PropertyInformation();
            originalProperty.propertyInformation.setId(Objects.requireNonNull(mPropertyViewModel.getAllProperties_.getValue()).size() + 1);
        }
        if (originalProperty.propertyLocation == null)
            originalProperty.propertyLocation = new PropertyLocation();
        if (originalProperty.pointOfInterests == null)
            originalProperty.pointOfInterests = new ArrayList<>();
        if (originalProperty.photos == null)
            originalProperty.photos = new ArrayList<>();
        originalProperty.propertyLocation.setAddress(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
        //property.propertyLocation.setRegion(originalProperty.propertyLocation.getRegion());
        originalProperty.propertyInformation.setEnterOnMarket(getTodayDate());
        originalProperty.propertyInformation.setId(Objects.requireNonNull(mPropertyViewModel.getAllProperties_.getValue()).size() + 1);
        //originalProperty.propertyInformation.setRealEstateAgentId(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()).getId());
        return updateKnownProperty(originalProperty);
    }

    private Property updateKnownProperty(@NonNull Property property) {
        property.propertyInformation.setNumberOfRooms(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfRooms.getValueForView()));
        property.propertyInformation.setNumberOfBathrooms(Integer.parseInt((mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBathrooms.getValueForView())));
        property.propertyInformation.setNumberOfBedrooms(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationNumberOfBedrooms.getValueForView()));
        property.propertyInformation.setPropertyArea(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationArea.getValueForView()));
        property.propertyInformation.setPrice(Integer.parseInt(mBinding.addPropertyInformationLayout.addPropertyInformationPrice.getValueForView()));
        property.propertyInformation.setPropertyType(mBinding.addPropertyInformationLayout.addPropertyInformationTypeCustomViewAutocompleteTextView.getText().toString());
        property.propertyInformation.setDescription(mBinding.addPropertyDescriptionLayout.addPropertyDescriptionEditText.getText().toString());
        property.propertyInformation.setSold(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked());
        property.propertyInformation.setSoldFrom(mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ? mBinding.addPropertySoldLayout.addPropertySoldEditText.getText().toString() : "");
        return property;
    }

    private void setPhotoToDeleteList(int position, @NonNull CheckBox selectToDeleteCheckBox) {
        int indexForExistingMedia = position - 1;
        if (selectToDeleteCheckBox.isChecked() &&
                !mPhotosToDelete.contains(propertyToUpdate.photos.get(indexForExistingMedia)))
            mPhotosToDelete.add(propertyToUpdate.photos.get(indexForExistingMedia));
        else if (!mPhotosToDelete.isEmpty())
            mPhotosToDelete.remove(propertyToUpdate.photos.get(indexForExistingMedia));
        mPropertyViewModel.setPhotosToDelete(mPhotosToDelete);
    }

    private void persistAllNewPhotos() {
        mPropertyViewModel.resetPhotoForProperty(propertyToUpdate);
        for (PropertyPhoto propertyPhoto : propertyToUpdate.photos)
            mPropertyViewModel.addPropertyPhoto(propertyPhoto);
    }

    //___________________________________BOOLEAN_____________________________________________

    private boolean isAddressHaveChanged(@NonNull Property originalProperty) {
        return originalProperty.propertyLocation.getAddress() != null && !originalProperty.propertyLocation.getAddress().equalsIgnoreCase(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
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
                && !propertyToUpdate.photos.isEmpty();
    }

    private boolean isNewPropertyAddressNotEqualPreviousSavedPropertyAddress(@NonNull PropertyLocation propertyLocation) {
        return !mPreviousSavedPropertyAddress.equals(propertyLocation.getAddress());
    }
}
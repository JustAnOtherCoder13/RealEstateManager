package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;
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
import com.openclassrooms.realestatemanager.presentation.viewModels.PropertyViewModel;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyInformation;
import com.picone.core.domain.entity.PropertyLocation;
import com.picone.core.domain.entity.PropertyPhoto;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.presentation.utils.NotificationHelper.createNotification;
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
    private PropertyViewModel mInnerPropertyViewModel;
    private PhotoRecyclerViewAdapter mAdapter;
    private ManageImageHelper mImageHelper;
    private CustomMediaSetTitleDialog mGetMediaDialog;

    private Property mSelectedProperty;
    private List<PropertyPhoto> mPhotosToDelete = new ArrayList<>();
    private boolean isNewPropertyToPersist;
    private boolean isPhotoListHaveBeenChanged;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAddPropertyBinding.inflate(getLayoutInflater());
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mImageHelper = new ManageImageHelper(requireContext());
        mGetMediaDialog = new CustomMediaSetTitleDialog(requireContext());
        mInnerPropertyViewModel = new ViewModelProvider(this).get(PropertyViewModel.class);
        mUpdateButton.setEnabled(true);
        setAppBarVisibility(false);
        setUpdateButtonIcon(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInnerPropertyViewModel.isDataLoading.observe(getViewLifecycleOwner(), this::playLoader);
        mInnerPropertyViewModel.setPropertyLocationForPropertyAddress(new Property());
        isPhotoListHaveBeenChanged = false;
        isNewPropertyToPersist = Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).propertyInformation == null;
        mAdapter = new PhotoRecyclerViewAdapter(mImageHelper.propertyPhotosWithAddButton(new ArrayList<>()));
        mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView.setAdapter(mAdapter);
        initViewModel();
        initClickListener();
        initPropertyTypeDropDownMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //manage returned media
            case CAMERA_PHOTO_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mImageHelper.saveImageInGallery();
                    initGetMediaDialog(true, data);
                }
                playLoader(false);
                break;
            case CAMERA_VIDEO_INTENT_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    initGetMediaDialog(false, data);
                }
                break;
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    mImageHelper.setCurrentPhotoPath(PathUtil.getPath(requireContext(), data.getData()));
                    initGetMediaDialog(true, data);
                }
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    //mPropertyViewModel.getSelectedProperty_.getValue() can't be null cause checked on isNewPropertyToPersist
    private void initViewModel() {
        mInnerPropertyViewModel.resetCompletionState();
        if (!isNewPropertyToPersist) {//if is not a new property set value for selected property
            mSelectedProperty = mPropertyViewModel.getSelectedProperty.getValue();
            //to have ref of stored point of interest
            mInnerPropertyViewModel.setAllPointOfInterestForProperty(mSelectedProperty);
            mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(mSelectedProperty.photos));
            initViewIfNotNewProperty(mBinding.addPropertyInformationLayout, mSelectedProperty);
        } else {//else create new property and hide sold layout
            mBinding.addPropertySoldLayout.getRoot().setVisibility(View.GONE);
            mSelectedProperty = PROPERTY_TO_ADD(Objects.requireNonNull(mAgentViewModel.getAgent.getValue()));
        }
        mInnerPropertyViewModel.getPhotosToDelete.observe(getViewLifecycleOwner(), photosToDelete ->
                mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(photosToDelete.isEmpty() ? View.GONE : View.VISIBLE));
    }

    //___________________________________VIEW_____________________________________________


    private void initViewIfNotNewProperty(@NonNull FragmentAddPropertyInformationLayoutBinding addPropertyInformationCustomView, @NonNull Property property) {
        mInnerPropertyViewModel.setPhotosToDelete(new ArrayList<>());
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

    private void initAddNewMediaDialog() {
        CustomMediaDialog mediaDialog = new CustomMediaDialog(requireContext());
        mediaDialog.show();
        mediaDialog.okButtonSetOnClickListener(v -> {
            switch (mediaDialog.getIntentRequestCode()) {
                case GALLERY_REQUEST_CODE:
                    launchGalleryIntent(mediaDialog);
                    break;
                case CAMERA_PHOTO_INTENT_REQUEST_CODE:
                    launchPhotoIntent(mediaDialog);
                    break;
                case CAMERA_VIDEO_INTENT_REQUEST_CODE:
                    launchVideoIntent(mediaDialog);
                    break;
            }
        });
    }
    //___________________________________CLICK LISTENER_____________________________________________

    private void initClickListener() {
        initMediaRecyclerViewAddMediaImageClickListener();
        initMediaRecyclerViewLongClickListener();
        initMediaRecyclerViewDeleteMediaClickListener();
        initSoldCheckBoxClickListener();
        setSaveButtonOnClickListener(v -> initAddButtonClick());
    }

    private void initSoldCheckBoxClickListener() {
        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.setOnClickListener(v ->
                mBinding.addPropertySoldLayout.addPropertySoldEditText.setVisibility(
                        mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked() ?
                                View.VISIBLE : View.GONE));
    }

    private void initMediaRecyclerViewDeleteMediaClickListener() {
        mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setOnClickListener(v -> {
            //to reset checkBox in adapter
            mAdapter.isPhotoHaveBeenDeleted(true);
            //delete photo for property but don't push change still save clicked
            mSelectedProperty.photos.removeAll(mPhotosToDelete);
            mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(mSelectedProperty.photos));
            mBinding.addPropertyMediaLayout.detailCustomViewDeleteButton.setVisibility(View.GONE);
            isPhotoListHaveBeenChanged = true;
        });
    }

    private void initMediaRecyclerViewLongClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemLongClickListener((recyclerView, position, v) -> {
                    //if 0 is add media image
                    if (position == 0) return false;
                    CheckBox selectToDeleteCheckBox = v.findViewById(R.id.property_detail_item_check_box);
                    selectToDeleteCheckBox.setChecked(!selectToDeleteCheckBox.isChecked());
                    selectToDeleteCheckBox.setVisibility(selectToDeleteCheckBox.isChecked() ? View.VISIBLE : View.GONE);
                    setPhotoToDeleteList(position, selectToDeleteCheckBox);
                    return true;
                });
    }

    private void initGetMediaDialog(boolean isPhoto, Intent data) {
        mGetMediaDialog.show();
        if (isPhoto) mGetMediaDialog.setPhoto(mImageHelper.getCurrentPhotoPath());
        else mGetMediaDialog.setVideo(data.getData());
        mGetMediaDialog.setAcceptOnClickListener(v -> {
            hideSoftKeyboard(requireView());
            if (!mGetMediaDialog.getText().trim().isEmpty()) {
                createPropertyPhoto(mGetMediaDialog.getText(), isPhoto);
                mGetMediaDialog.resetEditText();
                mGetMediaDialog.dismiss();
            } else
                Toast.makeText(requireContext(), R.string.enter_description, Toast.LENGTH_LONG).show();
        });
    }

    private void initMediaRecyclerViewAddMediaImageClickListener() {
        RecyclerViewItemClickListener.addTo(mBinding.addPropertyMediaLayout.detailCustomViewRecyclerView, R.layout.fragment_add_property)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (position == 0) initAddNewMediaDialog();
                });
    }

    private void initAddButtonClick() {
        mUpdateButton.setEnabled(false);
        hideSoftKeyboard(mBinding.addPropertyInformationLayout.getRoot());
        // observers
        addAdditionalInformationOrNavigateUp();
        setPointOfInterestAndSaveOrUpdateLocation();
        saveOrUpdatePointsOfInterest();

        if (isRequiredInformationAreFilled()) {
            if (isNewPropertyToPersist) { //if save new property
                //if no network can't add new property cause no access at position or point of interest
                if (checkNetworkAvailability()) return;
                mInnerPropertyViewModel.addProperty(updateProperty(mSelectedProperty));
            } else { //if is an update
                //look if sold is checked, if is case a date must be fill
                if (checkSoldValue()) return;
                //check if photos have changed, if case persist photos
                if (isPhotoListHaveBeenChanged) persistAllNewPhotos();
                //check if address have changed, if case persist new address and get point of interests
                if (!mSelectedProperty.propertyLocation.getAddress().equalsIgnoreCase(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView())) {
                    //if no network return cause can't update position or point of interest
                    if (checkNetworkAvailability()) return;
                    UpdatePropertyWhenAddressChange(mSelectedProperty);
                }//else no need to search info on network, just update property with new values
                else updatePropertyAndNavigateUp();
            }
            return;
        }
        Toast.makeText(requireContext(), R.string.information_not_filled, Toast.LENGTH_SHORT).show();
        mUpdateButton.setEnabled(true);
    }

    //___________________________________HELPERS_____________________________________________

    private void updatePropertyAndNavigateUp() {
        mInnerPropertyViewModel.updatePropertyInformation(updateKnownProperty(mSelectedProperty));
        mInnerPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            if (completionState.equals(UPDATE_PROPERTY_COMPLETE)) {
                playLoader(false);
                createNotification(requireContext(), message("You have update property :\n"), "UPDATE PROPERTY");
                mNavController.navigate(R.id.action_addPropertyFragment_to_propertyListFragment);
            }
        });
    }

    private boolean checkSoldValue() {
        if (mBinding.addPropertySoldLayout.getRoot().getVisibility() == View.VISIBLE
                && mBinding.addPropertySoldLayout.addPropertySoldCheckbox.isChecked()
                && mBinding.addPropertySoldLayout.addPropertySoldEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), R.string.add_date_message, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void saveOrUpdatePointsOfInterest() {
        mInnerPropertyViewModel.getMapsPointOfInterest.observe(getViewLifecycleOwner(), mapsPointOfInterests -> {
            if (isNewPropertyToPersist)
                mInnerPropertyViewModel.addPropertyPointOfInterest(mapsPointOfInterests);
            else
                mInnerPropertyViewModel.updatePointOfInterest(mapsPointOfInterests);
            mSelectedProperty.pointOfInterests = mapsPointOfInterests;
        });
    }

    private void setPointOfInterestAndSaveOrUpdateLocation() {
        mInnerPropertyViewModel.getLocationForAddress.observe(getViewLifecycleOwner(), propertyLocation -> {
            if (propertyLocation.getAddress() != null) {
                mInnerPropertyViewModel.setNearBySearchForPropertyLocation(propertyLocation);
                if (isNewPropertyToPersist) {
                    mInnerPropertyViewModel.addPropertyLocationForProperty(propertyLocation);
                } else mInnerPropertyViewModel.updatePropertyLocation(propertyLocation);
                mSelectedProperty.propertyLocation = propertyLocation;
            }
        });
    }

    private void addAdditionalInformationOrNavigateUp() {
        mInnerPropertyViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                //if add complete mean that we add new property, so add additional information
                case ADD_PROPERTY_COMPLETE:
                    mInnerPropertyViewModel.setPropertyLocationForPropertyAddress(mSelectedProperty);
                    persistAllNewPhotos();
                    break;
                //when point of interest complete, mean that add or update when address change is finish
                case ADD_POINT_OF_INTEREST_COMPLETE:
                    if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.addPropertyFragment) {
                        //playLoader(false);
                        if (isNewPropertyToPersist)
                            createNotification(requireContext(),
                                    message(getString(R.string.notification_add_start_message)), getString(R.string.notification_add_message_title));
                        else {
                            createNotification(requireContext(),
                                    message(getString(R.string.notification_update_start_message)), getString(R.string.notification_update_message_title));}

                        mNavController.navigate(getResources().getBoolean(R.bool.phone_device) ?
                                R.id.action_addPropertyFragment_to_propertyListFragment
                                : R.id.action_addPropertyFragment_to_mapsFragment);
                    }
                    break;
            }
        });
    }

    private String message(String startMessage) {
        return startMessage
                .concat(getString(R.string.notification_id))
                .concat(String.valueOf(mSelectedProperty.propertyInformation.getId()))
                .concat(getString(R.string.notification_address))
                .concat(mSelectedProperty.propertyLocation.getAddress())
                .concat(getString(R.string.notification_number_of_photos))
                .concat(String.valueOf(mSelectedProperty.photos.size()))
                .concat(getString(R.string.notification_number_of_point_of_interst))
                .concat(String.valueOf(mSelectedProperty.pointOfInterests.size()));

    }

    private void UpdatePropertyWhenAddressChange(Property originalProperty) {
        Property updatedProperty = updateKnownProperty(originalProperty);
        updatedProperty.propertyLocation.setAddress(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
        mInnerPropertyViewModel.updatePropertyInformation(updatedProperty);
        mInnerPropertyViewModel.setPropertyLocationForPropertyAddress(updatedProperty);
    }

    private void createPropertyPhoto(String title, boolean isPhoto) {
        PropertyPhoto propertyPhoto = new PropertyPhoto();
        if (mSelectedProperty.photos == null) mSelectedProperty.photos = new ArrayList<>();
        if (isPhoto) propertyPhoto.setPhotoPath(mImageHelper.getCurrentPhotoPath());
        else propertyPhoto.setPhotoPath(mGetMediaDialog.getVideoPath());

        propertyPhoto.setDescription(title);
        propertyPhoto.setPropertyId(isNewPropertyToPersist ?
                Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).size() + 1
                : Objects.requireNonNull(mPropertyViewModel.getSelectedProperty.getValue()).propertyInformation.getId());
        mSelectedProperty.photos.add(propertyPhoto);
        mAdapter.updatePhotos(mImageHelper.propertyPhotosWithAddButton(mSelectedProperty.photos));
        isPhotoListHaveBeenChanged = true;
    }

    @NonNull
    private Property updateProperty(@NonNull Property originalProperty) {
        if (originalProperty.propertyInformation == null)
            originalProperty.propertyInformation = new PropertyInformation();
        if (originalProperty.propertyLocation == null)
            originalProperty.propertyLocation = new PropertyLocation();
        if (originalProperty.pointOfInterests == null)
            originalProperty.pointOfInterests = new ArrayList<>();
        if (originalProperty.photos == null)
            originalProperty.photos = new ArrayList<>();
        originalProperty.propertyLocation.setPropertyId(originalProperty.propertyInformation.getId());
        originalProperty.propertyLocation.setAddress(mBinding.addPropertyInformationLayout.addPropertyInformationAddress.getValueForView());
        originalProperty.propertyInformation.setEnterOnMarket(getTodayDate());
        originalProperty.propertyInformation.setId(Objects.requireNonNull(mPropertyViewModel.getAllProperties.getValue()).size() + 1);
        return updateKnownProperty(originalProperty);
    }

    @NonNull
    @Contract("_ -> param1")
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
                !mPhotosToDelete.contains(mSelectedProperty.photos.get(indexForExistingMedia)))
            mPhotosToDelete.add(mSelectedProperty.photos.get(indexForExistingMedia));
        else if (!mPhotosToDelete.isEmpty())
            mPhotosToDelete.remove(mSelectedProperty.photos.get(indexForExistingMedia));
        mInnerPropertyViewModel.setPhotosToDelete(mPhotosToDelete);
    }

    private void persistAllNewPhotos() {
        mInnerPropertyViewModel.deleteSelectedPhotosForProperty(mPhotosToDelete);
        for (PropertyPhoto propertyPhoto : mSelectedProperty.photos)
            mInnerPropertyViewModel.addPropertyPhoto(propertyPhoto);
    }

    //___________________________________INTENT_____________________________________________

    private void launchVideoIntent(CustomMediaDialog mediaDialog) {
        if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE)
                && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 90);
            startActivityForResult(takeVideoIntent, CAMERA_VIDEO_INTENT_REQUEST_CODE);
            playLoader(true);
        } else
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

        mediaDialog.dismiss();
    }

    private void launchPhotoIntent(CustomMediaDialog mediaDialog) {
        if (isPermissionGrantedForRequestCode(CAMERA_PERMISSION_CODE)
                && isPermissionGrantedForRequestCode(WRITE_PERMISSION_CODE)) {
            startActivityForResult(mImageHelper.getTakePictureIntent(), CAMERA_PHOTO_INTENT_REQUEST_CODE);
            playLoader(true);
        } else
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        mediaDialog.dismiss();
    }

    private void launchGalleryIntent(CustomMediaDialog mediaDialog) {
        if (isPermissionGrantedForRequestCode(READ_PERMISSION_CODE)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        } else
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        mediaDialog.dismiss();
    }

    //___________________________________BOOLEAN_____________________________________________

    private boolean isRequiredInformationAreFilled() {
        FragmentAddPropertyInformationLayoutBinding binding = mBinding.addPropertyInformationLayout;
        return !binding.addPropertyInformationAddress.isEditTextEmpty()
                && !binding.addPropertyInformationTypeCustomViewAutocompleteTextView.getText().toString().trim().isEmpty()
                && !binding.addPropertyInformationArea.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfBathrooms.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfBedrooms.isEditTextEmpty()
                && !binding.addPropertyInformationNumberOfRooms.isEditTextEmpty()
                && !binding.addPropertyInformationPrice.isEditTextEmpty()
                && !mSelectedProperty.photos.isEmpty();
    }

    private boolean checkNetworkAvailability() {
        if (!isInternetAvailable(requireContext())) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_dialog_shape, null));
            builder.setMessage(R.string.no_internet_message);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            });
            builder.show();
            return true;
        }
        return false;
    }
}
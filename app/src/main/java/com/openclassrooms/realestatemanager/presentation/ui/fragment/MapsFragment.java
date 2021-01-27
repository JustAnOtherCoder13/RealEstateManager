package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapsBinding;
import com.openclassrooms.realestatemanager.presentation.ui.main.BaseFragment;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;
import com.picone.core.domain.entity.PropertyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.openclassrooms.realestatemanager.presentation.utils.BitmapConverterUtil.getBitmapFromVectorOrDrawable;
import static com.picone.core.utils.ConstantParameters.LOCATION_PERMISSION_CODE;
import static com.picone.core.utils.ConstantParameters.MAPS_CAMERA_LARGE_ZOOM;
import static com.picone.core.utils.ConstantParameters.MAPS_CAMERA_NEAR_ZOOM;
import static com.picone.core.utils.ConstantParameters.MAPS_KEY;

public class MapsFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private FragmentMapsBinding mBinding;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MarkerOptions mMarkerOptions = new MarkerOptions();
    private List<Marker> mPointOfInterestMarkers = new ArrayList<>();
    private Marker mMarkerToAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        MAPS_KEY = this.getResources().getString(R.string.google_maps_key);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater, container, false);
        mBinding.mapView.getMapAsync(this);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        setAppBarVisibility(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMapView(savedInstanceState);
        mPropertyViewModel.setAllPropertiesAndAllValues();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        updateLocationUI();
        mMap.setOnInfoWindowClickListener(this);
        if (getView() != null)
            mPropertyViewModel.getAllProperties_.observe(getViewLifecycleOwner(), this::initMarkersValue);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if (isPropertyMarker(marker)) {
            mPropertyViewModel.setSelectedProperty_(getPropertyForId(marker.getTitle()));
            if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.mapsFragment)
                mNavController.navigate(R.id.action_mapsFragment_to_propertyDetailFragment);
            else mNavController.navigate(R.id.propertyDetailFragment);
        }
    }

    //----------------------------------INIT MAPS----------------------------------------------

    private void initMapView(@Nullable Bundle savedInstanceState) {
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.onResume();
        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //todo remove cause already in main
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this.requireActivity(),
                new String[]{ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_CODE);
    }

    private void updateLocationUI() {
        try {
            mMap.setMyLocationEnabled(true);
            if (isPermissionGrantedForRequestCode(LOCATION_PERMISSION_CODE)) fetchLastLocation();
            else requestLocationPermission();

        } catch (SecurityException e) {
            Log.e(getString(R.string.exception), Objects.requireNonNull(e.getMessage()));
        }
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null)
                setUpMapPosition(new LatLng(location.getLatitude(), location.getLongitude()), MAPS_CAMERA_LARGE_ZOOM);

        });
    }

    private void setUpMapPosition(@NonNull LatLng location, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location).zoom(zoom).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    //----------------------------------MAPS MARKERS----------------------------------------------

    private void initMarkersValue(@NonNull List<PropertyFactory> allProperties) {
        mMap.clear();
        if (mMap != null) {
            for (PropertyFactory propertyFactory : allProperties)
                mMap.addMarker(mMarkerOptions.position(new LatLng(propertyFactory.propertyLocation.getLatitude(), propertyFactory.propertyLocation.getLongitude()))
                        .title(String.valueOf(propertyFactory.property.getId()))
                        .snippet(String.valueOf(propertyFactory.property.getAddress()))
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), R.drawable.ic_fragment_detail_location_24))));


            mMap.setOnMarkerClickListener(marker -> {
                if (!marker.isInfoWindowShown()) marker.showInfoWindow();
                if (isPropertyMarker(marker)) {
                    setUpMapPosition(marker.getPosition(), MAPS_CAMERA_NEAR_ZOOM);
                    placePointOfInterestMarkersForClickedProperty(getPropertyForId(marker.getTitle()));
                }
                return true;
            });

            mMap.setOnMyLocationButtonClickListener(() -> {
                removePointOfInterest();
                return false;
            });
        }
    }

    private void placePointOfInterestMarkersForClickedProperty(@NonNull PropertyFactory property) {
        removePointOfInterest();
        for (PointOfInterest pointOfInterest : property.pointOfInterests)
            createPointOfInterestMarker(pointOfInterest);
    }

    //----------------------------------HELPERS----------------------------------------------

    private void createPointOfInterestMarker(@NonNull PointOfInterest pointOfInterest) {
        MarkerOptions pointOfInterestMarkerOptions = new MarkerOptions();

        pointOfInterestMarkerOptions
                .position(new LatLng(pointOfInterest.getLatitude(), pointOfInterest.getLongitude()))
                .title(pointOfInterest.getType())
                .snippet(pointOfInterest.getName());

        Glide.with(requireContext())
                .load(pointOfInterest.getIcon())
                .apply(getResources().getBoolean(R.bool.phone_device) ?
                        new RequestOptions()
                        : new RequestOptions().override(40))
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        pointOfInterestMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), resource)));
                        mMarkerToAdd = mMap.addMarker(pointOfInterestMarkerOptions);
                        mPointOfInterestMarkers.add(mMarkerToAdd);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private boolean isPropertyMarker(@NonNull Marker marker) {
        return getPropertyForId(marker.getTitle()).property.getAddress() != null;
    }

    private void removePointOfInterest() {
        for (Marker marker : mPointOfInterestMarkers)
            marker.remove();
    }
}
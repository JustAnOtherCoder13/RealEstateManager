package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
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
import com.openclassrooms.realestatemanager.presentation.utils.AgentRegionUnderResponsibility;
import com.picone.core.domain.entity.PointOfInterest;
import com.picone.core.domain.entity.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.openclassrooms.realestatemanager.presentation.utils.BitmapConverterUtil.getBitmapFromVectorOrDrawable;
import static com.picone.core.utils.ConstantParameters.MAPS_CAMERA_ZOOM;
import static com.picone.core.utils.ConstantParameters.MAPS_KEY;
import static com.picone.core.utils.ConstantParameters.REQUEST_CODE;

public class MapsFragment extends BaseFragment implements GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback {

    private FragmentMapsBinding mBinding;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MarkerOptions mMarkerOptions = new MarkerOptions();


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
        mPropertyViewModel.setAllProperties();
        initPropertyMarkers();
        initPointOfInterestMarkersForClickedProperty();
    }

    private void initPropertyMarkers() {
        mPropertyViewModel.getPropertyLocationForProperty.observe(getViewLifecycleOwner(), propertyLocation -> {
            if (mMap!=null){
            mMarkerOptions.position(new LatLng(propertyLocation.getLatitude(), propertyLocation.getLongitude()))
                    .title(String.valueOf(propertyLocation.getPropertyId()))
                    .snippet(getPropertyForId(String.valueOf(propertyLocation.getPropertyId())).getAddress());

            if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.BOUCHES_DU_RHONE.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone1_24))));
            else if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.VAR.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone2_24))));
            else if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.VAUCLUSE.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone3_24))));
            else
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(), R.drawable.ic_location_on_24))));
        }});
    }

    private void initPointOfInterestMarkersForClickedProperty() {
        mPropertyViewModel.getAllPointOfInterestForProperty.observe(getViewLifecycleOwner(), allPointOfInterests -> {
            allMarkers.clear();
            for (PointOfInterest pointOfInterest : allPointOfInterests) {
                MarkerOptions pointOfInterestMarkerOptions = new MarkerOptions();

                pointOfInterestMarkerOptions
                        .position(new LatLng(pointOfInterest.getLatitude(), pointOfInterest.getLongitude()))
                        .title(pointOfInterest.getType())
                        .snippet(pointOfInterest.getName());

                Glide.with(requireContext())
                        .load(pointOfInterest.getIcon())
                        .centerCrop()
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                pointOfInterestMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorOrDrawable(requireContext(),resource)));
                                markerToAdd = mMap.addMarker(pointOfInterestMarkerOptions);
                                updateMarkerList(markerToAdd);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        });
    }

    private void initMapView(@Nullable Bundle savedInstanceState) {
        mBinding.mapView.onCreate(savedInstanceState);
        mBinding.mapView.onResume();
        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();
        mMap.setOnInfoWindowClickListener(this);
        mPropertyViewModel.getAllProperties.observe(getViewLifecycleOwner(), this::initMarkers);
    }

    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            mLocationPermissionGranted = true;
            updateLocationUI();

        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }
    }

    private void updateLocationUI() {
        try {
            if (mLocationPermissionGranted) fetchLastLocation();
            else enableMyLocation();

        } catch (SecurityException e) {
            Log.e(getString(R.string.exception), Objects.requireNonNull(e.getMessage()));
        }
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]
                    {ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                mCurrentLocation = location;
                setUpMapCurrentPosition();
            }
        });
    }

    private void setUpMapCurrentPosition() {
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(MAPS_CAMERA_ZOOM).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private List<Marker> allMarkers = new ArrayList<>();
    private Marker markerToAdd;


    private void initMarkers(@NonNull List<Property> allProperties) {
        mMap.clear();
        allMarkers.clear();

        for (Property property : allProperties){
            mPropertyViewModel.setPropertyLocationForProperty(property);}

        mMap.setOnMarkerClickListener(marker -> {
            if (getPropertyForId(marker.getTitle()).getAddress() != null) {
                mPropertyViewModel.setAllPointOfInterestForProperty(getPropertyForId(marker.getTitle()));
                initPointOfInterestForProperty(mPropertyViewModel.getAllPointOfInterestForProperty.getValue());
            }

            return false;
        });
    }

    private void updateMarkerList(Marker marker) {
        if (!allMarkers.contains(marker)) allMarkers.add(marker);
    }

    private void initPointOfInterestForProperty(List<PointOfInterest> pointOfInterests) {

        if (pointOfInterests != null) {
            for (Marker marker1 : allMarkers)
                for (PointOfInterest pointOfInterest : pointOfInterests) {
                    if (marker1.getSnippet().equalsIgnoreCase(pointOfInterest.getName())) {
                        marker1.remove();
                    }
                }
        }
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        mPropertyViewModel.setSelectedProperty(getPropertyForId(marker.getTitle()));
        mPropertyViewModel.setAllPhotosForProperty(getPropertyForId(marker.getTitle()));
        mNavController.navigate(R.id.action_mapsFragment_to_propertyDetailFragment);
    }


}
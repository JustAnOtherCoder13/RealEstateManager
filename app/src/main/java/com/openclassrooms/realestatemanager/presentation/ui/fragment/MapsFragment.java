package com.openclassrooms.realestatemanager.presentation.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
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

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.openclassrooms.realestatemanager.presentation.utils.GetBitmapFromVectorUtil.getBitmapFromVectorDrawable;
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


    private void initMarkers(@NonNull List<Property> allProperties) {
        mMap.clear();
        for (Property property : allProperties)
            mPropertyViewModel.setPropertyLocationForProperty(property);


        mPropertyViewModel.getPropertyLocationForProperty.observe(getViewLifecycleOwner(), propertyLocation -> {
            mMarkerOptions.position(new LatLng(propertyLocation.getLatitude(), propertyLocation.getLongitude()))
                    .title(String.valueOf(propertyLocation.getPropertyId()))
                    .snippet(getPropertyForId(String.valueOf(propertyLocation.getPropertyId())).getAddress());
            Log.i("TAG", "initMarkers: " + propertyLocation.getRegion() + " " + getResources().getString(AgentRegionUnderResponsibility.BOUCHES_DU_RHONE.label));
            if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.BOUCHES_DU_RHONE.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone1_24))));
            else if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.VAR.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone2_24))));
            else if (propertyLocation.getRegion().equalsIgnoreCase(getResources().getString(AgentRegionUnderResponsibility.VAUCLUSE.label)))
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_fragment_detail_location_on_zone3_24))));
            else
                mMap.addMarker(mMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_location_on_24))));

            //mPropertyViewModel.setStaticMapForLatLng(propertyLatLgn);
        });

        mMap.setOnMarkerClickListener(marker ->{
            mPropertyViewModel.setAllPointOfInterestForProperty(getPropertyForId(marker.getTitle()));
            setPointOfInterestForProperty();
            return false;
        } );
    }

    private void setPointOfInterestForProperty(){
        mPropertyViewModel.getAllPointOfInterestForProperty.observe(getViewLifecycleOwner(),pointOfInterests -> {
            for (PointOfInterest pointOfInterest:pointOfInterests){
                MarkerOptions pointOfInterestMarkerOptions = new MarkerOptions();

                pointOfInterestMarkerOptions
                        .position(new LatLng(pointOfInterest.getLatitude(),pointOfInterest.getLongitude()))
                        .title(pointOfInterest.getType())
                        .snippet(pointOfInterest.getName());

                 Glide.with(requireContext())
                        .load(pointOfInterest.getIcon())
                        .centerCrop()
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                pointOfInterestMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(resource)));
                                mMap.addMarker(pointOfInterestMarkerOptions);

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                Log.i("TAG", "setPointOfInterestForProperty: "+pointOfInterest.getIcon());
            }
        });
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        mPropertyViewModel.setSelectedProperty(getPropertyForId(marker.getTitle()));
        mPropertyViewModel.setAllPhotosForProperty(getPropertyForId(marker.getTitle()));
        mNavController.navigate(R.id.action_mapsFragment_to_propertyDetailFragment);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
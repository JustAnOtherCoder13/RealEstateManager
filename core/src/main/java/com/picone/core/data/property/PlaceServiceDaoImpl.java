package com.picone.core.data.property;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.picone.core.data.service.RetrofitClient;
import com.picone.core.domain.entity.pojo.propertyLocation.PropertyLocationPojo;
import com.picone.core.domain.entity.pojo.staticMap.StaticMapPojo;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.picone.core.utils.ConstantParameters.MAPS_CAMERA_ZOOM;

public class PlaceServiceDaoImpl {

    @Inject
    RetrofitClient retrofitClient;

    public PlaceServiceDaoImpl(RetrofitClient retrofitClient) {
        this.retrofitClient = retrofitClient;
    }

    public Observable<PropertyLocationPojo> getPropertyLocationForAddress(String address, String googleKey) {
        return retrofitClient.googlePlaceService().getLocationForAddress(address, googleKey);
    }

    //TODO error on static map, due to return type as png?
    public Observable<StaticMapPojo> getStaticMapForLatLng(LatLng location, String googleKey) {

        return Observable.create(emitter -> {
            String latLng = location.latitude+(",")+location.longitude;
            retrofitClient.googlePlaceService().getStaticMapForLatLng(latLng, MAPS_CAMERA_ZOOM, "140x140", googleKey)
                    .subscribe(new Observer<StaticMapPojo>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.i("TAG", "onSubscribe: "+d);
                        }

                        @Override
                        public void onNext(StaticMapPojo staticMapPojo) {
                            Log.i("TAG", "onNext: "+staticMapPojo);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("TAG", "onError: "+e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }


}

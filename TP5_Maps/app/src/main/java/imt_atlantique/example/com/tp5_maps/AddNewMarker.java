package imt_atlantique.example.com.tp5_maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AddNewMarker implements GoogleMap.OnMapLongClickListener {

    public LatLng mylatLng;
    GoogleMap googleMap;
    Activity activity;
    Bitmap imageBitmap;

    public AddNewMarker(GoogleMap googleMap, Activity activity, Bitmap imageBitmap){
        this.googleMap = googleMap;
        this.activity = activity;
        this.imageBitmap = imageBitmap;
    }
    static final int REQUEST_IMAGE = 1;

    @Override
    public void onMapLongClick(LatLng latLng) {
        mylatLng=latLng;
        Intent addMarker = new Intent(activity, EditMarkerINFO.class);
        activity.startActivityForResult(addMarker, REQUEST_IMAGE);

        MyMapsActivity.latLngLast = latLng;

        Log.i("test AddNewMarker", "ok");
    }
}

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

    //Variables globales
    static final int REQUEST_IMAGE = 1;
    public LatLng mylatLng;
    GoogleMap googleMap;
    Activity activity;
    Bitmap imageBitmap;


    //Constructeur
    public AddNewMarker(GoogleMap googleMap, Activity activity, Bitmap imageBitmap){
        this.googleMap = googleMap;
        this.activity = activity;
        this.imageBitmap = imageBitmap;
    }


    /**
     * Quand on intercepte un click long sur une position de la carte,
     * Cette méthode qui prend en paramètre la position,
     * appelle l'activité permettant d'éditer les infos du marker
     */

    @Override
    public void onMapLongClick(LatLng latLng) {
        mylatLng=latLng;
        Intent addMarker = new Intent(activity, EditMarkerINFO.class);
        activity.startActivityForResult(addMarker, REQUEST_IMAGE);

        MyMapsActivity.latLngLast = latLng;

        Log.i("test AddNewMarker", "ok");
    }
}

package imt_atlantique.example.com.tp5_maps;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerClickListener implements GoogleMap.OnMarkerClickListener {
    Activity activity;

    public MarkerClickListener (Activity activity) {
        this.activity = activity;
    }


    /**
     * Quand on intercepte un click sur un marker,
     * Cette méthode qui prend en paramètre le marker,
     * appelle l'activité permettant d'afficher ses infos
     */

    @Override
    public boolean onMarkerClick(Marker marker) {
        MarkerOptions nMarker = new MarkerOptions()
                .position(marker.getPosition())
                .title(marker.getTitle());

        Intent displayMarker = new Intent(activity, DisplayInfoMap.class);
        displayMarker.putExtra("Info_Marker", nMarker);
        activity.startActivity(displayMarker);

        Log.i("test", "display marker Listener Ok");

        return true;
    }
}
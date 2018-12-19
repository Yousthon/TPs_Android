package imt_atlantique.example.com.tp5_maps;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MapClickListener implements GoogleMap.OnMapClickListener {

    private Context context;
    public MapClickListener(Context context){
        this.context=context;
        Log.i("test","ok");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("test","ok");
        CharSequence text = "info : "+latLng;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();
    }
}

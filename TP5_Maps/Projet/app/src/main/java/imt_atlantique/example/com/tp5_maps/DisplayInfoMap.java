package imt_atlantique.example.com.tp5_maps;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayInfoMap extends AppCompatActivity implements OnMapReadyCallback {

    //Variables globales
    GoogleMap map;
    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Pour retablir le thème de base apres le splash screen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_infos_layout);

        Intent intent = getIntent();
        Bundle a = intent.getExtras();

        markerOptions = (MarkerOptions) a.get("Info_Marker");
        TextView view = findViewById(R.id.T_MarkerTitle);
        view.setText(markerOptions.getTitle());
        TextView view2 = findViewById(R.id.T_Coordonnées);
        view2.setText(markerOptions.getPosition().toString());

        Log.i("test display 1", "position "+markerOptions.getPosition().toString()+" Titre "+markerOptions.getTitle() );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        Log.i("test", "display marker text and pos set 2");
    }

    //region Affichage des infos marker sélectionné sur une carte zoomée

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        Log.i("test display 2", "position "+markerOptions.getPosition().toString()+" Titre "+markerOptions.getTitle() );

        map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(), 11);
        googleMap.animateCamera(cameraUpdate);

        Log.i("test", "display marker onMapReady");

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setCompassEnabled (true) ;
        map.getUiSettings().setMyLocationButtonEnabled (true);

        Log.i("test", "display marker onMapReadyFin");
    }

    //endregion

    //region Gestion du menu

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_map, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return(true);
            case R.id.satellite:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return(true);
            case R.id.hybrid:
                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    //endregion
}

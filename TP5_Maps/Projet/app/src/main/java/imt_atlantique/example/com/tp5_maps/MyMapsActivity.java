package imt_atlantique.example.com.tp5_maps;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import static imt_atlantique.example.com.tp5_maps.EditMarkerINFO.scaleDown;

public class MyMapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    //region Déclaration des Variables globales  et initialisations
    Bundle savedInstanceState;
    public Bitmap imageBitmap;
    private GoogleMap mMap;
    static final String KEY = "DATA_MAP_KEY";
    public static ArrayList<MarkerOptions> markerOptionsList;
    public static ArrayList<Marker> markerList ;

    public static LatLng latLngLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Pour retablir le thème de base apres le splash screen
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_my_maps);

        markerOptionsList = new ArrayList<>();
        markerList = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    //endregion

    //region On fait les traitements ici
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near IMT & Yamoussoukro (Cote d'Ivoire).
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //region Restauration des valeurs si activité killed ou rotation ecran

        if (this.savedInstanceState != null) {
            markerOptionsList = (ArrayList) savedInstanceState.getSerializable("list");
            if (markerOptionsList != null)
                for (MarkerOptions m : markerOptionsList)
                    markerList.add(mMap.addMarker(m));
        }
        else
            LoadPreferences(mMap);

        Log.i("test restore ", "Ok");

        //endregion

        //region traitemps sur la Map

        //Ajout de listener de clic sur la carte
        MapClickListener listener = new MapClickListener(this);
        mMap.setOnMapClickListener(listener);

        // Add a marker in IMT Atlantique and move the camera
        LatLng IMT_Atlantique = new LatLng(48.359285, -4.569933);
        mMap.addMarker(new MarkerOptions().position(IMT_Atlantique).title("Marker in IMT"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(IMT_Atlantique));

        // Ajouter marker à yamoussourko (Capitale de la Côte d'Ivoire)
        LatLng yamoussoukro = new LatLng(6.82055,-5.27674);
        mMap.addMarker(new MarkerOptions().position(yamoussoukro).title("Yamoussoukro").icon(BitmapDescriptorFactory.fromResource(R.drawable.civ)));

        // On vérifie que le système nous permet de positionner la carte à la position courante
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
            Log.i("test position", String.valueOf(mMap.getUiSettings().isCompassEnabled()));
        }
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings ().setCompassEnabled (true) ;
        mMap.getUiSettings ().setMyLocationButtonEnabled (true);

        //Listener sur la carte permettant l'ajout dynamique de markers apres appuis long
        mMap.setOnMapLongClickListener(new AddNewMarker(mMap,this, imageBitmap));

        //Clic sur un marker
        mMap.setOnMarkerClickListener(new MarkerClickListener(this));

        //endregion
    }

    // Ici on ajoute les nouveaux markers ajoutés
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            //On recupère le bundle renvoyé pour avoir les infos (titre marker et uri de l'image)
            Bundle marker_data = data.getExtras().getBundle("Marker_data");

            String titre = marker_data.getString("titre", "marker"),
            uri_text = marker_data.getString("Bitmap") ;

            Uri uri = Uri.parse(uri_text);
            Log.i("test uri", String.valueOf(uri));

            //On crée un nouveau MarkerOptions qui nous permettra d'add le marker
            MarkerOptions marker = new MarkerOptions();
            marker.position(latLngLast).title(titre);

            //Si une image a été renvoyée
            if (uri != null) {
                try {
                    //On redimensionne l'image. La fonction est définie dans la classe EditMarkerINFO
                    imageBitmap = scaleDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 72, true);
                    marker.icon(BitmapDescriptorFactory.fromBitmap(imageBitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //On sauvegarde le marker et ses options
            Marker markerToRemoveLater = mMap.addMarker(marker);
            markerList.add(markerToRemoveLater);
            markerOptionsList.add(marker);
        }
    }

    //endregion

    //region Sauvegarde des Markers

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", markerOptionsList);
        Log.i("test OnSave", "Ok");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SavePreferences();
        Log.i("test save", "data saved");
    }

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(MyMapsActivity.KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("listSize", markerList.size());

        for(int i = 0; i < markerList.size(); i++){
            editor.putFloat("lat"+i, (long) markerList.get(i).getPosition().latitude);
            editor.putFloat("long"+i, (long) markerList.get(i).getPosition().longitude);
            editor.putString("title"+i, markerList.get(i).getTitle());
        }

        editor.apply();
    }

    private void LoadPreferences(GoogleMap mMap){
        SharedPreferences sharedPreferences = getSharedPreferences(MyMapsActivity.KEY, MODE_PRIVATE);

        int size = sharedPreferences.getInt("listSize", 0);
        markerList = new ArrayList<>();
        String title;

        for(int i = 0; i < size; i++) {

            double lat = (double) sharedPreferences.getFloat("lat" + i, 0);
            double longit = (double) sharedPreferences.getFloat("long" + i, 0);
            title = sharedPreferences.getString("title" + i, null);

            markerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, longit))
                    .title(title)));
        }
    }

    //endregion Sauvegarde des Markers

    //region Gestion du menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return(true);

            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return(true);

            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                //Suppresion de tous els amrkers ajoutés dynamiquement
            case R.id.resetMap:
                if(markerList != null) {
                    for (Marker m : markerList)
                        m.remove();
                    markerList.clear();
                }
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    //endregion Gestion du menu
}

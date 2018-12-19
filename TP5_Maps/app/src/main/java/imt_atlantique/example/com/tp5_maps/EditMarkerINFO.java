package imt_atlantique.example.com.tp5_maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;

import static android.util.Log.i;

public class EditMarkerINFO extends AppCompatActivity {

    ImageView MarkerPhoto;
    Bitmap imageIcon;
    boolean PhotoSelected = false;
    LinearLayout layoutEditMarker, imageViewLayout, save;
    static final int CAPTURE_IMAGE_REQUEST = 2;
    static final int PICK_IMAGE_REQUEST = 1;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Pour retablir le thème de base apres le splash screen
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_marker_info);
    }

    //Prendre Photo
    public void takePhotoFromCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
        }
    }

    //Selectionner Photo
    public void PickPhotoFromGalley(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            traiterImage(data);
        } else if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            traiterImage(data);
        }
    }

    public void traiterImage(Intent data) {
        uri = data.getData();
        //On recupère le Layout main et on en crée un autre pour l'imageView
        layoutEditMarker = findViewById(R.id.AjoutMarkerLayout);
        imageViewLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // margin = 20
        params.setMargins(20, 20, 20, 20);
        params.weight = 1;
        imageViewLayout.setLayoutParams(params);

        try {
            Bitmap bitmap = scaleDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 500, true);
            imageIcon = scaleDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 72, true);

            MarkerPhoto = new ImageView(this);
            MarkerPhoto.setLayoutParams(params);
            MarkerPhoto.setImageBitmap(bitmap);
            imageViewLayout.addView(MarkerPhoto);

            //Si image déjà ajoutée, on l'enlève
            if (layoutEditMarker.getChildCount() > 3) layoutEditMarker.removeView(save);

            layoutEditMarker.addView(imageViewLayout, layoutEditMarker.getChildCount() - 1);
            save = imageViewLayout;
            PhotoSelected = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMarker(View view) {
        EditText TitreMarker = findViewById(R.id.editTextTitre);
        String titre = TitreMarker.getText().toString(), flag="";
        Bundle data = new Bundle();

        if ((titre == null || titre.isEmpty()) && !PhotoSelected)
            Toast.makeText(this,
                    getString(R.string.Text_Toast),
                    Toast.LENGTH_LONG).show();
        else {

            if ((titre == null || titre.isEmpty()))
                titre = getString(R.string.Marker_nom_parDefaut);

            data.putString("Bitmap",  String.valueOf(uri));
            data.putCharSequence("titre", titre);
            data.putCharSequence("flag", flag);
            Intent intent = new Intent(getApplicationContext(), MyMapsActivity.class);
            intent.putExtra("Marker_data", data);
            setResult(RESULT_OK, intent);

            Log.i("test", " savemarker ok");

            finish();
        }
    }

    //Methode pour redimensionner l'image
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,height, filter);
        return newBitmap;
    }
}

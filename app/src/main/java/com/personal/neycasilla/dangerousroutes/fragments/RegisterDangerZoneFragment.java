package com.personal.neycasilla.dangerousroutes.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.personal.neycasilla.dangerousroutes.R;
import com.personal.neycasilla.dangerousroutes.model.DangerZone;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterDangerZoneFragment extends Fragment {

    private static final int GALLERY_PERMISSION = 100;
    private static final int CAMERA_PERMISSION = 101;
    private Intent pictureActionIntent;
    private registerADangerZone registerADangerZone;
    private EditText editTextDescription;
    private EditText editTextSector;
    private EditText editTextProvincia;
    private ImageView dangerImage;
    private Button entryDanger;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_danger_zone, container, false);
        editTextDescription = view.findViewById(R.id.title_description_edit_text);
        editTextProvincia = view.findViewById(R.id.provincia_description_edit_text);
        editTextSector = view.findViewById(R.id.sector_description_edit_text);
        entryDanger = view.findViewById(R.id.danger_register_button);
        dangerImage = view.findViewById(R.id.danger_image_view);

        dangerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFinder();
            }
        });
        entryDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheData();
            }
        });
        return view;
    }

    public void setRegisterADangerZone(RegisterDangerZoneFragment.registerADangerZone registerADangerZone) {
        this.registerADangerZone = registerADangerZone;
    }

    public interface registerADangerZone{
        LatLng positionOfDanger();
    }

    private void saveTheData() {
        dangerImage.buildDrawingCache();
        Bitmap bitmap= dangerImage.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream);
        final String NAME_OF_FILE = "Danger"+new Date().getTime()+".jpg";
        storageReference = FirebaseStorage.getInstance().getReference("DangerImages");
        StorageReference  reference = storageReference.child(NAME_OF_FILE);

        UploadTask uploadTask = reference.putBytes(stream.toByteArray());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"No se pudo guardar la imagen",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                DangerZone dangerZone = setDangerZone(registerADangerZone.positionOfDanger(),NAME_OF_FILE);
                savingInDatabase(dangerZone);

                getActivity().finish();
            }
        });


    }
    private void imageFinder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Forma de carga de la imangen");

        alertDialog.setPositiveButton("Camara",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    useCamera();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE} , CAMERA_PERMISSION);
                }
            }
        });
        alertDialog.setNegativeButton("Galeria",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            gallery();
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION);
                        }
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (CAMERA_PERMISSION == requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        useCamera();
                    }
                }
            }
        } else if (GALLERY_PERMISSION == requestCode) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        gallery();
                    }
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == CAMERA_PERMISSION){
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
                dangerImage.setImageBitmap(bitmap);
            }else if(requestCode == GALLERY_PERMISSION){
                if(data!=null){
                    Uri imageUri = data.getData();
                    String[] path = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(imageUri, path, null, null, null);
                    cursor.moveToFirst();
                    String completePath = cursor.getString(cursor.getColumnIndex(path[0]));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(completePath, options);

                    dangerImage.setImageBitmap(bitmap);

                    cursor.close();
                }
            }
        }
    }

    private void useCamera() {
        pictureActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureActionIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(pictureActionIntent,CAMERA_PERMISSION);
        }
    }

    private void gallery() {
        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pictureActionIntent,GALLERY_PERMISSION);
    }

    private DangerZone setDangerZone(LatLng latLng,String filename) {
        DangerZone dangerZone = new DangerZone();
        dangerZone.setLatitude(latLng.latitude);
        dangerZone.setLongitud(latLng.longitude);
        dangerZone.setImageUrls(filename);
        dangerZone.setDangerName(editTextDescription.getText().toString());
        dangerZone.setProvincia(editTextProvincia.getText().toString());
        dangerZone.setSector(editTextSector.getText().toString());
        dangerZone.setDateMarked(Calendar.getInstance().getTime());
        return dangerZone;
    }

    private void savingInDatabase(DangerZone dangerZone) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("DangerZones");
        String id = databaseReference.push().getKey();
        dangerZone.setKey(id);
        databaseReference.child(id).setValue(dangerZone);
    }

}

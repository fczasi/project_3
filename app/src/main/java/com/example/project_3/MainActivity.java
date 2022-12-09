package com.example.project_3;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button captb;
    ImageView imageView;
    RelativeLayout ldata;
    TextView kraj, miasto, dlugosc, szerokosc;
    FusedLocationProviderClient fusedLocationProviderClient;
    Switch lokalizacja;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captb = findViewById(R.id.captureb);
        imageView = findViewById(R.id.imgview);
        ldata = findViewById(R.id.localizationdata);
        kraj = findViewById(R.id.kraj);
        miasto = findViewById(R.id.miasto);
        dlugosc = findViewById(R.id.dlugosc);
        szerokosc = findViewById(R.id.szerokosc);
        lokalizacja = findViewById(R.id.lokalizacja);

        captb.setEnabled(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        lokalizacja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    ldata.setVisibility(View.VISIBLE);
                }else
                {
                    ldata.setVisibility(View.INVISIBLE);
                }
            }
        });

        captb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            captb.setEnabled(false);
        } else {
            captb.setEnabled(true);
        }
        captb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        szerokosc.setText(Html.fromHtml(
                                "<font colour='#6200EE'><b>Latitude :</b><br></font>" + addresses.get(0).getLatitude()
                        ));
                        dlugosc.setText(Html.fromHtml(
                                "<font colour='#6200EE'><b>Longitude :</b><br></font>" + addresses.get(0).getLongitude()
                        ));
                        kraj.setText(Html.fromHtml(
                                "<font colour='#6200EE'><b>Kraj :</b><br></font>" + addresses.get(0).getCountryName()
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            captb.setVisibility(View.GONE);
        }
    }
}
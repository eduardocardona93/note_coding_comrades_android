package com.madt.note_coding_comrades_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.madt.note_coding_comrades_android.R;
import com.madt.note_coding_comrades_android.utilities.NoteUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;

    // Location Demo with FUSED LOCATION PROVIDER CLIENT

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    TextView locationDetailsTV, longitudeTV, latitudeTV;
    Geocoder geocoder;
    List<Address> addresses;
    
    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int FASTEST_INTERVAL = 3000; // 3 seconds

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();



    Button btnPlay, btnRecord;
    String pathSave = "";
    MediaRecorder mediaRecorder;
    SeekBar scrubberSld;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    Boolean isRecording = false, isPlaying = false;
    final private static String RECORDED_FILE = "/audio.3gp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnPlay = findViewById(R.id.playerBtn);
        btnPlay.setVisibility(View.GONE);
        btnRecord = findViewById(R.id.recorderBtn);
        scrubberSld = findViewById(R.id.scrubberSld);
        scrubberSld.setVisibility(View.GONE);

        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        // set the volume of played media to maximum.
        audioManager.setStreamVolume (AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.RECORD_AUDIO);


        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording){
                    if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && hasPermission(Manifest.permission.RECORD_AUDIO)) {
                        pathSave = getExternalCacheDir().getAbsolutePath()
                                + RECORDED_FILE;

                        setUpMediaRecorder();

                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();


                            btnPlay.setEnabled(false);
                            btnPlay.setVisibility(View.GONE);
                            scrubberSld.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                        } catch (IllegalStateException ise) {
                            // make something ...
                            ise.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else
                        Log.i(TAG, "Set permissions: ");
                    //TODO
                }else{

                    mediaRecorder.stop();
                    btnPlay.setEnabled(true);
                    btnPlay.setVisibility(View.VISIBLE);
                    scrubberSld.setVisibility(View.VISIBLE);


                }
                isRecording = !isRecording;
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying){
                    btnRecord.setEnabled(false);
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_pause, 0, 0, 0);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        scrubberSld.setMax(mediaPlayer.getDuration());
                        scrubberSld.setProgress(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            scrubberSld.setProgress(0);
                            btnRecord.setEnabled(true);

                            btnPlay.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);

                        }
                    });

                    mediaPlayer.start();
                    Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                }else{
                    btnRecord.setEnabled(true);
                    btnPlay.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
                    mediaPlayer.pause();

                }
                isPlaying = !isPlaying;

            }
        });
        scrubberSld.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isPlaying && mediaPlayer != null){
                    scrubberSld.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 300);

        // LOCATION INIT
        geocoder = new Geocoder(this,Locale.getDefault());
        longitudeTV = findViewById(R.id.longitudeTV);
        latitudeTV = findViewById(R.id.latitudeTV);
        locationDetailsTV = findViewById(R.id.locationDetailsTV);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        // add permissions


        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // this is a proper place to check the google play services availability

        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(MainActivity.this, "No Google Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
            Log.i(TAG, "onPostResume: ");
            findLocation();
        }
    }


    private void setUpMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);

    }


    //------------------------location methods
    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longitudeTV.setText(String.format("Lng: %s", location.getLongitude()));
                    latitudeTV.setText(String.format("Lat: %s", location.getLatitude()));
                    locationDetailsTV.setText(String.format("Accuracy: %s,Altitude: %s", location.getAccuracy(), location.getAltitude()));
                }
            }
        });

        startUpdateLocation();
    }

    private void startUpdateLocation() {
        Log.d(TAG, "startUpdateLocation: ");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    longitudeTV.setText(String.format("Lng: %s", location.getLongitude()));
                    latitudeTV.setText(String.format("Lat: %s", location.getLatitude()));
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        locationDetailsTV.setText(String.format("Accuracy: %s,Altitude: %s \n %s, %s, %s, %s, %s ,%s",
                                location.getAccuracy(), location.getAltitude() , address, city, state, country, postalCode, knownName
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
             return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    //permissions
    private List<String> permissionsToRequest(List<String> permissions) {
        ArrayList<String> results = new ArrayList<>();
        for (String perm : permissions) {
            if (!hasPermission(perm))
                results.add(perm);
        }

        return results;
    }

    private boolean hasPermission(String perm) {
            return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (String perm : permissions) {
                if (!hasPermission(perm))
                    permissionsRejected.add(perm);
            }

            if (permissionsRejected.size() > 0) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("The location permission is mandatory")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE);

                                    }
                                }).setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                }
            }
        }
    }
}
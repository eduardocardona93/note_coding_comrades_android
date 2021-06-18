package com.madt.note_coding_comrades_android.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.madt.note_coding_comrades_android.R;
import com.madt.note_coding_comrades_android.model.Note;
import com.madt.note_coding_comrades_android.model.NoteAppViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class NoteDetailActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    public static final int READ_EXTERNAL_STORAGE_CODE = 2;

    // Location Demo with FUSED LOCATION PROVIDER CLIENT

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    TextView locationDetailsTV;
    Geocoder geocoder;
    LatLng latLangNote = null;
    List<Address> addresses;

    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int FASTEST_INTERVAL = 1000; // 3 seconds
    private static final int SMALLEST_DISPLACEMENT = 200; // 200 meters

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();

    private NoteAppViewModel noteAppViewModel;
    ArrayList<Note> noteList = new ArrayList<>();
    private int catID = 0;
    ImageButton btnPlay, btnRecord;
    ImageView btnBack, uploadImage, mapIcon;
    TextView saveTV;
    View audioBannerV;
    EditText titleET, detailET;
    String pathSave = "", recordFile = null;
    MediaRecorder mediaRecorder;
    SeekBar scrubberSld;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    Boolean isRecording = false, isPlaying = false, imageSet = false;

    ActivityResultLauncher<Intent> someActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        // audio elements
        audioBannerV = findViewById(R.id.audioBannerV);
        btnPlay = findViewById(R.id.playerBtn);
        btnPlay.setVisibility(View.GONE);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    btnRecord.setEnabled(false);
                    //btnPlay.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_pause, 0, 0, 0);

                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(pathSave);
                        mediaPlayer.prepare();
                        scrubberSld.setMax(mediaPlayer.getDuration());
                        scrubberSld.setProgress(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    mediaPlayer.start();
                    audioBannerV.setBackgroundColor(Color.parseColor("#FF00DD00"));
                    btnPlay.setImageResource(R.drawable.pause_icon_f);
                    Toast.makeText(NoteDetailActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            scrubberSld.setProgress(0);
                            btnRecord.setEnabled(true);
                            btnPlay.setImageResource(R.drawable.play_btn_f);
                            audioBannerV.setBackgroundResource(R.color.dark_app_color);

                        }
                    });
                } else {
                    btnRecord.setEnabled(true);
                    //btnPlay.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_media_play, 0, 0, 0);
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.play_btn_f);
                    audioBannerV.setBackgroundResource(R.color.dark_app_color);

                }
                isPlaying = !isPlaying;

            }
        });
        btnRecord = findViewById(R.id.recorderBtn);
        btnRecord.setOnClickListener(v -> {
            if (!isRecording) {
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) && hasPermission(Manifest.permission.RECORD_AUDIO)) {
                    recordFile = "/" + UUID.randomUUID().toString() + ".3gp";
                    pathSave = getExternalCacheDir().getAbsolutePath()  + recordFile ;
                    setUpMediaRecorder();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                        btnPlay.setEnabled(false);
                        btnPlay.setVisibility(View.GONE);
                        scrubberSld.setVisibility(View.GONE);
                        Toast.makeText(NoteDetailActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                        audioBannerV.setBackgroundColor(Color.parseColor("#FFFF0000"));
                    } catch (IllegalStateException ise) {
                        // make something ...
                        ise.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else {

                mediaRecorder.stop();
                btnPlay.setEnabled(true);
                btnPlay.setVisibility(View.VISIBLE);
                scrubberSld.setVisibility(View.VISIBLE);
                audioBannerV.setBackgroundResource(R.color.dark_app_color);




            }
            isRecording = !isRecording;
        });
        scrubberSld = findViewById(R.id.scrubberSld);
        scrubberSld.setVisibility(View.GONE);
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
        // top bar elements
        btnBack = findViewById(R.id.backBtn);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        saveTV = findViewById(R.id.saveTV);
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString().trim();
                String detail = detailET.getText().toString().trim();

                 if (title.isEmpty()) {
                    alertBox("Title can not be empty!");
                } else if (detail.isEmpty()) {
                    alertBox("Description can not be empty!");
                }else if (latLangNote == null) {
                    alertBox("Wait until the location is set");
                 } else {
                     byte[] imageInByte = null;
                     if(imageSet){
                         Bitmap bitmap = ((BitmapDrawable) uploadImage.getDrawable()).getBitmap();
                         ByteArrayOutputStream baos = new ByteArrayOutputStream();
                         bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                         imageInByte = baos.toByteArray();
                     }
                    noteAppViewModel.insertNote(new Note(catID, title, detail,  imageInByte,recordFile , latLangNote.latitude ,latLangNote.longitude));

                    finish();
                }
            }
        });
        // image elements
        uploadImage = findViewById(R.id.uploadImage);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
                } else {
                    pickImageFromGalary();
                }
            }
        });

        // location elements
        locationDetailsTV = findViewById(R.id.locationDetailsTV);
        locationDetailsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailActivity.this, MapsActivity.class);
                intent.putExtra("note_longitude", latLangNote.longitude);
                intent.putExtra("note_latitude",  latLangNote.latitude);
                startActivity(intent);
            }
        });
        mapIcon = findViewById(R.id.mapIcon);
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailActivity.this, MapsActivity.class);
                intent.putExtra("note_longitude", latLangNote.longitude);
                intent.putExtra("note_latitude",  latLangNote.latitude);
                startActivity(intent);
            }
        });
        // text elements
        titleET = findViewById(R.id.titleET);
        detailET = findViewById(R.id.detailET);


        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        // set the volume of played media to maximum.
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


        catID = getIntent().getIntExtra(NoteListActivity.CATEGORY_ID, 0);


        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isPlaying && mediaPlayer != null) {
                    scrubberSld.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 300);

        // LOCATION INIT
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // view model
        noteAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(NoteAppViewModel.class);

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            uploadImage.setImageURI(data.getData());
                            imageSet = true;
                        }
                    }
                });

        // add permissions
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.RECORD_AUDIO);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0)
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        else
            startUpdateLocation();
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
                            Toast.makeText(NoteDetailActivity.this, "No Google Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
            Log.i(TAG, "onPostResume: ");
//            findLocation();
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
    @SuppressLint("MissingPermission")
    private void findLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    locationDetailsTV.setText(String.format("Accuracy: %s,Altitude: %s", location.getAccuracy(), location.getAltitude()));
                }
            }
        });

        startUpdateLocation();
    }

    @SuppressLint("MissingPermission")
    private void startUpdateLocation() {
        Log.d(TAG, "startUpdateLocation: ");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); // sets the geocoder object

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    try {
                        if(latLangNote == null)
                            latLangNote = new LatLng(location.getLatitude(), location.getLongitude());

                        addresses = geocoder.getFromLocation(latLangNote.latitude, latLangNote.longitude, 1);
                        String address  = ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        if (addresses != null && addresses.size() > 0) { // if the addressList gets a result
                            address = ""; // empty the address message
                            // street name
                            if (addresses.get(0).getThoroughfare() != null) // if there is a street name
                                address += addresses.get(0).getThoroughfare() + ", "; // add the street name
                            if (addresses.get(0).getPostalCode() != null)  // if there is a postal code name
                                address += addresses.get(0).getPostalCode() + ", "; // add the postal code name
                            if (addresses.get(0).getLocality() != null)  // if there is a city name
                                address += addresses.get(0).getLocality() + ", "; // add the city name
                            if (addresses.get(0).getAdminArea() != null)  // if there is a province name
                                address += addresses.get(0).getAdminArea(); // add the province name

                        }
                        locationDetailsTV.setText(address);
                    } catch (Exception e) {
                        e.printStackTrace(); // catch the error
                    }


                }
            }
        };
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
                        new AlertDialog.Builder(NoteDetailActivity.this)
                                .setMessage("The permission is mandatory")
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
        } else if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGalary();
            }
        }
    }

    // method that will display the alert dialog
    public void alertBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NoteDetailActivity.this);
        builder.setTitle("Alert");
        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void pickImageFromGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }
}
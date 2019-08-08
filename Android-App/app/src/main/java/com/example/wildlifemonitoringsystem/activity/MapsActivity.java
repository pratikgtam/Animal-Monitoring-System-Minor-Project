package com.example.wildlifemonitoringsystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wildlifemonitoringsystem.R;
import com.example.wildlifemonitoringsystem.api.ApiClient;
import com.example.wildlifemonitoringsystem.api.ApiInterface;
import com.example.wildlifemonitoringsystem.model.AnimalDetails;
import com.example.wildlifemonitoringsystem.model.BodyTemprAll;
import com.example.wildlifemonitoringsystem.model.GeoLocationAll;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3;
    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private double mOriginLatitude;
    private double mOriginLongitude;
    private double mplaceLatitude;
    private double mplaceLongitude;
    private Point destinationPosition;
    private MapView mapView;
    private String placeName;
    private ProgressBar progressBar;
    private static final String TAG = "MapsActivity";
    private Integer count = 0;
    public static final int REQUEST_LOCATION = 001;
    GoogleApiClient googleApiClient;
    LocationComponent locationComponent;

    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    Context context;
    private double originLatitude;
    private double originLongitude;
    PendingResult<LocationSettingsResult> pendingResult;
    List<LatLng> latLngs = new ArrayList<>();
    private Button buttonRecentLocation, buttonLocationHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
//        MobileAds.initialize(this,
//                getString(R.string.ads_app_id));
        context = this;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        mapView = findViewById(R.id.mapView);
        progressBar = findViewById(R.id.progressbar);
        buttonLocationHistory = findViewById(R.id.b_location_history);
        buttonRecentLocation = findViewById(R.id.b_recent_location);
//        mplaceLatitude = 27.669257;
//        mplaceLongitude = 85.257821;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        toolbar();
//        openInGoogleMap();
        getUserCoordinates();
        //adsBanner();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mEnableGps();
        }

    }


    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Map");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void setCameraView(MapboxMap mapboxMap, Double latitude, Double longitute, int zoom) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitute))
                // Sets the new camera position
                .zoom(zoom)
                // Sets the zoom to level 10
                .tilt(20)// Set the camera tilt to 20 degrees
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 1500);

    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        enableLocationComponent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // String category = bundle.getString("CATEGORY");

            loadFeed(bundle.getString("ID"));

        }
//        ImageView imageView = findViewById(R.id.iv_gps);
//        imageView.setOnClickListener(view -> {
//            enableLocationComponent();
//
//            count = count + 1;
//            if (originLocation != null) {
//                enableLocationComponent();
//                Integer zoom = null;
//                if (count % 2 == 0) {
//                    zoom = 15;
//                } else {
//                    zoom = 16;
//                }
//                setCameraView(mapboxMap, originLocation.getLatitude(), originLocation.getLongitude(), zoom);
//            }
////Set camera view and enablelocationcomponentcan't work together.
//        });
        Point originPosition = Point.fromLngLat(mOriginLongitude, mOriginLatitude);
        destinationPosition = Point.fromLngLat(mplaceLongitude, mplaceLatitude);
        // variables for adding a marker

        // setMarker(mplaceLatitude, mplaceLongitude, "Location", "Comedy");
        // setCameraView(mapboxMap, mplaceLatitude, mplaceLongitude, 15);

        //getRoute(originPosition, destinationPosition);


    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        //hideProgressBar();
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            //
                            // Toast.makeText(MapsActivity.this, "No routes found, make sure you set the right user and access token.", Toast.LENGTH_LONG).show();
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            Toast.makeText(MapsActivity.this, "No Route Found", Toast.LENGTH_LONG).show();
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
//                        DecimalFormat df = new DecimalFormat("#.##");
//                        Double duration = response.body().routes().get(0).duration() / 60;
//                        duration = Double.valueOf(df.format(duration));
//                        Double distance = response.body().routes().get(0).distance() / 1000;
//                        distance = Double.valueOf(distance);
                        //  button.setText("Du and Dis " +durationn + " " +distance);
//                        if (response.body().routes().get(0).duration() != null && response.body().routes().get(0).duration() != 0 && response.body().routes().get(0).distance() != 0) {
//                            TextView textView = findViewById(R.id.category);
//                            TextView textView1 = findViewById(R.id.tv_duration);
//                            String distance = "Distance: " + String.format("%.2f", response.body().routes().get(0).distance() / 1000) + " Km";
//                            textView.setText(distance);
//                            String duration = "Duration: " + String.format("%.2f", response.body().routes().get(0).duration() / 60) + " minutes drive";
//                            textView1.setText(duration);
//
//                        }
                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//the include method will calculate the min and max bound.
                        builder.include(new LatLng(origin.latitude(), origin.longitude()));
                        builder.include(new LatLng(destination.latitude(), destination.longitude()));
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen
                        LatLngBounds bounds = builder.build();

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mapboxMap.animateCamera(cu);

//                        CameraPosition position = new CameraPosition.Builder()
//                                .target(new LatLng(origin.latitude(), origin.longitude()))
//                                .target(new LatLng(destination.latitude(), destination.longitude()))
//                                .zoom(15)
//                                .tilt(20) // Set the camera tilt to 20 degrees
//                                .build();
//                        mapboxMap.animateCamera(CameraUpdateFactory
//                                .newCameraPosition(position), 1500);
//                    }
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        //  Toast.makeText(MapsActivity.this, "Error Getting Your Location ", Toast.LENGTH_LONG).show();
                        setCameraView(mapboxMap, mplaceLatitude, mplaceLongitude, 10);
                        //   hideProgressBar();
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    private void enableLocationComponent() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        // Check if permissions are enabled and if not request
        // Activate the MapboxMap LocationComponent to show user location
        // Adding in LocationComponentOptions is also an optional parameter
        locationComponent = mapboxMap.getLocationComponent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationComponent.activateLocationComponent(this);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.NORMAL);

        // Set the component's camera mode
        //locationComponent.setCameraMode(CameraMode.TRACKING);
        originLocation = locationComponent.getLastKnownLocation();


        //else {
        //     Toast.makeText(this, " Getting your location... Make sure GPS is active", Toast.LENGTH_SHORT).show();

//                    setCameraView(mapboxMap,originLocation.getLatitude(), originLocation.getLongitude(), 15);
//                    enableLocationComponent();
        //  LatLng originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

        // }


    }

    private void setMarker(Double latitude, Double longitude, String title, String snippet) {
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title)
                .snippet(snippet)
        );
    }

//    private void openInGoogleMap() {
//        Button button = findViewById(R.id.b_in_google_map);
//        button.setText("Open in Google Map");
//        button.setOnClickListener(view -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://maps.google.com/maps?daddr=" + mplaceLatitude + "," + mplaceLongitude));
//            intent.setPackage("com.google.android.apps.maps");
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            }
//        });
//    }


    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
    }

    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
        Log.d(TAG, "logggg mEnableGps: ");
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Log.d(TAG, "logggg mLocationSetting: top");
        mResult();
        Log.d(TAG, "logggg mLocationSetting: ");
    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        Log.d(TAG, "logggg mResult: ");
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(MapsActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialogggg.


                        break;
                }
            }

        });
    }


    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //  Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show();
                        getUserCoordinates();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(context, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        // Toast.makeText(context, "Gps onConnected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "logggg onConnected: onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Toast.makeText(context, "Gps onConnectionSuspended", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "logggg onConnected: onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //Toast.makeText(context, "Gps onConnectionFailed", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "logggg onConnected: onConnectionFailed");

    }

    public void getUserCoordinates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        originLatitude = location.getLatitude();
                        originLongitude = location.getLongitude();
                        // Toast.makeText(MapsActivity.this, "latitude " + originLatitude + "longitude " + originLongitude, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "logggg onComplete: latitude" + originLatitude);
                    }

                } else {
                }
                // Toast.makeText(MapsActivity.this, "Couldn't get your location", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadFeed(String id) {
        loading();
        try {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//            HashMap<String, String> headerMap = new HashMap<>();
//            headerMap.put("access-token", getString(R.string.access_token));

            Call<AnimalDetails> call = apiInterface.getDetails(id);
            call.enqueue(new Callback<AnimalDetails>() {

                @Override
                public void onResponse(@NonNull Call<AnimalDetails> call, @NonNull Response<AnimalDetails> response) {
                    assert response.body() != null;
                    generateFeed(response.body());
                    loaded();
                }

                @Override
                public void onFailure(@NonNull Call<AnimalDetails> call, @NonNull Throwable t) {
                    loaded();
                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            //  Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            loaded();
        }
    }

    private void generateFeed(AnimalDetails body) {
        List<GeoLocationAll> geoLocationAlls = new ArrayList<>(body.getGeoLocationAll());
        Collections.reverse(geoLocationAlls);
        setCameraView(mapboxMap, geoLocationAlls.get(0).getGeoLocation().get(0), geoLocationAlls.get(0).getGeoLocation().get(1), 15);
        int increaseBy = 1;
        if (geoLocationAlls.size() > 20) {
            increaseBy = geoLocationAlls.size() / 20;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(String.valueOf(geoLocationAlls.get(0).getTimestramp())));
        String date = DateFormat.format("dd-MMM hh:mm:ss", calendar).toString();
        setMarker(geoLocationAlls.get(0).getGeoLocation().get(0), geoLocationAlls.get(0).getGeoLocation().get(1), "Animals Location", date);

        buttonRecentLocation.setOnClickListener(view -> {
            mapboxMap.removeAnnotations();
            setMarker(geoLocationAlls.get(0).getGeoLocation().get(0), geoLocationAlls.get(0).getGeoLocation().get(1), "Animals Location", date);

        });

        int finalIncreaseBy = increaseBy;
        buttonLocationHistory.setOnClickListener(view -> {
            mapboxMap.removeAnnotations();
            for (int i = 0; i < geoLocationAlls.size(); i = i + finalIncreaseBy) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTimeInMillis(Long.parseLong(String.valueOf(geoLocationAlls.get(i).getTimestramp())));
                String date1 = DateFormat.format("dd-MMM hh:mm:ss", calendar1).toString();
                setMarker(geoLocationAlls.get(i).getGeoLocation().get(0), geoLocationAlls.get(i).getGeoLocation().get(1), "Animals Location", date1);
            }
        });

        // setMarker(27.679301, 85.258709, "Location", "Snippet");
    }

    private void loading() {
        progressBar.setVisibility(View.VISIBLE);
        mapView.setVisibility(View.GONE);
    }

    private void loaded() {
        progressBar.setVisibility(View.GONE);
        mapView.setVisibility(View.VISIBLE);
    }
}
package kevinstar1.edu.cn.mapbox;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;

import java.util.List;

import kevinstar1.edu.cn.mapbox.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements LocationEngineListener ,PermissionsListener {

    ActivityMainBinding mActivityMainBinding;
    private static final String TAG = "MainActivity";
    PermissionsManager mPermissionsManager;

    private MapView mMapView;
    private MapboxMap mMapboxMap;
    private LocationEngine mLocationEngine;
    private LocationLayerPlugin mLocationPlugin;
    private Button mBtnLocation;
    private Boolean mIsLoadSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,"pk.eyJ1Ijoia2V2aW4tc3RhciIsImEiOiJjamZ0bnZudDUwZHdzMndudnRnMHU4bmVtIn0.oVWAird9kWcs48X9zzlXWw");
        mActivityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        mMapView = mActivityMainBinding.mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap  = mapboxMap;

                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Icon icon = iconFactory.fromResource(R.mipmap.ic_launcher);
                mMapboxMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(48.85819,2.29458))
                                .icon(icon)
                                .title("Hello World")
                );
                mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        Toast.makeText(MainActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

                        //更行标记的位置
                        LatLng latLng = new LatLng(-37.822884, 144.981916);
                        // Change the marker location
                        marker.setPosition(latLng);
                        setCameraPosition(latLng);
                        return true;
                    }
                });
                enableLocationPlugin();
                mIsLoadSuccess = true;
            }
        });
        mBtnLocation = mActivityMainBinding.btnLocation;
        mBtnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLoadSuccess){
                    Log.d(TAG, "onClick: 定位开始");
                    Toast.makeText(MainActivity.this, "尽情期待", Toast.LENGTH_SHORT).show();
                    enableLocationPlugin();
                }else {
                    Toast.makeText(MainActivity.this, "地图尚未加载完成,请等待...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void enableLocationPlugin() {
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            //创建定位引擎实例
            initializeLocationEngine();

            //加载定位插件
            mLocationPlugin = new LocationLayerPlugin(this.mMapView,this.mMapboxMap,mLocationEngine);
            mLocationPlugin.setLocationLayerEnabled(true);
        }else{
            mPermissionsManager = new PermissionsManager(this);
            mPermissionsManager.requestLocationPermissions(this);
        }
    }
    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {

        //设置监听需要放在 引擎激活之前
        mLocationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        mLocationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        //设置位置监听
        mLocationEngine.addLocationEngineListener(this);
        mLocationEngine.activate();


        Location location = mLocationEngine.getLastLocation();
        if (location!=null){
            setCameraPosition(location);
        }else{

        }

    }

    private void setCameraPosition(Location location) {
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(),location.getLongitude()),16));

    }
    private void setCameraPosition(LatLng latLng){
        mMapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (mLocationPlugin!=null){
            mLocationPlugin.onStart();
        }
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationPlugin!=null){
            mLocationPlugin.onStop();
        }
        if (mLocationEngine!=null){
            mLocationEngine.removeLocationUpdates();
        }
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mLocationEngine!=null){
            mLocationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        Log.d(TAG, "onConnected: 引擎链接成功");
        mLocationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null){
            setCameraPosition(location);
            Log.d("TAG", "onLocationChanged: lat = "+location.getLatitude()+" long = "+location.getLongitude());
          /*  mMapboxMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(location.getLatitude(), location.getLongitude()))
                            .title("Hello World")
            );*/
            mLocationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            enableLocationPlugin();
        }else{
            Toast.makeText(this, "无定位访问权限", Toast.LENGTH_SHORT).show();
        }
    }
}

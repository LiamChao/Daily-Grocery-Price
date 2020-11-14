package tw.tcnr109a05.grocery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class F10400 extends F000 implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = "tcnr06=>";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    static LatLng VGPS = new LatLng(24.172127, 120.610313);
    private static String[][] locations = {
            {"建國市場", "24.1421026,120.6702602"},
            {"第一市場", "24.1421029,120.6768263"},
            {"第二市場", "24.1421078,120.6768263"},
            {"第三市場", "24.1384331,120.6632556"},
            {"第五市場", "24.1384334,120.6698217"},
            {"中央市場", "24.1503359,120.6723398"},
            {"篤行市場", "24.1514123,120.6714387"},
            {"中義市場", "24.1716797,120.660304"},
            {"何厝市場", "24.1624296,120.651538"}
    };
    private static String[] mapType = {
            "街道圖",
            "衛星圖",
            "地形圖",
            "混合圖",
            "開啟路況",
            "關閉路況"};
    float mapzoom = 14.0f;
    private List<String> permissionsList = new ArrayList<String>();
    private LocationManager locationManager;
    private GoogleMap map;
    private Spinner mSpnLocation;
    private Spinner mSpnMapType;
    private int icosel = 1;
    private double dLat, dLon;
    private BitmapDescriptor image_des;
    private TextView txtOutput;
    private String provider;
    private long minTime = 5000;
    private float minDist = 5.0f;
    private Marker markerMe;
    private float currentZoom = 12.0f;
    private int resID;
    private ArrayList<LatLng> mytrace;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f10400);
        checkRequiredPermission(this);
        //------------設定MapFragment-----------------------------------
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //-------------------------------------------------------
        u_checkgps();
        setupViewComponent();
        // Bottom Navigation JAVA 開始
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Market);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem mrnuitem) {
                switch (mrnuitem.getItemId()) {
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), F10100.class));
                        overridePendingTransition(0, 0);
                        F10400.this.finish();
                        return true;
                    case R.id.Price:
                        startActivity(new Intent(getApplicationContext(), F10200.class));
                        overridePendingTransition(0, 0);
                        F10400.this.finish();
                        return true;
                    case R.id.Food:
                        startActivity(new Intent(getApplicationContext(), F10300.class));
                        overridePendingTransition(0, 0);
                        F10400.this.finish();
                        return true;
                    case R.id.Market:
                        return true;

                    case R.id.Book:
                        startActivity(new Intent(getApplicationContext(), F10500.class));
                        overridePendingTransition(0, 0);
                        F10400.this.finish();
                        return true;
                }
                return false;
            }
        });
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        // Bottom Navigation JAVA 結束
    }


    private void setupViewComponent() {
        mSpnLocation = (Spinner) this.findViewById(R.id.spnLocation);
        mSpnMapType = (Spinner) this.findViewById(R.id.spnMapType);
        // ------------------------------
        // Parameters: 對應的三個常量值：VISIBLE=0 INVISIBLE=4 GONE=8
        // ---------------
        icosel = 0;  //設定圖示初始值
        // -----------------------------
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        for (int i = 0; i < locations.length; i++) adapter.add(locations[i][0]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnLocation.setAdapter(adapter);
        // 指定事件處理物件
        mSpnLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                map.clear();
                mytrace = null;     //清除軌跡圖
                showloc();
                setMapLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // ---------------
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (int i = 0; i < mapType.length; i++)
            adapter.add(mapType[i]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnMapType.setAdapter(adapter);
        //-----------設定ARGB 透明度----
//        mSpnMapType.setPopupBackgroundDdrawableable(new ColorDdrawableable(0x00FFFFFF)); //全透明
        mSpnMapType.setPopupBackgroundDrawable(new ColorDrawable(0x80FFFFFF)); //50%透明
        mSpnLocation.setPopupBackgroundDrawable(new ColorDrawable(0x80FFFFFF)); //50%透明
//        # ARGB依次代表透明度（alpha）、紅色(red)、綠色(green)、藍色(blue)
//        100% — FF       95% — F2        90% — E6        85% — D9
//        80% — CC        75% — BF        70% — B3        65% — A6
//        60% — 99        55% — 8C        50% — 80        45% — 73
//        40% — 66        35% — 59        30% — 4D        25% — 40
//        20% — 33        15% — 26        10% — 1A         5% — 0D         0% — 00
        //---------------
        mSpnMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);// "街道圖",
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);// "衛星圖",
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);// "地形圖",
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);// "混合圖",
                        break;
                    case 4:
                        map.setTrafficEnabled(true);//  "開啟路況",
                        break;
                    case 5:
                        map.setTrafficEnabled(false);//   "關閉路況"
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setMapLocation() {
        showloc();
        int iSelect = mSpnLocation.getSelectedItemPosition();
        String[] sLocation = locations[iSelect][1].split(",");
        double dLat = Double.parseDouble(sLocation[0]);    // 南北緯
        double dLon = Double.parseDouble(sLocation[1]);    // 東西經
        String vtitle = locations[iSelect][0];
        //--- 設定所選位置之當地圖示 ---//
        image_des = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN); //使用系統水滴
        VGPS = new LatLng(dLat, dLon);
        map.addMarker(new MarkerOptions()
                .position(VGPS)
                .title(vtitle)
                .snippet("座標:" + dLat + "," + dLon)
                .infoWindowAnchor(0.5f, 0.9f)
                .icon(image_des));// 顯示圖標文字
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, currentZoom));
    }

    private void showloc() {
        for (int i = 0; i < locations.length; i++) {
            String[] slocation = locations[i][1].split(",");
            dLat = Double.parseDouble(slocation[0]);
            dLon = Double.parseDouble(slocation[1]);
            String vtitle = locations[i][0];

            switch (icosel) {
                case 0:
                    image_des = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                    break;
                case 1:
                    if (i >= 0 && i < 8) {
                        String idName = "t" + String.format("%02d", i);
                        resID = getResources().getIdentifier(idName, "drawable",
                                getPackageName());
                    } else {
                        resID = getResources().getIdentifier("t99", "drawable",
                                getPackageName());
                    }
                    image_des = BitmapDescriptorFactory.fromResource(resID);// 使用照片
                    break;
            }
            VGPS = new LatLng(dLat, dLon);
            map.addMarker(new MarkerOptions()
                    .position(VGPS)
                    .title(vtitle)
                    .snippet("座標:" + dLat + "," + dLon)
                    .infoWindowAnchor(0.5f, 0.9f)
                    .icon(image_des));
        }
    }

    private void u_checkgps() {
        // 取得系統服務的LocationManager物件
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 檢查是否有啟用GPS
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 顯示對話方塊啟用GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS目前狀態是尚未啟用.\n"
                            + "請問你是否現在就設定啟用GPS?")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 使用Intent物件啟動設定程式來更改GPS設定
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    }).setNegativeButton("不啟用", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void checkRequiredPermission(final Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), permissions[i] + "權限申請成功!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "權限被拒絕： " + permissions[i], Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        mUiSettings = map.getUiSettings();//
//        開啟 Google Map 拖曳功能
        map.getUiSettings().setScrollGesturesEnabled(true);
//        右下角的導覽及開啟 Google Map功能
        map.getUiSettings().setMapToolbarEnabled(true);
//        左上角顯示指北針，要兩指旋轉才會出現
        map.getUiSettings().setCompassEnabled(true);
//        右下角顯示縮放按鈕的放大縮小功能
        map.getUiSettings().setZoomControlsEnabled(true);
        // --------------------------------
        map.addMarker(new MarkerOptions().position(VGPS).title("中區職訓"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS, mapzoom));
        //----------取得定位許可------------API 20-----------
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //----顯示我的位置ICO-------
            Toast.makeText(getApplicationContext(), "GPS定位權限未允許", Toast.LENGTH_LONG).show();
        } else {
            //----顯示我的位置ICO-------
            map.setMyLocationEnabled(true);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private boolean initLocationProvider() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }
        return false;
    }

    private void nowAddress() {
// 取得上次已知的位置
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);
            return;
        }
// 監聽 GPS Listener----------------------------------
// long minTime = 5000;// ms
// float minDist = 5.0f;// meter
//---網路和GPS來取得定位，因為GPS精準度比網路來的更好，所以先使用網路定位、
// 後續再用GPS定位，如果兩者皆無開啟，則跳無法定位的錯誤訊息
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location = null;
    }

    private void updateWithNewLocation(Location location) {
        String where = "";
        if (location != null) {
            double lng = location.getLongitude();// 經度
            double lat = location.getLatitude();// 緯度
            float speed = location.getSpeed();// 速度
            long time = location.getTime();// 時間
            String timeString = getTimeString(time);
            where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed + "\n時間: " + timeString + "\nProvider: "
                    + provider;
            // 標記"我的位置"
            showMarkerMe(lat, lng);
            cameraFocusOnMe(lat, lng);
            //--------------------
            trackMe(lat, lng);      //軌跡圖
            //--------------------
        } else {
            where = "*位置訊號消失*";
        }
        // 位置改變顯示
        txtOutput.setText(where);
    }

    private void trackMe(double lat, double lng) {
        if (mytrace == null) {
            mytrace = new ArrayList<LatLng>();
        }
        mytrace.add(new LatLng(lat, lng));
        PolylineOptions polylineOpt = new PolylineOptions();
        for (LatLng latlng : mytrace) {
            polylineOpt.add(latlng);
        }
        polylineOpt.color(Color.BLUE); // 軌跡顏色
        Polyline line = map.addPolyline(polylineOpt);
        line.setWidth(15); // 軌跡寬度
//---------------------------------
        line.setPoints(mytrace);
//    ----虛線-----
//    private void trackMe(double lat, double lng) {
//        if (mytrace == null) {
//            mytrace = new ArrayList<LatLng>();
//        }
//        mytrace.add(new LatLng(lat, lng));
//        PolylineOptions polylineOpt = new PolylineOptions()
//                .geodesic(true)
//                .color(Color.CYAN)
//                .width(10)
//                .pattern(PATTERN_POLYGON_ALPHA);
//
////        polylineOpt.addAll(Polyline.getPoints(mytrace));
////        polylinePaths.add(mGoogleMap.addPolyline(polylineOpt));
//
////        for (LatLng latlng : mytrace) {
////            polylineOpt.add(latlng);
////        }
//        // -----***軌跡顏色***-----
//        polylineOpt.color(Color.rgb(188 ,143,143));
//        Polyline line = map.addPolyline(polylineOpt);
//        line.setWidth(10); // 軌跡寬度
//        line.equals(10);
//        line.setPoints(mytrace);
//
//    }
    }

    private String getTimeString(long timeInMilliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timeInMilliseconds);
    }

    private void showMarkerMe(double lat, double lng) {
        if (markerMe != null) markerMe.remove();
//------------------
        int resID = getResources().getIdentifier("t00", "drawable", getPackageName());
//-------------------------
        dLat = lat; // 南北緯
        dLon = lng; // 東西經
        String vtitle = "GPS位置:";
        String vsnippet = "座標:" + String.valueOf(dLat) + "," + String.valueOf(dLon);
        VGPS = new LatLng(lat, lng);// 更新成欲顯示的地圖座標
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(lat, lng));
        markerOpt.title(vtitle);
        markerOpt.snippet(vsnippet);
        markerOpt.infoWindowAnchor(0.5f, 0.9f);
        markerOpt.draggable(true);
        if (icosel == 0) {
            markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else {
            image_des = BitmapDescriptorFactory.fromResource(resID);// 使用照片
            markerOpt.icon(image_des);
        }
//        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        markerMe = map.addMarker(markerOpt);
//----------------------------
        locations[0][1] = lat + "," + lng;
    }

    private void cameraFocusOnMe(double lat, double lng) {
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(map.getCameraPosition().zoom)
                .build();
        /* 移動地圖鏡頭 */
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getApplicationContext(), "返回GPS目前位置", Toast.LENGTH_LONG).show();
        return true;
    }

    // ---Control 控制項設定--------------------------------
    private boolean isChecked(int id) {
        return ((CheckBox) findViewById(id)).isChecked();
    }

    // -------- 地圖縮放 -------------------------------------------
    public void setZoomButtonsEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setZoomControlsEnabled(((CheckBox) v).isChecked());
    }

    // ---------------設定指北針----------------------------------------------
    public void setCompassEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setCompassEnabled(((CheckBox) v).isChecked());
    }

    // -----顯示 我的位置座標圖示
    public void setMyLocationLayerEnabled(View v) {
        if (!checkReady()) return;

        //----------取得定位許可-----------------------
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //----顯示我的位置ICO-------
            map.setMyLocationEnabled(((CheckBox) v).isChecked());
        } else {
            Toast.makeText(getApplicationContext(), "GPS定位權限未允許", Toast.LENGTH_LONG).show();
        }
    }

    // ---- 可用捲動手勢操控,用手指平移或捲動來拖曳地圖
    public void setScrollGesturesEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setScrollGesturesEnabled(((CheckBox) v).isChecked());
    }

    // ---- 縮放手勢 按兩下 按一下 或兩指拉大拉小----
    public void setZoomGesturesEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setZoomGesturesEnabled(((CheckBox) v).isChecked());
    }

    // ---- 傾斜手勢 改變地圖的傾斜角度 兩指上下拖曳來增加/減少傾斜角度----
    public void setTiltGesturesEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setTiltGesturesEnabled(((CheckBox) v).isChecked());
    }

    // ---- 旋轉手勢 兩指旋轉地圖  ----
    public void setRotateGesturesEnabled(View v) {
        if (!checkReady()) return;
        map.getUiSettings().setRotateGesturesEnabled(((CheckBox) v).isChecked());
    }

    private boolean checkReady() {
        if (map == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

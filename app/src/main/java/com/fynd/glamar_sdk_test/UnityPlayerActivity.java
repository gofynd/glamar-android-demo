package com.fynd.glamar_sdk_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.fynd.glamar_sdk_test.GlamAREssential.GlamConfigModel;
import com.fynd.glamar_sdk_test.GlamAREssential.SKU;
import com.fynd.glamarcommunication.ErrorMessageType;
import com.fynd.glamarcommunication.GlamARCommunication;
import com.fynd.glamarcommunication.IGlamARCommunication;
import com.fynd.glamarcommunication.MessageType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.unity3d.player.IUnityPlayerLifecycleEvents;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnityPlayerActivity extends AppCompatActivity implements IUnityPlayerLifecycleEvents, IGlamARCommunication {



    protected String currentColorVal, currentCategoryName;
    protected int currentIntensityVal;

    protected UnityPlayer mUnityPlayer;

    private FrameLayout frameLayoutForUnity;

    private RecyclerView recyclerView;

    private Button sampleMakeupButton;

    private RadioGroup stylingGroup;

    private SKUListAdapter adapter;

    private SeekBar colorSeekBar;

    public static Activity currentActivity = null;

    protected GlamARCommunication glamARCommunication;

    protected IGlamARCommunication message;

    protected static String TAG = "UnityPlayerActivity";

    protected  String configJSON = null;

    protected boolean isOn3d = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glamARCommunication = GlamARCommunication.getInstance();
        message = this;
        mUnityPlayer = new UnityPlayer(getApplicationContext());

        setContentView(R.layout.activity_unity_player);
        currentActivity = this;

        this.frameLayoutForUnity = (FrameLayout) findViewById(R.id.unity_frame);
        this.frameLayoutForUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer.requestFocus();
        mUnityPlayer.windowFocusChanged(true);

        glamARCommunication.InitGlamAR(getResources().getString(R.string.GLAM_API_KEY),
                "com.fynd.glamar_sdk_test.UnityPlayerActivity", this);
        glamARCommunication.setCameraActivity(this);

        InitializeUI();
        InitializingColorSeekBarEvents();
        StylingGroupEvent();
        //value = getIntent().getStringArrayExtra("sku-detail");
        //UnityPlayerCallSKURequest(value[0]);
    }

    private void InitializeUI() {
        recyclerView = (RecyclerView)findViewById(R.id.sku_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SKUListAdapter(getApplicationContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        colorSeekBar = findViewById(R.id.color_seek_bar);
        stylingGroup = findViewById(R.id.stylingGroup);
        sampleMakeupButton = findViewById(R.id.sample_makeup);

        GlamARAPIRequest();
    }

    private void GlamARAPIRequest() {
        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<List<SKU>> skuListCall = apiService.getAllSKUs(getResources().getString(R.string.GLAM_API_KEY), "application/json", "android");

        skuListCall.enqueue(new Callback<List<SKU>>() {
            @Override
            public void onResponse(Call<List<SKU>> call, Response<List<SKU>> response) {
                if (response.isSuccessful()) {

                    List<SKU> skus = response.body();
                    SKU hairSKU = new SKU();
                    hairSKU.id = 497;
                    hairSKU.name = "Hair";
                    hairSKU.category = "Hair";
                    hairSKU.pattern = new HashMap<String, HashMap<String, Object>>();
                    HashMap<String, Object> hairPropery = new HashMap<>();
                    hairPropery.put("hair1Color", "#00D3FF");
                    hairPropery.put("hair1ColorIntensity", 77);
                    hairSKU.pattern.put("pattern", hairPropery);
                    skus.add(hairSKU);
                    // Process the list of users here
                    for (SKU sku : skus) {
                        Log.d("SKU", "ID: " + sku.id + ", Name: " + sku.category);
                    }
                    adapter.setSkuList(skus);
                    adapter.setOnClickListener(new SKUListAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position, SKU model) {

                            for (Map.Entry<String, HashMap<String, Object>> prop : model.pattern.entrySet()) {
                                for (Map.Entry<String, Object> innerProp :prop.getValue().entrySet()) {
                                    Log.d("Properties", innerProp.getKey());
                                    String key = innerProp.getKey();
                                    Object val = innerProp.getValue();
                                    Object intensityVal = innerProp.getValue();

                                    if(key.endsWith("ColorIntensity") && intensityVal instanceof Double) {
                                        Log.d("Color Intensity", intensityVal.toString());
                                        currentIntensityVal = (int) Double.parseDouble(intensityVal.toString());
                                        colorSeekBar.setProgress(currentIntensityVal);
                                    }

                                    if ((key.endsWith("Color") && val instanceof String)) {
                                        startGlamAR(model.id + "", val.toString(), model.category);
                                    }


                                }
                            }
                        }
                    });
                } else {
                    Log.e("Request", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SKU>> call, Throwable t) {
                Log.e("Request", "Failed to retrieve skus: " + t.getMessage());
            }
        });

        Call<GlamConfigModel> skuConfigCall = apiService.getParticularSKU("497", getResources().getString(R.string.GLAM_API_KEY), "application/json", "android");

        skuConfigCall.enqueue(new Callback<GlamConfigModel>() {
            @Override
            public void onResponse(Call<GlamConfigModel> call, Response<GlamConfigModel> response) {
                if(response.isSuccessful()) {
                    GlamConfigModel configModel = response.body();
                    Log.d(TAG, configModel.config.name);
                    Gson gson = new Gson();
                    String configJSON = gson.toJson(configModel);

                    sampleMakeupButton.setVisibility(View.VISIBLE);
                    sampleMakeupButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, configJSON);
                            glamARCommunication.applyMakeupConfig(configJSON);
                        }
                    });

                }
                else {
                    Log.d(TAG, "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GlamConfigModel> call, Throwable t) {
                Log.e("Request", "Failed to retrieve particular sku: " + t.getMessage());
            }
        });
    }

    private void startGlamAR(String skuId, String colorVal, String categoryName) {
        this.currentColorVal = colorVal;
        this.currentCategoryName = categoryName;

        glamARCommunication.fetchParticularSKUId(skuId);

    }

    private void UnityPlayerSetColorIntensity(int currentIntensityVal) {
        Log.d("Color Intensity", currentIntensityVal + "");
    }

    private void InitializingColorSeekBarEvents() {
        colorSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentIntensityVal = seekBar.getProgress();
                glamARCommunication.setColorIntensity(currentIntensityVal+"");
            }
        });
    }

    private void StylingGroupEvent() {
        stylingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                glamARCommunication.setStyleId(radioButton.getTag().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        mUnityPlayer.destroy();
        super.onDestroy();

        mUnityPlayer = null;

    }

    @Override
    public void onUnityPlayerUnloaded() {}

    @Override
    public void onUnityPlayerQuitted() {}

    @Override
    public void onErrorMessage(MessageType type, String message) {
        Log.e(TAG, type.toString());
    }

    @Override
    public void onSuccessMessage(MessageType type, String message) {
        Log.d(TAG, type.toString());
    }

    @Override
    public void onSuccessResponseModuleLaunch(String message) {
        Log.d(TAG, message);
        glamARCommunication.launchModuleByName(currentCategoryName, currentColorVal);
    }

    @Override
    public void onFaceTrackingLost(boolean trackingLost) {
        if(trackingLost) {
            Toast.makeText(currentActivity, "Tracking Lost", Toast.LENGTH_SHORT).show();
        }
    }

}
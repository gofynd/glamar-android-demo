package com.fynd.glamar_sdk_test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.fynd.glamar_sdk_test.GlamAREssential.ErrorClass;
import com.fynd.glamar_sdk_test.GlamAREssential.GlamConfigModel;
import com.fynd.glamar_sdk_test.GlamAREssential.Pattern;
import com.fynd.glamar_sdk_test.GlamAREssential.SKU;
import com.fynd.glamar_sdk_test.GlamAREssential.SKUListModel;
import com.fynd.glamar_sdk_test.databinding.FragmentSecondBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unity3d.player.UnityPlayer;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;

public class SecondFragment extends Fragment {

    protected UnityPlayer mUnityPlayer;
    FrameLayout frameLayoutForUnity;

    private FragmentSecondBinding binding;
    String[] value;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        mUnityPlayer = new UnityPlayer(getContext());
        //View view = inflater.inflate(R.layout.fragment_second, container, false);
        this.frameLayoutForUnity = (FrameLayout) binding.getRoot().findViewById(R.id.unity_frame);
        this.frameLayoutForUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer.requestFocus();
        mUnityPlayer.windowFocusChanged(true);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        value = MainActivity.valueHolderBundle.getStringArray("SKUID");
        if(value != null) {
            UnityPlayerCallSKURequest(value[0]);
        }

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    NavHostFragment.findNavController(SecondFragment.this)
//                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                } catch (Exception e) {
                    Log.d("Transaction Error", e.getMessage());
                }

            }
        });
    }

    private void UnityPlayerCallSKURequest(String skuId) {
        //UnityPlayer.UnitySendMessage("APIRequestController", "SetPackagePath", "com.fynd.glamar_sdk_test.GlamAREssential");

        UnityPlayer.UnitySendMessage("APIRequestController", "SetAuthorizationKey", "d6dba6bc07444c3aba254bb106c97821");

        UnityPlayer.UnitySendMessage("APIRequestController", "SetActivityName", "com.fynd.glamar_sdk_test.SecondFragment");

        UnityPlayer.UnitySendMessage("APIRequestController", "FetchParticularSKUId", skuId);

        //UnityPlayer.UnitySendMessage("APIRequestController", "FetchAllSKU", "");
    }

    public void onDestroyView() {
        mUnityPlayer.unload();
        mUnityPlayer = null;
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }

    public void ReceiveResponse(Object object) {
        Log.d("SDK Response", object.toString());
        Gson gson = new Gson();
        ErrorClass error = null;
        try {
             error = gson.fromJson(object.toString(), ErrorClass.class);
        } catch (Exception e) { Log.d("Error Class", e.getMessage()); }

        if(error != null) {
            String sceneToLoad = null;
            value = MainActivity.valueHolderBundle.getStringArray("SKUID");
            switch (value[2]) {
                case "Eye Liner":
                    sceneToLoad = "eyelinertryon";
                    break;
                case "Blush":
                    sceneToLoad = "blushtryon";
                    break;
                case "Eye Shadow":
                    sceneToLoad = "eyeshadowtryon";
                    break;
                case "Lipstick":
                    sceneToLoad = "lipstryon";
                    break;
                default:
                    sceneToLoad = null;
            }
            if(sceneToLoad.isEmpty())
                return;
            UnityPlayer.UnitySendMessage("NativeCalls", "LaunchModuleByName", sceneToLoad);
            value = MainActivity.valueHolderBundle.getStringArray("SKUID");
            UnityPlayer.UnitySendMessage("NativeCalls", "ApplyColor", value[1]);
        }

    }

//    public void ReceiveResponseParticularSKU(Object object) {
//        GlamConfigModel configModel = (GlamConfigModel) object;
//        Log.d("Model Received", "Id : " + configModel.config.id);
//        String colorValue = null;
//        for (Pattern pattern : configModel.config.pattern) {
//            for (Map.Entry<String,Object> mapElement : pattern.Properties.entrySet()) {
//                String key = mapElement.getKey();
//                Object val = mapElement.getValue();
//
//                Log.d("Properties", key + ", " + val);
//
//                if (key.endsWith("Color") && val instanceof String) {
//                    colorValue = (String) val;
//                    break;
//                }
//            }
//        }
//        UnityPlayer.UnitySendMessage("NativeCalls", "LaunchModuleByName", "lipstryon");
//        UnityPlayer.UnitySendMessage("NativeCalls", "ApplyColor", colorValue);
//    }

    public void ReceiveResponseAllSKU(Object object) {
        SKUListModel listModel = (SKUListModel) object;
        Log.d("List Model Received", "Count " + listModel.list.size());

        for (SKU sku : listModel.list) {
            Log.d("Separator", "----------------------------");
            Log.d("SKU Basic Property", sku.id + "");
            Log.d("SKU Basic Property", sku.name);
            Log.d("SKU Basic Property", sku.category);
            Log.d("SKU Basic Property", sku.barcode != null ? sku.barcode: "empty-barcode");
            Log.d("SKU Basic Property", sku.imageUrl != null ? sku.imageUrl: "empty-imageUrl");
//            for (Map.Entry<String, HashMap<String, Object>> prop : sku.pattern.properties.entrySet()) {
//                Log.d("SKU Sub Property", "" + prop.getValue().values());
//            }
            Log.d("Seperator", "----------------------------");
        }
    }

}
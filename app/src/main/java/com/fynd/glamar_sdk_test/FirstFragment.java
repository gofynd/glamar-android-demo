package com.fynd.glamar_sdk_test;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fynd.glamar_sdk_test.GlamAREssential.SKU;
import com.fynd.glamar_sdk_test.databinding.FragmentFirstBinding;
import com.fynd.glamar_sdk_test.databinding.FragmentSecondBinding;
import com.unity3d.player.UnityPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private RecyclerView recyclerView;
    private SKUListAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.sku_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SKUListAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        APIInterface apiService = APIClient.getClient().create(APIInterface.class);

        Call<List<SKU>> skuListCall = apiService.getAllSKUs(getResources().getString(R.string.GLAM_API_KEY), "application/json", "android");

        skuListCall.enqueue(new Callback<List<SKU>>() {
                @Override
                public void onResponse(Call<List<SKU>> call, Response<List<SKU>> response) {
                    if (response.isSuccessful()) {

                        List<SKU> skus = response.body();
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
                                        if (key.endsWith("Color") && val instanceof String) {
                                            Intent intent = new Intent(getContext(), UnityPlayerActivity.class);
                                            intent.putExtra("sku-detail", new String[]{
                                                    "" + model.id, val.toString(), model.category
                                            });
                                            startActivity(intent);
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
                public void onFailure(Call<List<SKU>> call, Throwable t) {
                    Log.e("Request", "Failed to retrieve users: " + t.getMessage());
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
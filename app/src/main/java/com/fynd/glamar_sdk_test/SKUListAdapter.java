package com.fynd.glamar_sdk_test;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fynd.glamar_sdk_test.GlamAREssential.Pattern;
import com.fynd.glamar_sdk_test.GlamAREssential.PatternWithSubProperties;
import com.fynd.glamar_sdk_test.GlamAREssential.SKU;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SKUListAdapter extends RecyclerView.Adapter<SKUListAdapter.SKUListViewHolder> {

    private List<SKU> skuList;
    Context context;

    private OnClickListener onClickListener;

    public SKUListAdapter(Context context, List<SKU> skuList) {
        this.context = context;
        this.skuList = skuList;
    }

    @NonNull
    @Override
    public SKUListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sku, parent, false);
        return new SKUListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SKUListViewHolder holder, int position) {
        SKU sku = skuList.get(position);
        if(sku != null) {
            for (Map.Entry<String, HashMap<String, Object>> prop : sku.pattern.entrySet()) {
                for (Map.Entry<String, Object> innerProp :prop.getValue().entrySet()) {
                    Log.d("Properties", innerProp.getKey());
                    String key = innerProp.getKey();
                    Object val = innerProp.getValue();
                    if (key.endsWith("Color") && val instanceof String) {
                        holder.skuColorImage.setBackgroundColor(Color.parseColor(val.toString()));
                    }
                }
            }


            holder.skuItemName.setText(sku.name);
            holder.skuCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(position, sku);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return skuList.size();
    }

    public void setSkuList(List<SKU> list) {
        this.skuList = list;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, SKU model);
    }

    public class SKUListViewHolder extends RecyclerView.ViewHolder {
        public ImageView skuColorImage;
        public TextView skuItemName;

        public CardView skuCardView;

        public SKUListViewHolder(@NonNull View itemView) {
            super(itemView);
            skuColorImage = itemView.findViewById(R.id.sku_color);
            skuItemName = itemView.findViewById(R.id.sku_name);
            skuCardView = itemView.findViewById(R.id.sku_card);
        }
    }
}

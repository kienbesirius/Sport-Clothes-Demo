package com.example.sportclothes.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.databinding.ItemSportClothGridBinding;
import com.example.sportclothes.listener.IOnClickSCItemListener;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.GlideUtils;

import java.util.List;

public class SCGridAdapter extends RecyclerView.Adapter<SCGridAdapter.SCGViewHolder> {

    private final List<SportCloth> sportClothList;
    public final IOnClickSCItemListener iOnClickSCItemListener;

    public SCGridAdapter(List<SportCloth> mListSportCloths, IOnClickSCItemListener iOnClickSCItemListener) {
        this.sportClothList = mListSportCloths;
        this.iOnClickSCItemListener = iOnClickSCItemListener;
    }

    @NonNull
    @Override
    public SCGViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSportClothGridBinding binding = ItemSportClothGridBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SCGViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SCGViewHolder holder, int position) {
        SportCloth sportCloth = sportClothList.get(position);
        if (sportCloth == null) {
            return;
        }
        GlideUtils.loadUrl(sportCloth.getImage(), holder.binding.imgSportCloth);
        if (sportCloth.getSale() <= 0) {
            holder.binding.tvSaleOff.setVisibility(View.GONE);
            holder.binding.tvPrice.setVisibility(View.GONE);

            String strPrice = sportCloth.getPrice() + Constant.CURRENCY;
            holder.binding.tvPriceSale.setText(strPrice);
        } else {
            holder.binding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.binding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + sportCloth.getSale() + "%";
            holder.binding.tvSaleOff.setText(strSale);

            String strOldPrice = sportCloth.getPrice() + Constant.CURRENCY;
            holder.binding.tvPrice.setText(strOldPrice);
            holder.binding.tvPrice.setPaintFlags(holder.binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = sportCloth.getRealPrice() + Constant.CURRENCY;
            holder.binding.tvPriceSale.setText(strRealPrice);
        }
        holder.binding.tvSportClothName.setText(sportCloth.getName());

        holder.binding.layoutItem.setOnClickListener(v -> iOnClickSCItemListener.onClickItemSportCloth(sportCloth));
    }

    @Override
    public int getItemCount() {
        return sportClothList == null ? 0 : sportClothList.size();
    }

    public static class SCGViewHolder extends RecyclerView.ViewHolder{
        ItemSportClothGridBinding binding;
        public SCGViewHolder(ItemSportClothGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

package com.example.sportclothes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.databinding.ItemSportClothPopularBinding;
import com.example.sportclothes.listener.IOnClickSCItemListener;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.GlideUtils;

import java.util.List;

public class SCPopularAdapter extends RecyclerView.Adapter<SCPopularAdapter.SCPViewHolder> {

    private final List<SportCloth> sportClothList;
    private final IOnClickSCItemListener iOnClickSCItemListener;

    public SCPopularAdapter(List<SportCloth> sportClothList, IOnClickSCItemListener iOnClickSCItemListener) {
        this.sportClothList = sportClothList;
        this.iOnClickSCItemListener = iOnClickSCItemListener;
    }

    @NonNull
    @Override
    public SCPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSportClothPopularBinding bd = ItemSportClothPopularBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SCPViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(@NonNull SCPViewHolder holder, int position) {
        SportCloth sportCloth = sportClothList.get(position);
        if (sportCloth == null) {
            return;
        }
        GlideUtils.loadUrlBanner(sportCloth.getBanner(), holder.bd.imageSportCloth);
        if (sportCloth.getSale() <= 0) {
            holder.bd.tvSaleOff.setVisibility(View.GONE);
        } else {
            holder.bd.tvSaleOff.setVisibility(View.VISIBLE);
            String strSale = "Giảm " + sportCloth.getSale() + "%";
            holder.bd.tvSaleOff.setText(strSale);
        }
        holder.bd.layoutItem.setOnClickListener(v -> iOnClickSCItemListener.onClickItemSportCloth(sportCloth));
    }

    @Override
    public int getItemCount() {
        return sportClothList == null ? 0 : sportClothList.size();
    }

    public static class SCPViewHolder extends RecyclerView.ViewHolder{
        private final ItemSportClothPopularBinding bd;
        public SCPViewHolder(ItemSportClothPopularBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }
}

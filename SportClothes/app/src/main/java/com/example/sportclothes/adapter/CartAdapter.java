package com.example.sportclothes.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.databinding.ItemCartBinding;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.GlideUtils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<SportCloth> sportClothList;
    private final IClickListener iClickListener;

    public CartAdapter(List<SportCloth> sportClothList, IClickListener iClickListener) {
        this.sportClothList = sportClothList;
        this.iClickListener = iClickListener;
    }

    public interface IClickListener {
        void clickDelete(SportCloth sportCloth, int position);

        void updateItem(SportCloth sportCloth, int position);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding itemCartBinding = ItemCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CartViewHolder(itemCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        SportCloth sportCloth = sportClothList.get(position);
        if (sportCloth == null) {
            return;
        }
        GlideUtils.loadUrl(sportCloth.getImage(), holder.itemCartBinding.imgSportClothCart);
        holder.itemCartBinding.tvSportClothNameCart.setText(sportCloth.getName());


        String strSportClothPriceCart = sportCloth.getPrice() + Constant.CURRENCY;
        if (sportCloth.getSale() > 0) {
            strSportClothPriceCart = sportCloth.getRealPrice() + Constant.CURRENCY;
        }
        holder.itemCartBinding.tvSportClothPriceCart.setText(strSportClothPriceCart);
        holder.itemCartBinding.tvCount.setText(String.valueOf(sportCloth.getCount()));

        holder.itemCartBinding.tvSubtract.setOnClickListener(v -> {
            String strCount = holder.itemCartBinding.tvCount.getText().toString();
            int count = Integer.parseInt(strCount);
            if (count <= 1) {
                return;
            }
            int newCount = count - 1;
            holder.itemCartBinding.tvCount.setText(String.valueOf(newCount));

            int totalPrice = sportCloth.getRealPrice() * newCount;
            sportCloth.setCount(newCount);
            sportCloth.setTotalPrice(totalPrice);

            iClickListener.updateItem(sportCloth, holder.getAdapterPosition());
        });

        holder.itemCartBinding.tvAdd.setOnClickListener(v -> {
            int newCount = Integer.parseInt(holder.itemCartBinding.tvCount.getText().toString()) + 1;
            holder.itemCartBinding.tvCount.setText(String.valueOf(newCount));

            int totalPrice = sportCloth.getRealPrice() * newCount;
            sportCloth.setCount(newCount);
            sportCloth.setTotalPrice(totalPrice);

            iClickListener.updateItem(sportCloth, holder.getAdapterPosition());
        });

        holder.itemCartBinding.tvDelete.setOnClickListener(v
                -> iClickListener.clickDelete(sportCloth, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return sportClothList == null ? 0 : sportClothList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        private final ItemCartBinding itemCartBinding;
        public CartViewHolder(ItemCartBinding itemCartBinding) {
            super(itemCartBinding.getRoot());
            this.itemCartBinding = itemCartBinding;
        }
    }
}

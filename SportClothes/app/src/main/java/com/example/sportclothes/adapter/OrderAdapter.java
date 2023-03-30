package com.example.sportclothes.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.databinding.ItemOrderBinding;
import com.example.sportclothes.model.Order;
import com.example.sportclothes.util.DateTimeUtils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderViewHolder(itemOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        if (order == null) {
            return;
        }
        holder.itemOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.itemOrderBinding.tvName.setText(order.getName());
        holder.itemOrderBinding.tvPhone.setText(order.getPhone());
        holder.itemOrderBinding.tvAddress.setText(order.getAddress());
        holder.itemOrderBinding.tvMenu.setText(order.getSportCloth());
        holder.itemOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.itemOrderBinding.tvTotalAmount.setText(strAmount);

        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.itemOrderBinding.tvPayment.setText(paymentMethod);
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderBinding itemOrderBinding;
        public OrderViewHolder(@NonNull ItemOrderBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            this.itemOrderBinding = itemOrderBinding;
        }
    }
}

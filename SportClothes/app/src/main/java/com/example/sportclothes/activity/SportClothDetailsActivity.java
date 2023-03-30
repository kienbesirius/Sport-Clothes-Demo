package com.example.sportclothes.activity;


import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.sportclothes.R;
import com.example.sportclothes.adapter.MoreImageAdapter;
import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.database.SportClothDatabase;
import com.example.sportclothes.databinding.ActivitySportClothDetailsBinding;
import com.example.sportclothes.event.ReloadListCartEvent;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.GlideUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SportClothDetailsActivity extends BaseActivity {

    public ActivitySportClothDetailsBinding binding;
    private SportCloth mSC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySportClothDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataIntent();
        initToolbar();
        setDataFoodDetail();
        initListener();
    }

    private void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSC = (SportCloth) bundle.get(Constant.KEY_INTENT_CLOTH_OBJECT);
        }
    }

    private void initToolbar() {
        binding.toolbar.imgBack.setVisibility(View.VISIBLE);
        binding.toolbar.imgCart.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText(getString(R.string.sport_cloth_detail_title));

        binding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void setDataFoodDetail() {
        if (mSC == null) {
            return;
        }

        GlideUtils.loadUrlBanner(mSC.getBanner(), binding.imageSportCloth);
        if (mSC.getSale() <= 0) {
            binding.tvSaleOff.setVisibility(View.GONE);
            binding.tvPrice.setVisibility(View.GONE);

            String strPrice = mSC.getPrice() + Constant.CURRENCY;
            binding.tvPriceSale.setText(strPrice);
        } else {
            binding.tvSaleOff.setVisibility(View.VISIBLE);
            binding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + mSC.getSale() + "%";
            binding.tvSaleOff.setText(strSale);

            String strPriceOld = mSC.getPrice() + Constant.CURRENCY;
            binding.tvPrice.setText(strPriceOld);
            binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = mSC.getRealPrice() + Constant.CURRENCY;
            binding.tvPriceSale.setText(strRealPrice);
        }
        binding.tvSportClothName.setText(mSC.getName());
        binding.tvSportClothDescription.setText(mSC.getDescription());

        displayListMoreImages();

        setStatusButtonAddToCart();
    }

    private void displayListMoreImages() {
        if (mSC.getImages() == null || mSC.getImages().isEmpty()) {
            binding.tvMoreImageLabel.setVisibility(View.GONE);
            return;
        }
        binding.tvMoreImageLabel.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rcvImages.setLayoutManager(gridLayoutManager);

        MoreImageAdapter moreImageAdapter = new MoreImageAdapter(mSC.getImages());
        binding.rcvImages.setAdapter(moreImageAdapter);
    }

    private void setStatusButtonAddToCart() {
        if (isFoodInCart()) {
            binding.tvAddToCart.setBackgroundResource(R.drawable.bg_gray_shape_corner_6);
            binding.tvAddToCart.setText(getString(R.string.added_to_cart));
            binding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
            binding.toolbar.imgCart.setVisibility(View.GONE);
        } else {
            binding.tvAddToCart.setBackgroundResource(R.drawable.bg_green_shape_corner_6);
            binding.tvAddToCart.setText(getString(R.string.add_to_cart));
            binding.tvAddToCart.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.toolbar.imgCart.setVisibility(View.VISIBLE);
        }
    }

    private boolean isFoodInCart() {
        List<SportCloth> list = SportClothDatabase.getInstance(this).sportClothDAO().checkSportClothInCart(mSC.getId());
        return list != null && !list.isEmpty();
    }

    private void initListener() {
        binding.tvAddToCart.setOnClickListener(v -> onClickAddToCart());
        binding.toolbar.imgCart.setOnClickListener(v -> onClickAddToCart());
    }

    public void onClickAddToCart() {
        if (isFoodInCart()) {
            return;
        }

        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_cart, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);

        ImageView imgFoodCart = viewDialog.findViewById(R.id.img_sport_cloth_cart);
        TextView tvFoodNameCart = viewDialog.findViewById(R.id.tv_sport_cloth_name_cart);
        TextView tvFoodPriceCart = viewDialog.findViewById(R.id.tv_sport_cloth_price_cart);
        TextView tvSubtractCount = viewDialog.findViewById(R.id.tv_subtract);
        TextView tvCount = viewDialog.findViewById(R.id.tv_count);
        TextView tvAddCount = viewDialog.findViewById(R.id.tv_add);
        TextView tvCancel = viewDialog.findViewById(R.id.tv_cancel);
        TextView tvAddCart = viewDialog.findViewById(R.id.tv_add_cart);

        GlideUtils.loadUrl(mSC.getImage(), imgFoodCart);
        tvFoodNameCart.setText(mSC.getName());

        int totalPrice = mSC.getRealPrice();
        String strTotalPrice = totalPrice + Constant.CURRENCY;
        tvFoodPriceCart.setText(strTotalPrice);

        mSC.setCount(1);
        mSC.setTotalPrice(totalPrice);

        tvSubtractCount.setOnClickListener(v -> {
            int count = Integer.parseInt(tvCount.getText().toString());
            if (count <= 1) {
                return;
            }
            int newCount = Integer.parseInt(tvCount.getText().toString()) - 1;
            tvCount.setText(String.valueOf(newCount));

            int totalPrice1 = mSC.getRealPrice() * newCount;
            String strTotalPrice1 = totalPrice1 + Constant.CURRENCY;
            tvFoodPriceCart.setText(strTotalPrice1);

            mSC.setCount(newCount);
            mSC.setTotalPrice(totalPrice1);
        });

        tvAddCount.setOnClickListener(v -> {
            int newCount = Integer.parseInt(tvCount.getText().toString()) + 1;
            tvCount.setText(String.valueOf(newCount));

            int totalPrice2 = mSC.getRealPrice() * newCount;
            String strTotalPrice2 = totalPrice2 + Constant.CURRENCY;
            tvFoodPriceCart.setText(strTotalPrice2);

            mSC.setCount(newCount);
            mSC.setTotalPrice(totalPrice2);
        });

        tvCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvAddCart.setOnClickListener(v -> {
            SportClothDatabase.getInstance(SportClothDetailsActivity.this).sportClothDAO().insert(mSC);
            bottomSheetDialog.dismiss();
            setStatusButtonAddToCart();
            EventBus.getDefault().post(new ReloadListCartEvent());
        });

        bottomSheetDialog.show();
    }
}
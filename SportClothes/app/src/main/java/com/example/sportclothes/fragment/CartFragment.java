package com.example.sportclothes.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sportclothes.R;
import com.example.sportclothes.SCApplication;
import com.example.sportclothes.activity.MainActivity;
import com.example.sportclothes.adapter.CartAdapter;
import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.constant.GlobalFunction;
import com.example.sportclothes.database.SportClothDatabase;
import com.example.sportclothes.databinding.FragmentCartBinding;
import com.example.sportclothes.event.ReloadListCartEvent;
import com.example.sportclothes.model.Order;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.StringUtil;
import com.example.sportclothes.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public FragmentCartBinding binding;
    private Context context;
    private CartAdapter mCartAdapter;
    private List<SportCloth> sportClothList;
    private int mAmount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        this.context = binding.getRoot().getContext();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        displayListSportClothInCart();
        binding.tvOrderCart.setOnClickListener(v -> onClickOrderCart());

        return binding.getRoot();
    }

    private void displayListSportClothInCart() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.rcvSportClothCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        binding.rcvSportClothCart.addItemDecoration(itemDecoration);

        initDataSportClothCart();
    }

    private void initDataSportClothCart() {
        sportClothList = new ArrayList<>();
        sportClothList = SportClothDatabase.getInstance(context).sportClothDAO().getListSportClothesCart();
        if (sportClothList == null || sportClothList.isEmpty()) {
            return;
        }

        mCartAdapter = new CartAdapter(sportClothList, new CartAdapter.IClickListener() {
            @Override
            public void clickDelete(SportCloth sportCloth, int position) {
                deleteSportClothFromCart(sportCloth, position);
            }

            @Override
            public void updateItem(SportCloth sportCloth, int position) {
                SportClothDatabase.getInstance(context).sportClothDAO().update(sportCloth);
                mCartAdapter.notifyItemChanged(position);

                calculateTotalPrice();
            }
        });
        binding.rcvSportClothCart.setAdapter(mCartAdapter);

        calculateTotalPrice();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearCart() {
        if (sportClothList != null) {
            sportClothList.clear();
        }
        mCartAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        List<SportCloth> listSportClothCart = SportClothDatabase.getInstance(context).sportClothDAO().getListSportClothesCart();
        if (listSportClothCart == null || listSportClothCart.isEmpty()) {
            String strZero = 0 + Constant.CURRENCY;
            binding.tvTotalPrice.setText(strZero);
            mAmount = 0;
            return;
        }

        int totalPrice = 0;
        for (SportCloth sportCloth : listSportClothCart) {
            totalPrice = totalPrice + sportCloth.getTotalPrice();
        }

        String strTotalPrice = totalPrice + Constant.CURRENCY;
        binding.tvTotalPrice.setText(strTotalPrice);
        mAmount = totalPrice;
    }

    private void deleteSportClothFromCart(SportCloth sportCloth, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete_sport_cloth))
                .setMessage(getString(R.string.message_delete_sport_cloth))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    SportClothDatabase.getInstance(context).sportClothDAO().delete(sportCloth);
                    sportClothList.remove(position);
                    mCartAdapter.notifyItemRemoved(position);

                    calculateTotalPrice();
                })
                .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void onClickOrderCart() {
        if (getActivity() == null) {
            return;
        }

        if (sportClothList == null || sportClothList.isEmpty()) {
            return;
        }

        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_order, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        // init ui
        TextView tvSportClothsOrder = viewDialog.findViewById(R.id.tv_sport_clothes_order);
        TextView tvPriceOrder = viewDialog.findViewById(R.id.tv_price_order);
        TextView edtNameOrder = viewDialog.findViewById(R.id.edt_name_order);
        TextView edtPhoneOrder = viewDialog.findViewById(R.id.edt_phone_order);
        TextView edtAddressOrder = viewDialog.findViewById(R.id.edt_address_order);
        TextView tvCancelOrder = viewDialog.findViewById(R.id.tv_cancel_order);
        TextView tvCreateOrder = viewDialog.findViewById(R.id.tv_create_order);

        // Set data
        tvSportClothsOrder.setText(getStringListSportClothsOrder());
        tvPriceOrder.setText(binding.tvTotalPrice.getText().toString());

        // Set listener
        tvCancelOrder.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvCreateOrder.setOnClickListener(v -> {
            String strName = edtNameOrder.getText().toString().trim();
            String strPhone = edtPhoneOrder.getText().toString().trim();
            String strAddress = edtAddressOrder.getText().toString().trim();

            if (StringUtil.isEmpty(strName) || StringUtil.isEmpty(strPhone) || StringUtil.isEmpty(strAddress)) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.message_enter_infor_order));
            } else {
                long id = System.currentTimeMillis();
                Order order = new Order(id, strName, strPhone, strAddress,
                        mAmount, getStringListSportClothsOrder(), Constant.TYPE_PAYMENT_CASH);
                SCApplication.get(getActivity()).getBookingDatabaseReference()
                        .child(Utils.getDeviceId(getActivity()))
                        .child(String.valueOf(id))
                        .setValue(order, (error1, ref1) -> {
                            GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_order_success));
                            GlobalFunction.hideSoftKeyboard(getActivity());
                            bottomSheetDialog.dismiss();

                            SportClothDatabase.getInstance(context).sportClothDAO().deleteAll();
                            clearCart();
                        });
            }
        });

        bottomSheetDialog.show();
    }

    private String getStringListSportClothsOrder() {
        if (sportClothList == null || sportClothList.isEmpty()) {
            return "";
        }
        String result = "";
        for (SportCloth sportCloth : sportClothList) {
            if (StringUtil.isEmpty(result)) {
                result = "- " + sportCloth.getName() + " (" + sportCloth.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + sportCloth.getCount();
            } else {
                result = result + "\n" + "- " + sportCloth.getName() + " (" + sportCloth.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + sportCloth.getCount();
            }
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListCartEvent event) {
        displayListSportClothInCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.cart));
        }
    }
}
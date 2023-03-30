package com.example.sportclothes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.sportclothes.R;
import com.example.sportclothes.SCApplication;
import com.example.sportclothes.activity.MainActivity;
import com.example.sportclothes.activity.SportClothDetailsActivity;
import com.example.sportclothes.adapter.SCGridAdapter;
import com.example.sportclothes.adapter.SCPopularAdapter;
import com.example.sportclothes.constant.Constant;
import com.example.sportclothes.constant.GlobalFunction;
import com.example.sportclothes.databinding.FragmentHomeBinding;
import com.example.sportclothes.model.SportCloth;
import com.example.sportclothes.util.StringUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    public FragmentHomeBinding binding;
    private Context context;
    private List<SportCloth> mListSportCloth;
    private List<SportCloth> mListSportClothPopular;
    private SCPopularAdapter mSportClothPopularAdapter;
    private SCGridAdapter mSportClothGridAdapter;

    private final Handler mHandlerBanner = new Handler();
    private final Runnable mRunnableBanner = new Runnable() {
        @Override
        public void run() {
            if (mListSportClothPopular == null || mListSportClothPopular.isEmpty()) {
                return;
            }
            if (binding.viewpager2.getCurrentItem() == mListSportClothPopular.size() - 1) {
                binding.viewpager2.setCurrentItem(0);
                return;
            }
            binding.viewpager2.setCurrentItem(binding.viewpager2.getCurrentItem() + 1);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        this.context = binding.getRoot().getContext();

        getListSportClothFromFirebase("");
        initListener();

        return binding.getRoot();
    }

    private void initListener() {
        binding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    if (mListSportCloth != null) mListSportCloth.clear();
                    getListSportClothFromFirebase("");
                }
            }
        });

        binding.imgSearch.setOnClickListener(view -> searchSportCloth());

        binding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchSportCloth();
                return true;
            }
            return false;
        });
    }

    private void displayListSportClothPopular() {
        mSportClothPopularAdapter = new SCPopularAdapter(getListSportClothPopular(), this::goToSportClothDetail);
        binding.viewpager2.setAdapter(mSportClothPopularAdapter);
        binding.indicator3.setViewPager(binding.viewpager2);

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandlerBanner.removeCallbacks(mRunnableBanner);
                mHandlerBanner.postDelayed(mRunnableBanner, 3000);
            }
        });
    }

    private void displayListSportClothSuggest() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        binding.rcvSportCloth.setLayoutManager(gridLayoutManager);

        mSportClothGridAdapter = new SCGridAdapter(mListSportCloth, this::goToSportClothDetail);
        binding.rcvSportCloth.setAdapter(mSportClothGridAdapter);
    }

    private List<SportCloth> getListSportClothPopular() {
        mListSportClothPopular = new ArrayList<>();
        if (mListSportCloth == null || mListSportCloth.isEmpty()) {
            return mListSportClothPopular;
        }
        for (SportCloth sportCloth : mListSportCloth) {
            if (sportCloth.isPopular()) {
                mListSportClothPopular.add(sportCloth);
            }
        }
        return mListSportClothPopular;
    }

    private void getListSportClothFromFirebase(String key) {
        if (getActivity() == null) {
            return;
        }
        SCApplication.get(getActivity()).getSportClothesDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.layoutContent.setVisibility(View.VISIBLE);
                mListSportCloth = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SportCloth sportCloth = dataSnapshot.getValue(SportCloth.class);
                    if (sportCloth == null) {
                        return;
                    }

                    if (StringUtil.isEmpty(key)) {
                        mListSportCloth.add(0, sportCloth);
                    } else {
                        if (GlobalFunction.getTextSearch(sportCloth.getName()).toLowerCase().trim()
                                .contains(GlobalFunction.getTextSearch(key).toLowerCase().trim())) {
                            mListSportCloth.add(0, sportCloth);
                        }
                    }
                }
                displayListSportClothPopular();
                displayListSportClothSuggest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_get_date_error));
            }
        });
    }

    private void searchSportCloth() {
        String strKey = binding.edtSearchName.getText().toString().trim();
        if (mListSportCloth != null) mListSportCloth.clear();
        getListSportClothFromFirebase(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    private void goToSportClothDetail(@NonNull SportCloth sportCloth) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_CLOTH_OBJECT, sportCloth);
        GlobalFunction.startActivity(getActivity(), SportClothDetailsActivity.class, bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandlerBanner.removeCallbacks(mRunnableBanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandlerBanner.postDelayed(mRunnableBanner, 3000);
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(true, getString(R.string.home));
        }
    }
}
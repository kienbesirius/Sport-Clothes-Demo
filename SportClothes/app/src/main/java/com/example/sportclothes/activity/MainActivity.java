package com.example.sportclothes.activity;


import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sportclothes.R;
import com.example.sportclothes.adapter.MainViewPagerAdapter;
import com.example.sportclothes.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewpager2.setUserInputEnabled(false);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        binding.viewpager2.setAdapter(mainViewPagerAdapter);

        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_cart).setChecked(true);
                        break;

                    case 2:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;

                    case 3:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_contact).setChecked(true);
                        break;

                    case 4:
                        binding.bottomNavigation.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;
                }
            }
        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                binding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_cart) {
                binding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_feedback) {
                binding.viewpager2.setCurrentItem(2);
            } else if (id == R.id.nav_contact) {
                binding.viewpager2.setCurrentItem(3);
            } else if (id == R.id.nav_order) {
                binding.viewpager2.setCurrentItem(4);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        showConfirmExitApp();
    }

    private void showConfirmExitApp() {
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finish())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    public void setToolBar(boolean isHome, String title) {
        if (isHome) {
            binding.toolbar.layoutToolbar.setVisibility(View.GONE);
            return;
        }
        binding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText(title);
    }
}
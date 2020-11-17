package net.lishaoy.perpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.lishaoy.common.ui.PerBaseFragment;
import net.lishaoy.perpro.R;
import net.lishaoy.perpro.demo.ApiTestActivity;
import net.lishaoy.perpro.demo.PerBannerDemoActivity;
import net.lishaoy.perpro.demo.PerRefreshDemoActivity;
import net.lishaoy.perpro.demo.PerTopLayoutActivity;
import net.lishaoy.perpro.login.LoginActivity;

public class HomeFragment extends PerBaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_tab_top).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PerTopLayoutActivity.class));
            }
        });

        view.findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PerRefreshDemoActivity.class));
            }
        });

        view.findViewById(R.id.btn_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PerBannerDemoActivity.class));
            }
        });

        view.findViewById(R.id.btn_get_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ApiTestActivity.class));
            }
        });

        view.findViewById(R.id.btn_input_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
    }
}

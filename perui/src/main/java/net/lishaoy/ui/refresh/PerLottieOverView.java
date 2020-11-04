package net.lishaoy.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import net.lishaoy.ui.R;

public class PerLottieOverView extends PerOverView{

    private LottieAnimationView animationView;

    public PerLottieOverView(@NonNull Context context) {
        super(context);
    }

    public PerLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PerLottieOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.per_lottie_refresh_overview, this, true);
        animationView = findViewById(R.id.anim_lottie);
        animationView.setAnimation("preloader.json");
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    public void onOver() {

    }

    @Override
    public void onRefresh() {
        animationView.setSpeed(2);
        animationView.playAnimation();
    }

    @Override
    public void onRefreshFinish() {

    }
}

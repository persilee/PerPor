package net.lishaoy.ui.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.lishaoy.ui.R;

public class PerTextOverView extends PerOverView{

    private ImageView imageView;
    private TextView textView;

    public PerTextOverView(@NonNull Context context) {
        super(context);
    }

    public PerTextOverView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PerTextOverView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.per_refresh_overview, this, true);
        imageView = findViewById(R.id.iv_rotate);
        textView = findViewById(R.id.tv_text);
    }

    @Override
    protected void onScroll(int scrollY, int pullRefreshHeight) {

    }

    @Override
    protected void onVisible() {
        textView.setText("下拉刷新");
    }

    @Override
    public void onOver() {
        textView.setText("松开刷新");
    }

    @Override
    public void onRefresh() {
        textView.setText("正在刷新...");
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        imageView.startAnimation(animation);
    }

    @Override
    public void onRefreshFinish() {
        imageView.clearAnimation();
    }
}

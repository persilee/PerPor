package net.lishaoy.ui.banner;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class PerBannerAdapter extends PagerAdapter {

    private Context context;
    private SparseArray<PerBannerViewHolder> holderSparseArray = new SparseArray<>();
    private IPerBanner.OnBannerClickListener listener;
    private IBindAdapter bindAdapter;
    private List<? extends PerBannerMo> bannerMos;
    private boolean autoPlay = true;
    private boolean loop = false;
    private int layoutResId = -1;

    public PerBannerAdapter(Context context) {
        this.context = context;
    }

    public void setBannerMos(List<? extends PerBannerMo> bannerMos) {
        this.bannerMos = bannerMos;
        init();
        notifyDataSetChanged();
    }

    public void setBindAdapter(IBindAdapter bindAdapter) {
        this.bindAdapter = bindAdapter;
    }

    public void setListener(IPerBanner.OnBannerClickListener listener) {
        this.listener = listener;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return autoPlay ? Integer.MAX_VALUE : (loop ? Integer.MAX_VALUE : getRealCount());
    }

    public int getRealCount() {
        return bannerMos == null ? 0 : bannerMos.size();
    }

    public int getFirstItem() {
        return Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % getRealCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position;
        if (getRealCount() > 0) realPosition = position % getRealCount();
        PerBannerViewHolder viewHolder = holderSparseArray.get(realPosition);
        if (container.equals(viewHolder.rootView.getParent())) {
            container.removeView(viewHolder.rootView);
        }
        onBind(viewHolder, bannerMos.get(realPosition), realPosition);
        if (viewHolder.rootView.getParent() != null) {
            ((ViewGroup) viewHolder.rootView.getParent()).removeView(viewHolder.rootView);
        }
        container.addView(viewHolder.rootView);
        return viewHolder.rootView;
    }

    protected void onBind(@NonNull final PerBannerViewHolder viewHolder, @NonNull final PerBannerMo bannerMo, final int position) {
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onBannerClick(viewHolder, bannerMo, position);
                }
            }
        });
        if (bindAdapter != null) {
            bindAdapter.onBind(viewHolder, bannerMo, position);
        }
    }

    private void init() {
        holderSparseArray = new SparseArray<>();
        for (int i = 0; i < bannerMos.size(); i++) {
            PerBannerViewHolder viewHolder = new PerBannerViewHolder(createView(LayoutInflater.from(context), null));
            holderSparseArray.put(i, viewHolder);
        }
    }

    private View createView(LayoutInflater inflater, ViewGroup parent) {
        if (layoutResId == -1) throw new IllegalArgumentException("please set layoutResId first");
        return inflater.inflate(layoutResId, parent, false);
    }

    public static class PerBannerViewHolder {

        private SparseArray<View> array;

        public SparseArray<View> getArray() {
            return array;
        }

        View rootView;

        public PerBannerViewHolder(View rootView) {
            this.rootView = rootView;
        }

        public <V extends View> V findViewById(int id) {
            if (!(rootView instanceof ViewGroup)) return (V) rootView;
            if (array == null) array = new SparseArray<>(1);
            V childView = (V) array.get(id);
            if (childView == null) {
                childView = rootView.findViewById(id);
                array.put(id, childView);
            }
            return childView;
        }
    }
}

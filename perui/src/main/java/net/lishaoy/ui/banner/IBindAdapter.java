package net.lishaoy.ui.banner;

public interface IBindAdapter<M extends PerBannerMo> {

     void onBind(PerBannerAdapter.PerBannerViewHolder viewHolder, M bannerMo, int position);

}

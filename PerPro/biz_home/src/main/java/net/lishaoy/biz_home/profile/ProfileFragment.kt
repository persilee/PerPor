package net.lishaoy.biz_home.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_profile.*
import net.lishaoy.biz_home.R
import net.lishaoy.biz_home.api.AccountApi
import net.lishaoy.common.http.ApiFactory
import net.lishaoy.common.route.PerRoute
import net.lishaoy.common.ui.PerBaseFragment
import net.lishaoy.common.view.loadCircle
import net.lishaoy.common.view.loadCorner
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.PerDisplayUtil
import net.lishaoy.service_login.CourseNotice
import net.lishaoy.service_login.LoginServiceProvider
import net.lishaoy.service_login.UserProfile
import net.lishaoy.ui.banner.IPerBanner
import net.lishaoy.ui.banner.PerBannerAdapter
import net.lishaoy.ui.banner.PerBannerMo

class ProfileFragment : PerBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_profile
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notify_title.setText(R.string.item_notify)
        collection.setText(R.string.item_collection)
        address.setText(R.string.item_address)
        history.setText(R.string.item_history)
        playground.setText(R.string.item_playground)

        notice_container.setOnClickListener {
            PerRoute.startActivity(context,destination = PerRoute.Destination.NOTICE_LIST)
        }

        queryLoginUserData()
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : PerCallback<CourseNotice> {
                override fun onSuccess(response: PerResponse<CourseNotice>) {
                    if (response.code == PerResponse.SUCCESS && response.data != null && response.data!!.total > 0) {
                        notify_count.text = response.data!!.total.toString()
                        notify_count.visibility = View.VISIBLE
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })
    }

    private fun queryLoginUserData() {
        LoginServiceProvider.getUserProfile(this, Observer { profile ->
            if (profile != null) {
                updateUI(profile)
            } else {
                showToast(getString(R.string.get_profile_failed))
            }
        }, onlyCache = false)
    }

    private fun updateUI(userProfile: UserProfile?) {
        user_name.text =
            if (userProfile?.isLogin == true) userProfile.userName else getString(R.string.user_name_not_login)
        login_desc.text =
            if (userProfile?.isLogin == true) getString(R.string.login_desc_login) else getString(R.string.login_desc_no_login)

        if (userProfile?.isLogin == true) {
            user_avatar.loadCircle(userProfile.userIcon)
            queryCourseNotice()
        } else {
            user_avatar.setImageResource(R.drawable.ic_avatar_default)
            user_name.setOnClickListener {
               LoginServiceProvider.login(context, Observer {
                   queryLoginUserData()
               })
            }
        }

        item_collection.text =
            userProfile?.favoriteCount?.let { spannableItem(it, getString(R.string.profile_item_collection)) }
        item_history.text =
            userProfile?.browseCount?.let { spannableItem(it, getString(R.string.profile_item_history)) }
        item_learn.text =
            userProfile?.learnMinutes?.let { spannableItem(it, getString(R.string.profile_item_learn)) }

        updateBanner(userProfile?.bannerNoticeList)

        playground.setOnClickListener {
            PerRoute.startActivity(context, destination = PerRoute.Destination.PLAYGROUND_MAIN)
        }
    }

    private fun updateBanner(bannerNoticeList: List<net.lishaoy.service_login.Notice>?) {
        if (bannerNoticeList == null || bannerNoticeList.isEmpty()) return
        var bannerList = mutableListOf<PerBannerMo>()
        bannerNoticeList.forEach{
            var bannerMo = object : PerBannerMo() {}
            bannerMo.url = it.cover
            bannerList.add(bannerMo)
        }
        profile_banner.setBannerData(R.layout.layout_profile_banner_item, bannerList)
        profile_banner.setBindAdapter { viewHolder, bannerMo, position ->
            val image = viewHolder.findViewById<ImageView>(R.id.profile_banner_image)
            image.loadCorner(bannerMo.url, PerDisplayUtil.dp2px(10f, resources))
        }
        profile_banner.setOnBannerClickListener(object : IPerBanner.OnBannerClickListener<PerBannerMo>{
            override fun <M : Any?> onBannerClick(
                viewHolder: PerBannerAdapter.PerBannerViewHolder,
                bannerMo: M,
                position: Int
            ) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(bannerNoticeList[position].url))
                startActivity(intent)
            }

        })
        profile_banner.visibility = View.VISIBLE
    }

    private fun spannableItem(count: Int, name: String): CharSequence? {
        val topText = count.toString()
        var ssb = SpannableStringBuilder()
        var ssTop = SpannableString(topText)

        ssTop.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.color_000)),
            0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssTop.setSpan(
            AbsoluteSizeSpan(18, true),
            0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssTop.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            ssTop.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        ssb.append(ssTop)
        ssb.append(name)

        return ssb
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {
            queryLoginUserData()
        }
    }
}
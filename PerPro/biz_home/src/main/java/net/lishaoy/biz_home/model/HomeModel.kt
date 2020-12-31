package net.lishaoy.biz_home.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.lishaoy.pub_mod.model.GoodsModel
import java.io.Serializable

data class HomeModel(
    val bannerList: List<HomeBanner>?,
    val subcategoryList: List<Subcategory>?,
    val goodsList: List<GoodsModel>?
) : Serializable

/**
{
"categoryId": "1",
"categoryName": "热门",
"goodsCount": "1"
}
 */
data class TabCategory(
    val categoryId: String,
    val categoryName: String,
    val goodsCount: String
) : Serializable


/**
 * {
"id": "9",
"sticky": 1,
"type": "recommend",
"title": "好课推荐",
"subtitle": "移动端普通工程师到架构师的全方位蜕变",
"url": "http://class.imooc.com/sale/mobilearchitect",
"cover": "https://img2.mukewang.com/5f9b760f09ce4cd200000000.png",
"createTime": "2020-11-04 13:54:23"
}
 */

data class HomeBanner(
    val cover: String,
    val createTime: String,
    val id: String,
    val sticky: Int,
    val subtitle: String,
    val title: String,
    val type: String,
    val url: String
) : Serializable {
    companion object {
        const val TYPE_GOODS = "goods"
        const val TYPE_RECOMMEND = "recommend"
    }
}

/**
 * {
"subcategoryId": "1",
"groupName": null,
"categoryId": "1",
"subcategoryName": "限时秒杀",
"subcategoryIcon": "https://o.devio.org/images/as/images/2018-05-16/26c916947489c6b2ddd188ecdb54fd8d.png",
"showType": "1"
}
 */

data class Subcategory(
    val categoryId: String,
    val groupName: String,
    val showType: String,
    val subcategoryIcon: String,
    val subcategoryId: String,
    val subcategoryName: String
) : Serializable


@Parcelize
data class GoodsList(
    val total: Int,
    val list: List<GoodsModel>
): Serializable, Parcelable

data class Favorite(
    val goodsId: String,
    val isFavorite: Boolean
)

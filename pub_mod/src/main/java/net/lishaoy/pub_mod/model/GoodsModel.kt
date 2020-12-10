package net.lishaoy.pub_mod.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * {
"goodsId": "1574942461590",
"categoryId": "3",
"hot": true,
"sliderImages": [
{
"url": "https://o.devio.org/images/as/goods/images/2019-03-17/2eee85e6-3e96-4bff-bd45-b1f22a2bc571.jpg",
"type": 1
},
{
"url": "https://o.devio.org/images/as/goods/images/2019-03-09/df9caeca-26db-46a7-82b5-6c42093934ca.jpg",
"type": 1
},
{
"url": "https://o.devio.org/images/as/goods/images/2019-01-01/0e4eaf44-5aae-4142-b320-93dd29dc3ec5.jpeg",
"type": 1
}
],
"marketPrice": "¥159",
"groupPrice": "39",
"completedNumText": "已拼3.6万件",
"goodsName": "小包包女2019春夏新款百搭仙女斜挎包韩版手提单肩ins超火小方包",
"tags": "退货包运费 极速退款 全场包邮 7天无理由退货",
"joinedAvatars": null,
"createTime": "2019-11-28 20:01:01",
"sliderImage": "https://o.devio.org/images/as/goods/images/2019-03-17/2eee85e6-3e96-4bff-bd45-b1f22a2bc571.jpg"
}
 */
@Parcelize
@Keep
data class GoodsModel(
    val categoryId: String?,
    val completedNumText: String?,
    val createTime: String?,
    val goodsId: String?,
    val goodsName: String?,
    val groupPrice: String?,
    val hot: Boolean?,
    val joinedAvatars: List<SliderImage>?,
    val marketPrice: String?,
    val sliderImage: String?,
    val sliderImages: List<SliderImage>?,
    val tags: String?
) : Serializable, Parcelable

@Parcelize
@Keep
data class SliderImage(
    val type: Int,
    val url: String
) : Serializable, Parcelable

fun selectPrice(groupPrice: String?, marketPrice: String?): String? {
    var price: String? = marketPrice ?: groupPrice
    if (price?.startsWith("¥") != true){
        price = "¥".plus(price)
    }
    return price
}
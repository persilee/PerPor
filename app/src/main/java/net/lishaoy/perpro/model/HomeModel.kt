package net.lishaoy.perpro.model

data class HomeModel(
    val bannerList: List<HomeBanner>?,
    val subcategoryList: List<Subcategory>?,
    val goodsList: List<GoodsModel>?
)

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
)


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
)

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
    val groupName: Any,
    val showType: String,
    val subcategoryIcon: String,
    val subcategoryId: String,
    val subcategoryName: String
)


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
data class GoodsModel(
    val categoryId: String,
    val completedNumText: String,
    val createTime: String,
    val goodsId: String,
    val goodsName: String,
    val groupPrice: String,
    val hot: Boolean,
    val joinedAvatars: Any,
    val marketPrice: String,
    val sliderImage: String,
    val sliderImages: List<SliderImage>,
    val tags: String
)

data class SliderImage(
    val type: Int,
    val url: String
)
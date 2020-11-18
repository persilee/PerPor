package net.lishaoy.perpro.model


/**
 * "isLogin": false,
 * "favoriteCount": 0,
 * "browseCount": 0,
 * "bannerNoticeList": []
 */
data class UserProfile(
    val isLogin: Boolean,
    val favoriteCount: Int,
    val browseCount: Int,
    val learnMinutes: Int,
    val userName: String,
    val userIcon: String,
    val bannerNoticeList: List<Notice>
)


/**
 * {
 * "id": "2",
 * "sticky": 1,
 * "type": "recommend",
 * "title": "好课推荐",
 * "subtitle": "Flutter从入门到进阶-实战携程网App",
 * "url": "https://coding.imooc.com/class/321.html",
 * "cover": "https://o.devio.org/images/o_as/other/flutter_321.jpg",
 * "createTime": "2020-06-11 23:48:38"
 * }
 */
data class Notice(
    val id: String,
    val sticky: Int,
    val type: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val cover: String,
    val createTime: String
)

data class CourseNotice(
    val total: Int,
    val list: List<Notice>?
)


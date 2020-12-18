package net.lishaoy.biz_search

import net.lishaoy.pub_mod.model.GoodsModel

data class SearchModel(
    val list: List<KeyWord>,
    val total: Int
)

data class KeyWord(
    val id: String?,
    val keyWord: String
)

data class GoodsSearchList(
    val total: Int,
    val list: List<GoodsModel>
)
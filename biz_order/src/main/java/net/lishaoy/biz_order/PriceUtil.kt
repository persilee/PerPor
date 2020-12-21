package net.lishaoy.biz_order

import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat

internal object PriceUtil {

    private fun subPrice(goodsPrice: String?): String? {
        if (goodsPrice != null) {
            if (goodsPrice.startsWith("¥") && goodsPrice.length > 1) {
                //次数应当是0 ，substring 前闭后开
                return goodsPrice.substring(1, goodsPrice.length)
            }
            return goodsPrice
        }

        return null
    }

    fun calculate(goodsPrice: String?, amount: Int): String {
        val price = subPrice(goodsPrice)
        if (TextUtils.isEmpty(price)) return ""

        //在做金额的加减乘除的时候 不能够直接使用基本数据类型 +-x/
        val bigDecimal = BigDecimal(price)
        val multiply = bigDecimal.multiply(BigDecimal(amount))
        //金额数值的格式化
        val df = DecimalFormat("###,###.##")
        return df.format(multiply)
    }

}
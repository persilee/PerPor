package net.lishaoy.common.info

import net.lishaoy.library.util.SPUtil

class PerLocalConfig : LocalConfig {

    companion object {
        @get:Synchronized
        var instance: PerLocalConfig? = null
            get() {
                if (field == null) {
                    field = PerLocalConfig()
                }
                return field
            }
            private set
    }

    override fun authToken(): String {
        return "MTU5Mjg1MDg3NDcwNw11.26=="
    }

    override fun boardingPass(): String? {
        return SPUtil.getString("boarding_pass")
    }
}

interface LocalConfig {

    fun authToken(): String
    fun boardingPass(): String?

}
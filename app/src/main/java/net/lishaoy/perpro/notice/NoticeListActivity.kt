package net.lishaoy.perpro.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.perpro.R

@Route(path = "/notice/list")
class NoticeListActivity : PerBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)
    }
}
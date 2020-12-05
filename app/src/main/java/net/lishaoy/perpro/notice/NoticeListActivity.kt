package net.lishaoy.perpro.notice

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_notice_list.*
import net.lishaoy.common.ui.PerBaseActivity
import net.lishaoy.library.restful.PerCallback
import net.lishaoy.library.restful.PerResponse
import net.lishaoy.library.util.PerStatusBar
import net.lishaoy.perpro.R
import net.lishaoy.perpro.http.ApiFactory
import net.lishaoy.perpro.http.api.AccountApi
import net.lishaoy.perpro.model.CourseNotice
import net.lishaoy.ui.item.PerAdapter

@Route(path = "/notice/list")
class NoticeListActivity : PerBaseActivity() {

    private lateinit var adapter: PerAdapter
    private lateinit var courseNotice: CourseNotice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PerStatusBar.setStatusBar(this, true, Color.WHITE, false)
        setContentView(R.layout.activity_notice_list)
        notice_btn_back.setOnClickListener {
            onBackPressed()
        }
        initUI()
        queryCourseNotice()
    }

    private fun initUI() {
        val linearLayoutManager = LinearLayoutManager(this)
        adapter = PerAdapter(this)
        notice_list.layoutManager = linearLayoutManager
        notice_list.adapter = adapter
    }

    private fun queryCourseNotice() {
        ApiFactory.create(AccountApi::class.java).notice()
            .enqueue(object : PerCallback<CourseNotice> {
                override fun onSuccess(response: PerResponse<CourseNotice>) {
                    if (response.successful() && response.data != null) {
                        bindData(response.data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {

                }

            })
    }

    private fun bindData(data: CourseNotice) {
        courseNotice = data
        data.list?.map {
            adapter.addItem(0, NoticeItem(it), true)
        }
    }
}
package net.lishaoy.perpro.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_per_refresh_demo.*
import net.lishaoy.perpro.R
import net.lishaoy.ui.refresh.PerRefresh
import net.lishaoy.ui.refresh.PerTextOverView

class PerRefreshDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_per_refresh_demo)
        supportActionBar?.hide()
        var header = PerTextOverView(this)
        refresh_layout.setRefreshOverView(header)
        refresh_layout.setRefreshListener(object : PerRefresh.PerRefreshListener{
            override fun enableRefresh(): Boolean {
                return true
            }

            override fun onRefresh() {
                Handler(Looper.getMainLooper()).postDelayed({
                    refresh_layout.refreshFinished()
                }, 1000)
            }

        })
        refresh_layout.setDisableRefreshScroll(false)
        var data = arrayOf("HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh", "HiRefresh")
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = MyAdapter(data)
    }

    class MyAdapter(private val data: Array<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


        class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var textView: TextView

            init {
                textView = v.findViewById(R.id.tv_title)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = data[position]
        }
    }
}
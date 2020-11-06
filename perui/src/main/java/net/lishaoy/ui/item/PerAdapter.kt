package net.lishaoy.ui.item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType

class PerAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context = context
    private var inflater: LayoutInflater? = null
    private var dataSets = ArrayList<PerDataItem<*, RecyclerView.ViewHolder>>()
    private var typeArray = SparseArray<PerDataItem<*, RecyclerView.ViewHolder>>()

    init {
        this.inflater = LayoutInflater.from(context)
    }

    fun addItem(index: Int, item: PerDataItem<*, RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }

        val notifyPos: Int = if (index > 0) index else dataSets.size - 1
        if (notify) notifyItemInserted(notifyPos)
    }

    fun addItems(items: List<PerDataItem<*, RecyclerView.ViewHolder>>, notify: Boolean) {
        val start: Int = dataSets.size
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }
    }

    fun removeItem(item: PerDataItem<*, out RecyclerView.ViewHolder>) {
        if (item != null) {
            val index = dataSets.indexOf(item)
            removeItem(index)
        }
    }

    fun refreshItem(dataItem: PerDataItem<*, *>) {
        val indexOf = dataSets.indexOf(dataItem)
        notifyItemChanged(indexOf)
    }

    fun removeItem(index: Int): PerDataItem<*, RecyclerView.ViewHolder>? {
        return if (index > 0 && index < dataSets.size) {
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            remove
        } else {
            null
        }
    }

    override fun getItemViewType(position: Int): Int {
        val dataItem = dataSets[position]
        val type = dataItem.javaClass.hashCode()
        if (typeArray.indexOfKey(type) < 0) {
            typeArray.put(type, dataItem)
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataItem = typeArray[viewType]
        var view: View? = dataItem.getItemView(parent)
        if (view == null) {
            val layoutRes = dataItem.getItemLayoutRes()
            if (layoutRes < 0) {
                RuntimeException("dataItem ${dataItem.javaClass} must override getItemView or getItemLayoutRes")
            }
            view = inflater!!.inflate(layoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, view)
    }

    private fun createViewHolderInternal(
        clazz: Class<PerDataItem<*, RecyclerView.ViewHolder>>,
        view: View?
    ): RecyclerView.ViewHolder {
        val superclass = clazz.genericSuperclass
        if (superclass is ParameterizedType) {
            val arguments = superclass.actualTypeArguments
            for (argument in arguments) {
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    return argument.getConstructor(View::class.java)
                        .newInstance(view) as RecyclerView.ViewHolder
                }
            }
        }

        return object : RecyclerView.ViewHolder(view!!) {}
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataItem = dataSets[position]
        dataItem.onBindData(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < dataSets.size) {
                        val dataItem = dataSets[position]
                        val spanSize = dataItem.getSpanSize()
                        return if (spanSize <= 0) spanCount else spanSize
                    }

                    return spanCount
                }
            }
        }
    }

}
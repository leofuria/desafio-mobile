package com.barreto.fulllab.presentation.category.adapter

import android.view.View
import android.widget.TextView
import com.barreto.android.domain.category.model.CategoryItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.base.adapter.BaseViewHolder
import io.reactivex.Observable

class CategoryViewHolder(view: View) : BaseViewHolder<CategoryItem>(view) {

    private val titleText by lazy { itemView.findViewById<TextView>(R.id.titleText) }

    init {
        itemView.setOnClickListener {
            currentItem?.run {
                notifyItemClick.onNext(this)
            }
        }
    }

    override fun bindItem(item: CategoryItem?) {
        super.bindItem(item)

        currentItem = item
        currentItem?.also {
            titleText.text = it.name
        }
    }

    fun getItemClickObservable(): Observable<CategoryItem> {
        return notifyItemClick
    }
}
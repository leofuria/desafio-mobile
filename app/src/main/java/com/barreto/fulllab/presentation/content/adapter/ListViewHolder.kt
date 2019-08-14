package com.barreto.fulllab.presentation.content.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.barreto.android.domain.content.model.ContentItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.base.adapter.BaseViewHolder
import com.squareup.picasso.Picasso
import io.reactivex.Observable

class ListViewHolder(view: View) : BaseViewHolder<ContentItem>(view) {

    private val titleText by lazy { itemView.findViewById<TextView>(R.id.titleText) }
    private val valueText by lazy { itemView.findViewById<TextView>(R.id.valueText) }
    private val thumbnail by lazy { itemView.findViewById<ImageView>(R.id.thumbnail) }

    init {
        itemView.setOnClickListener {
            currentItem?.run {
                notifyItemClick.onNext(this)
            }
        }
    }

    override fun bindItem(item: ContentItem?) {
        super.bindItem(item)

        currentItem = item
        currentItem?.also {
            titleText.text = it.title
            valueText.text = it.value
            Picasso.with(itemView.context)
                .load(it.thumbnail)
                .noFade()
                .placeholder(R.drawable.ic_image_black_24dp)
                .fit()
                .tag("placholder")
                .into(thumbnail)
        }
    }

    fun getItemClickObservable(): Observable<ContentItem> {
        return notifyItemClick
    }
}
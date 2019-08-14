package com.barreto.fulllab.presentation.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.barreto.android.domain.category.model.CategoryItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.base.adapter.PagedAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class CategoryAdapter : PagedAdapter<CategoryItem, CategoryViewHolder>(DIFF_CALLBACK) {

    private val notifyClick: PublishSubject<Pair<Int, CategoryItem>> = PublishSubject.create()

    override fun createItemView(parent: ViewGroup): CategoryViewHolder {

        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        ).apply {
            getItemClickObservable().subscribe {
                notifyClick.onNext(Pair(adapterPosition, it))
            }
        }
    }

    override fun bindItemView(holder: CategoryViewHolder, position: Int) {

        holder.bindItem(getItem(position))
    }

    fun getNotifyItemClick(): Observable<Pair<Int, CategoryItem>> {
        return notifyClick
    }

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategoryItem>() {

            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
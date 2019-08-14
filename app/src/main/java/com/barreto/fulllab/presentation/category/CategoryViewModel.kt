package com.barreto.fulllab.presentation.category

import androidx.lifecycle.MutableLiveData
import com.barreto.android.domain.base.Event
import com.barreto.android.domain.category.model.CategoryItem
import com.barreto.android.domain.category.usecase.GetCategoryListUseCase
import com.barreto.android.domain.util.ISchedulerProvider
import com.barreto.fulllab.presentation.base.BaseViewModel
import io.reactivex.rxkotlin.addTo

class CategoryViewModel(
    scheduler: ISchedulerProvider,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseViewModel(scheduler) {

    private var currentPage: Int = 1
    private var totalItemCount: Int = 0
    private var currentItemCount: Int = 0

    val hasNextPage = MutableLiveData<Boolean>().apply { value = true }

    val error = MutableLiveData<Event.Error?>().apply { value = null }

    val total = MutableLiveData<Int>().apply { value = 0 }

    val categoryList = MutableLiveData<Event<List<CategoryItem>>>().apply { value = Event.idle() }

    fun getCategoryList() {

        getCategoryListUseCase.execute()
            .subscribeOn(scheduler.backgroundThread())
            .observeOn(scheduler.mainThread())
            .subscribe {
                when (it) {
                    is Event.Data -> {
                        currentPage += 1
                        totalItemCount = it.data.total
                        total.postValue(totalItemCount)
                        currentItemCount = it.data.items?.size ?: 0
                        hasNextPage.value = (currentItemCount < totalItemCount)
                        categoryList.postValue(Event.data(it.data.items ?: emptyList()))
                    }
                    is Event.Loading -> categoryList.postValue(it)
                    is Event.Error -> categoryList.postValue(it)
                }
            }
            .addTo(disposables)
    }
}
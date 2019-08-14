package com.barreto.android.domain.category.usecase

import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.base.Event
import com.barreto.android.domain.category.ICategoryRepository
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Observable

class GetCategoryListUseCase(
    private val repository: ICategoryRepository
) {

    fun execute(): Observable<Event<BaseListModel<CategoryItem>>> {

        return Observable.concat(
            Observable.just(Event.loading()),
            getCategoryList()
        )
    }

    private fun getCategoryList(): Observable<Event<BaseListModel<CategoryItem>>> {

        return repository.getCategoryList()
            .map { Event.data(it) }
            .onErrorReturn { Event.error(it) }
            .toObservable()
    }
}
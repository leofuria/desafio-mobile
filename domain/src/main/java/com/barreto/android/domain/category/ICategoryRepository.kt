package com.barreto.android.domain.category

import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Single

interface ICategoryRepository {

    fun getCategoryList(): Single<BaseListModel<CategoryItem>>
}
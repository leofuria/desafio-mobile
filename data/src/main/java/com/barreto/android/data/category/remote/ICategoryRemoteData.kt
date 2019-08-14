package com.barreto.android.data.category.remote

import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Single

interface ICategoryRemoteData {

    fun fetchCategoryList(): Single<BaseListModel<CategoryItem>>
}
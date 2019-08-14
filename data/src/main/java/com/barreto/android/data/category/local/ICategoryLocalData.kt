package com.barreto.android.data.category.local

import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Single

interface ICategoryLocalData {

    fun getCategoryList(): Single<BaseListModel<CategoryItem>>
}
package com.barreto.android.data.category.remote

import com.barreto.android.data.category.remote.api.ICategoryApiClient
import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Single

class CategoryRemoteData(
    private val apiClient: ICategoryApiClient
) : ICategoryRemoteData {

    override fun fetchCategoryList(): Single<BaseListModel<CategoryItem>> {

        return apiClient.fetchCategoryList()
            .map { list ->

                list.toListModel()
            }
    }
}
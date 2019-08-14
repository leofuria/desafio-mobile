package com.barreto.android.data.category

import com.barreto.android.data.category.local.ICategoryLocalData
import com.barreto.android.data.category.remote.ICategoryRemoteData
import com.barreto.android.data.remote.parseRemoteError
import com.barreto.android.domain.base.BaseListModel
import com.barreto.android.domain.base.toSingleError
import com.barreto.android.domain.category.ICategoryRepository
import com.barreto.android.domain.category.model.CategoryItem
import io.reactivex.Single

class CategoryRepository(
    private val remoteData: ICategoryRemoteData,
    private val localData: ICategoryLocalData
) : ICategoryRepository {

    override fun getCategoryList(): Single<BaseListModel<CategoryItem>> {

        return Single.concat(
            localData.getCategoryList(),
            remoteData.fetchCategoryList()
        )
            .filter { it.items != null }
            .firstOrError()
            .onErrorResumeNext { it.toSingleError(parseRemoteError(it), it.message) }
    }
}
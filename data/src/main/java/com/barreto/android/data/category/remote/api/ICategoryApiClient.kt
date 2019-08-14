package com.barreto.android.data.category.remote.api

import com.barreto.android.data.category.remote.response.CategoryListResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ICategoryApiClient {

    @GET("StorePreference/CategoryTree")
    fun fetchCategoryList(): Single<CategoryListResponse>
}
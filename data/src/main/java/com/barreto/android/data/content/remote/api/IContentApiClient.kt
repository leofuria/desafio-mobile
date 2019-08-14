package com.barreto.android.data.content.remote.api

import com.barreto.android.data.content.remote.response.ContentListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface IContentApiClient {

    @POST("Search/Criteria")
    fun fetchContentList(@QueryMap queryOptions: HashMap<String, Any>): Single<ContentListResponse>
}
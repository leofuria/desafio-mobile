package com.barreto.fulllab.presentation.category

import com.barreto.android.data.category.CategoryRepository
import com.barreto.android.data.category.local.CategoryLocalData
import com.barreto.android.data.category.local.ICategoryLocalData
import com.barreto.android.data.category.remote.CategoryRemoteData
import com.barreto.android.data.category.remote.ICategoryRemoteData
import com.barreto.android.data.category.remote.api.ICategoryApiClient
import com.barreto.android.data.provider.RemoteClientFactory
import com.barreto.android.domain.category.ICategoryRepository
import com.barreto.android.domain.category.usecase.GetCategoryListUseCase
import com.barreto.fulllab.di.REMOTE_CLIENT_FACTORY
import org.koin.android.experimental.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy

val categoryModule = module {

    viewModel<CategoryViewModel>()

    factory<GetCategoryListUseCase>()

    singleBy<ICategoryRepository, CategoryRepository>()

    factoryBy<ICategoryLocalData, CategoryLocalData>()

    factoryBy<ICategoryRemoteData, CategoryRemoteData>()

    single { get<RemoteClientFactory>(named(REMOTE_CLIENT_FACTORY)).createClient(ICategoryApiClient::class) }
}
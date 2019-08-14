package com.barreto.fulllab.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.barreto.android.data.remote.RemoteError
import com.barreto.android.domain.base.Event
import com.barreto.android.domain.content.model.ContentItem
import com.barreto.fulllab.R
import com.barreto.fulllab.presentation.base.adapter.PagedAdapter
import com.barreto.fulllab.presentation.base.view.PagedListLayout
import com.barreto.fulllab.presentation.category.CategoryActivity
import com.barreto.fulllab.presentation.content.ContentViewModel
import com.barreto.fulllab.presentation.category.adapter.CategoryAdapter
import com.barreto.fulllab.presentation.content.adapter.ListAdapter
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var contentList: List<ContentItem> = emptyList()
    private var totalItems = 0

    private val viewModel by viewModel<ContentViewModel>()

    private val adapter = ListAdapter()
    protected val disposable = CompositeDisposable()

    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val contentListView by lazy { findViewById<PagedListLayout>(R.id.content_list_view) }
    private val btCategory by lazy { findViewById<Button>(R.id.btCategory) }
    private val listBreadcrumbText by lazy { findViewById<TextView>(R.id.listBreadcrumbText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildToolbar()

        contentListView?.setOnRefreshListener {
            viewModel.getContentList()
        }
        contentListView?.setColorSchemeResources(R.color.colorPrimary)

        contentListView.adapter = this.adapter

        adapter.loadMoreListener = object : PagedAdapter.ILoadMoreListener {
            override fun onLoadMore() = viewModel.loadMore()
        }

        btCategory.setOnClickListener {
            startActivity(
                CategoryActivity.buildIntent(
                    this@MainActivity,
                    getString(R.string.bt_category)
                )
            )
        }

        initialize()
    }

    private fun initialize() {
        viewModel.hasNextPage.observe(this, Observer { adapter.loadEnable = (it == true) })
        viewModel.error.observe(this, Observer { showError(it) })
        viewModel.total.observe(this, Observer { totalItems = it ?: 0 })
        viewModel.contentList.observe(this, Observer { onContentsEvent(it) })
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = intent.getStringExtra(TOOLBAR_TITLE) ?: getString(R.string.app_name)
    }

    override fun onDestroy() {

        disposable.clear()
        super.onDestroy()
    }

    private fun onContentsEvent(event: Event<List<ContentItem>>?) {

        when (event) {
            is Event.Idle -> viewModel.getContentList()
            is Event.Data -> {
                listBreadcrumbText.visibility = View.VISIBLE
                contentList = event.data
                contentListView.isRefreshing = false
                if (event.data.isNullOrEmpty()) {
                    contentListView.showFeedbackStatus(R.drawable.ic_search_black_24dp, R.string.contents_not_found)
                } else {
                    adapter.submitList(event.data)
                }
            }
            is Event.Loading -> contentListView.takeUnless { it.isRefreshing }?.isRefreshing = true
            is Event.Error -> showError(event)
        }
    }

    private fun showError(error: Event.Error?) {

        if (error == null) return

        contentListView.isRefreshing = false
        when (error.error.type) {
            RemoteError.UNKNOW_HOST -> if (adapter.itemCount <= 0) {
                contentListView.showFeedbackStatus(
                    feedbackTitle = R.string.error_internet_title,
                    feedbackMessage = R.string.error_internet_msg,
                    action = {
                        viewModel.getContentList()
                    }
                )
            } else {
                adapter.showLoadMoreError()
            }
            else -> contentListView.showFeedbackStatus(feedbackTitle = R.string.generic_error)
        }
        Timber.d(error.error)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TOOLBAR_TITLE = "toolbarTitle"
    }
}

package com.example.composetest

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.composetest.model.natwork.KakaoImageAPI
import com.example.composetest.model.natwork.respose.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * @author gon (leeyg@brandi.co.kr)
 * @since 2021/03/03.
 **/
@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val api: KakaoImageAPI,
    private val handle: SavedStateHandle
) : ViewModel() {
    private val _keyword = MutableStateFlow(TextFieldValue())
    val keyword: StateFlow<TextFieldValue>
        get() = _keyword

    val imageList = _keyword.flatMapLatest {
        Pager(PagingConfig(pageSize = 20,maxSize = 80)) {
            Log.d("TAG", "keyword=$it")
            KakaoImageSearchPagingSource(api, it.text)
        }.flow.cachedIn(viewModelScope)
    }

    fun searchImage(keyword: TextFieldValue) {
        _keyword.value = keyword
    }

}

class KakaoImageSearchPagingSource(
    private val api: KakaoImageAPI,
    private val query: String
) : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>) = try {
        val nextPageNumber = params.key ?: 1
        val response = api.requestImageList(
            query = query,
            page = nextPageNumber,
            size = params.loadSize
        )
        LoadResult.Page(
            data = response.documents ?: emptyList(),
            prevKey = null,
            nextKey = nextPageNumber + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, Image>) =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}

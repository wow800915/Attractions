package com.weiyou.attractions.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiyou.attractions.data.models.AttractionsResponse
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _attractionResponse = MutableLiveData<NetworkResult<AttractionsResponse>?>()
    val attractionResponse: LiveData<NetworkResult<AttractionsResponse>?> = _attractionResponse

    suspend fun fetchAttractions() {
        homeRepository.getAttractions()
            .onStart {
                // 可以在这里发射一个加载状态
            }
            .onEach { response ->
                _attractionResponse.value = response
            }
            .catch { e ->
                // 处理异常，例如发射一个错误状态或者将错误记录下来
                // _attractionResponse.value = null // 如果需要在错误时清空数据
            }
            .launchIn(viewModelScope) // 使用 viewModelScope 启动协程
    }

    suspend fun saveLanguage(language: String) {
        viewModelScope.launch {
            homeRepository.saveLanguage(language)
        }
    }

    val language = homeRepository.getSavedLanguage()
}
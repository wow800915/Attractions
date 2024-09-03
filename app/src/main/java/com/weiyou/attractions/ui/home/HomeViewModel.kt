package com.weiyou.attractions.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.models.api.news.NewsOutput
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

    private val _attractions = MutableLiveData<AttractionsOutput?>()
    val attractions: LiveData<AttractionsOutput?> = _attractions

    private val _news = MutableLiveData<NewsOutput?>()
    val news: LiveData<NewsOutput?> = _news

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    suspend fun fetchAttractions(page: Int) {
        viewModelScope.launch {
            homeRepository.getAttractions(page)
                .onStart {
                    _attractions.value = null
                    _isLoading.value = true
                }
                .onEach { result ->
                    _isLoading.value = false
                    if (result is NetworkResult.Success) {
                        _attractions.value = result.data
                    } else if (result is NetworkResult.Error) {
                        _errorMessage.value = result.error?.message
                    }
                }
                .catch { e ->
                    _isLoading.value = false
                    _errorMessage.value = e.message
                }
                .launchIn(viewModelScope) // 使用 viewModelScope 启动协程
        }
    }

    suspend fun fetchNews() {
        viewModelScope.launch {
            homeRepository.getNews()
                .onStart {
                    _news.value = null
                    _isLoading.value = true
                }
                .onEach { result ->
                    _isLoading.value = false
                    if (result is NetworkResult.Success) {
                        _news.value = result.data
                    } else if (result is NetworkResult.Error) {
                        _errorMessage.value = result.error?.message
                    }
                }
                .catch { e ->
                    _isLoading.value = false
                    _errorMessage.value = e.message
                }
                .launchIn(viewModelScope) // 使用 viewModelScope 启动协程
        }
    }

    suspend fun saveLanguage(language: String) {
        viewModelScope.launch {
            homeRepository.saveLanguage(language)
        }
    }

    val language = homeRepository.getSavedLanguage()
}
package com.weiyou.attractions.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
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

    private val _attractions = MutableLiveData<AttractionsOutput?>()
    val attractions: LiveData<AttractionsOutput?> = _attractions


    suspend fun fetchAttractions() {
        viewModelScope.launch {
            homeRepository.getAttractions()
                .onStart {
                    _attractions.value = null //TODO 加載畫面
                }
                .onEach { result ->
                    // 仅在成功的情况下发射数据
                    if (result is NetworkResult.Success) {
                        _attractions.value = result.data
                    } else if (result is NetworkResult.Error) {
                        _attractions.value = null //TODO 錯誤訊息
                    }
                }
                .catch { e ->
                    _attractions.value = null //TODO 錯誤訊息
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
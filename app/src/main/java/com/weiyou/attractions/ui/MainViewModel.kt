package com.weiyou.attractions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weiyou.attractions.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> = _text

    private val _language = MutableLiveData<String?>()
    val language: LiveData<String?> = _language

    init {
        // 示例数据
        _text.value = mainRepository.getExampleData()

        // 启动协程收集语言设置的变化
        viewModelScope.launch {
            mainRepository.getLanguage().collect { lang ->
                _language.value = lang
            }
        }
    }
}
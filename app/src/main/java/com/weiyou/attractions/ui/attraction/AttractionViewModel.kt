package com.weiyou.attractions.ui.attraction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.weiyou.attractions.data.models.api.attractions.Attraction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttractionViewModel @Inject constructor() : ViewModel() {

    private val _attraction = MutableLiveData<Attraction>()
    val attraction: LiveData<Attraction> get() = _attraction

    fun setAttraction(attraction: Attraction) {
        _attraction.value = attraction
    }
}
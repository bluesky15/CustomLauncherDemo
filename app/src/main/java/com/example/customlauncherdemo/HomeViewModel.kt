package com.example.customlauncherdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData = _weatherData.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()
    fun getWeatherData() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getWeatherData().collectLatest {
                when (it) {
                    is Response.Success<*> -> {
                        _weatherData.value = it.data as WeatherData
                    }

                    is Response.Error -> {
                        _error.value = it.error
                    }
                }
            }
        }
    }

}
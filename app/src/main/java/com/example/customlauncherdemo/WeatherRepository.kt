package com.example.customlauncherdemo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val apiService: ApiService) {
    fun getWeatherData(): Flow<Response> {
        return flow {
                try {
                    val weatherData = apiService.getWeatherData()
                    emit(Response.Success(weatherData))
                } catch (e: Exception) {
                    emit(Response.Error(e.message ?: "Error"))
                }
        }
    }
}
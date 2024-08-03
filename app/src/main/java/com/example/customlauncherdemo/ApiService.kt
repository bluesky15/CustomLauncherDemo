package com.example.customlauncherdemo

class ApiService {
    fun getWeatherData(): WeatherData {
        return WeatherData("Bengaluru, India", temp = "26°C", feelLike = "Sunny")
    }
}
package com.example.customlauncherdemo

sealed class Response {
    data class Success<Type>(val data: Type) : Response()
    data class Error(val error: String) : Response()

}
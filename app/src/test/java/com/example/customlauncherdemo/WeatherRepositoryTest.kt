package com.example.customlauncherdemo

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doThrow

class WeatherRepositoryTest {
    var repository: WeatherRepository? = null

    @Mock
    lateinit var service: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(service)
    }

    @After
    fun tearDown() {
        Mockito.reset(service)
    }

    @Test
    fun `should return a flow success type`() {
        Mockito.`when`(service.getWeatherData())
            .thenReturn(WeatherData("Bengaluru, India", temp = "26°C", feelLike = "Sunny"))
        runTest {
            val result = repository?.getWeatherData()
            assertTrue(result is Flow<Response>)
            result?.test {
                val item = awaitItem()
                assertEquals(
                    item,
                    Response.Success(
                        WeatherData(
                            "Bengaluru, India",
                            temp = "26°C",
                            feelLike = "Sunny"
                        )
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `should return a flow error type`() {
        doThrow(RuntimeException("Error")).`when`(service)?.getWeatherData()
        runTest {
            val result = repository?.getWeatherData()
            result?.test {
                val item = awaitItem()
                assertTrue(item is Response.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
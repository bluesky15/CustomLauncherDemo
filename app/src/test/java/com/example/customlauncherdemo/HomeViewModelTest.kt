package com.example.customlauncherdemo

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.mockito.MockitoAnnotations

class HomeViewModelTest {
    var viewModel: HomeViewModel? = null

    @Mock
    lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        reset(repository)
    }

    @Test
    fun `should return a flow`(){
        Mockito.`when`(repository.getWeatherData())
            .thenReturn(flow { emit(Response.Error("ERROR")) })
        runTest {
            val result = repository.getWeatherData()
            Assert.assertTrue(result is Flow<Response>)
        }
    }

    @Test
    fun `should return error`() {
        Mockito.`when`(repository.getWeatherData())
            .thenReturn(flow { emit(Response.Error("ERROR")) })
        runTest {
            viewModel?.error?.test {
                viewModel?.getWeatherData()
                val item = awaitItem()
                val item2 = awaitItem()
                Assert.assertEquals(null, item)
                Assert.assertEquals("ERROR", item2)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `should return weather data`() {
        Mockito.`when`(repository.getWeatherData())
            .thenReturn(flow { emit(Response.Success(WeatherData("", "", ""))) })
        runTest {
            viewModel?.weatherData?.test {
                viewModel?.getWeatherData()
                val item = awaitItem()
                val item2 = awaitItem()
                Assert.assertEquals(null, item)
                Assert.assertEquals(true, item2 is WeatherData)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}
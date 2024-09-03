package com.weiyou.attractions

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.weiyou.attractions.data.models.NetworkResult
import com.weiyou.attractions.data.models.api.attractions.AttractionsOutput
import com.weiyou.attractions.data.models.ErrorResponse
import com.weiyou.attractions.data.repository.HomeRepository
import com.weiyou.attractions.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var homeRepository: HomeRepository

    @Mock
    private lateinit var attractionsObserver: Observer<AttractionsOutput?>

    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var errorMessageObserver: Observer<String?>

    private lateinit var homeViewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        homeViewModel = HomeViewModel(homeRepository)
    }

    @Test
    fun `fetchAttractions success`() = runTest {
        val mockAttractionsOutput = AttractionsOutput(total = 10, data = emptyList())
        `when`(homeRepository.getAttractions(1)).thenReturn(
            flowOf(
                NetworkResult.Success(
                    mockAttractionsOutput
                )
            )
        )

        homeViewModel.attractions.observeForever(attractionsObserver)
        homeViewModel.isLoading.observeForever(isLoadingObserver)

        homeViewModel.fetchAttractions(1)

        verify(isLoadingObserver).onChanged(true)
        verify(attractionsObserver).onChanged(mockAttractionsOutput)
        verify(isLoadingObserver).onChanged(false)

        homeViewModel.attractions.removeObserver(attractionsObserver)
        homeViewModel.isLoading.removeObserver(isLoadingObserver)
    }

    @Test
    fun `fetchAttractions error`() = runTest {
        val errorResponse = ErrorResponse(message = "Network Error")
        `when`(homeRepository.getAttractions(1)).thenReturn(flowOf(NetworkResult.Error(errorResponse)))

        homeViewModel.attractions.observeForever(attractionsObserver)
        homeViewModel.isLoading.observeForever(isLoadingObserver)
        homeViewModel.errorMessage.observeForever(errorMessageObserver)

        homeViewModel.fetchAttractions(1)

        verify(isLoadingObserver).onChanged(true)
        verify(attractionsObserver).onChanged(null)
        verify(errorMessageObserver).onChanged("Network Error")
        verify(isLoadingObserver).onChanged(false)

        homeViewModel.attractions.removeObserver(attractionsObserver)
        homeViewModel.isLoading.removeObserver(isLoadingObserver)
        homeViewModel.errorMessage.removeObserver(errorMessageObserver)
    }
}
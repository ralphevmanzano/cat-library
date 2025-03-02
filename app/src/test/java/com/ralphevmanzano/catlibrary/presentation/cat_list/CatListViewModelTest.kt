package com.ralphevmanzano.catlibrary.presentation.cat_list

import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatsUseCase
import com.ralphevmanzano.catlibrary.presentation.model.toCatUi
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class CatListViewModelTest {
    @MockK
    private lateinit var catRepository: CatRepository

    private lateinit var viewModel: CatListViewModel
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var getCatsUseCase: GetCatsUseCase

    private val testCatList = listOf(
        Cat(
            id = "abyss",
            name = "Abyssinian",
            description = "testAbyssDescription",
            weight = "4 - 5",
            lifeSpan = "12 - 15",
            imageUrl = "testImageUrl",
            imageHeight = 100,
            imageWidth = 100
        ),
        Cat(
            id = "aege",
            name = "Aegean",
            description = "testAegeanDescription",
            weight = "4 - 5",
            lifeSpan = "12 - 15",
            imageUrl = "testImageUrl",
            imageHeight = 100,
            imageWidth = 100
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        getCatsUseCase = GetCatsUseCase(catRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getCats should update state with cats`() = runTest {
        // Given
        val resultFlow = flow {
            delay(1)
            emit(Result.Success(testCatList))
        }
        coEvery { catRepository.getCats(isRefresh) } returns resultFlow

        viewModel = CatListViewModel(getCatsUseCase)

        // When
        val stateCollector = mutableListOf<CatListState>()
        val stateJob = launch {
            viewModel.state.collect { stateCollector.add(it) }
        }

        testScheduler.advanceUntilIdle()

        stateJob.cancel()

        // Then
        assertEquals(3, stateCollector.size)
        assertEquals(CatListState(), stateCollector.first())
        assertEquals(CatListState(isLoading = true), stateCollector[1])
        assertEquals(CatListState(isLoading = false, cats = testCatList.map { it.toCatUi() }), stateCollector.last())

        coVerify { catRepository.getCats(isRefresh) }
    }

    @Test
    fun `getCats should update state with error`() = runTest {
        // Given
        val networkError = NetworkError.Unknown
        val resultFlow = flow {
            delay(1)
            emit(Result.Error(networkError))
        }
        coEvery { catRepository.getCats(isRefresh) } returns resultFlow

        viewModel = CatListViewModel(getCatsUseCase)

        // When
        val stateCollector = mutableListOf<CatListState>()
        val errorCollector = mutableListOf<NetworkError>()
        val stateJob = launch {
            viewModel.state.collect { stateCollector.add(it) }
        }
        val errorJob = launch {
            viewModel.errorEvents.collect { errorCollector.add(it) }
        }

        testScheduler.advanceUntilIdle()

        stateJob.cancel()
        errorJob.cancel()

        // Then
        assertEquals(3, stateCollector.size)
        assertEquals(CatListState(), stateCollector.first())
        assertEquals(CatListState(isLoading = true), stateCollector[1])
        assertEquals(CatListState(isLoading = false), stateCollector.last())

        assertEquals(1, errorCollector.size)
        assertEquals(networkError, errorCollector.first())

        coVerify { catRepository.getCats(isRefresh) }
    }
}
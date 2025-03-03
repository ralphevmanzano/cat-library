package com.ralphevmanzano.catlibrary.presentation.cat_details

import androidx.lifecycle.SavedStateHandle
import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloaderRepository
import com.ralphevmanzano.catlibrary.domain.usecase.DownloadImageUseCase
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatDetailsUseCase
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


class CatDetailsViewModelTest {

    @MockK
    private lateinit var catRepository: CatRepository

    @MockK
    private lateinit var imageDownloaderRepository: ImageDownloaderRepository

    private lateinit var viewModel: CatDetailsViewModel
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getCatDetailsUseCase: GetCatDetailsUseCase
    private lateinit var downloadImageUseCase: DownloadImageUseCase

    private val testCat = Cat(
        id = "abyss",
        name = "Abyssinian",
        description = "testDescription",
        weight = "4 - 5",
        lifeSpan = "12 - 15",
        imageUrl = "testImageUrl",
        imageHeight = 100,
        imageWidth = 100
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        savedStateHandle = SavedStateHandle().apply {
            set("catId", testCat.id)
        }
        getCatDetailsUseCase = GetCatDetailsUseCase(catRepository)
        downloadImageUseCase = DownloadImageUseCase(imageDownloaderRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getCatDetails should emit success state on success`() = runTest {
        // Given
        val resultFlow = flow {
            delay(1)
            emit(Result.Success(testCat))
        }
        coEvery { catRepository.getCatDetails(testCat.id) } returns resultFlow

        viewModel = CatDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCatDetailsUseCase = getCatDetailsUseCase,
            downloadImageUseCase = downloadImageUseCase
        )

        val stateCollector = mutableListOf<CatDetailsState>()
        val errorCollector = mutableListOf<NetworkError>()

        val stateJob = launch {
            viewModel.state.collect {
                stateCollector.add(it)
            }
        }
        val errorJob = launch {
            viewModel.errorEvents.collect {
                errorCollector.add(it)
            }
        }

        testScheduler.advanceUntilIdle()

        stateJob.cancel()
        errorJob.cancel()

        // Then
        assertEquals(3, stateCollector.size)
        assertEquals(CatDetailsState(), stateCollector.first())
        assertEquals(CatDetailsState(isLoading = true), stateCollector[1])
        assertEquals(CatDetailsState(isLoading = false, cat = testCat.toCatUi()), stateCollector.last())

        assertEquals(0, errorCollector.size)

        coVerify { catRepository.getCatDetails(testCat.id) }
    }

    @Test
    fun `getCatDetails should emit error event on failure`() = runTest {
        // Given
        val networkError = NetworkError.TooManyRequests
        val resultFlow = flow {
            delay(1)
            emit(Result.Error(networkError))
        }
        coEvery { catRepository.getCatDetails(testCat.id) } returns resultFlow

        viewModel = CatDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCatDetailsUseCase = getCatDetailsUseCase,
            downloadImageUseCase = downloadImageUseCase
        )

        // When
        val stateCollector = mutableListOf<CatDetailsState>()
        val errorCollector = mutableListOf<NetworkError>()

        val stateJob = launch {
            viewModel.state.collect {
                stateCollector.add(it)
            }
        }
        val errorJob = launch {
            viewModel.errorEvents.collect {
                errorCollector.add(it)
            }
        }

        testScheduler.advanceUntilIdle()

        errorJob.cancel()
        stateJob.cancel()

        // Then
        assertEquals(3, stateCollector.size)
        assertEquals(CatDetailsState(), stateCollector.first())
        assertEquals(CatDetailsState(isLoading = true), stateCollector[1])
        assertEquals(CatDetailsState(isLoading = false), stateCollector.last())

        assertEquals(1, errorCollector.size)
        assertEquals(networkError, errorCollector[0])

        coVerify { catRepository.getCatDetails(testCat.id) }
    }

    @Test
    fun `downloadImage should update downloadStatus with progress and success`() = runTest {
        // Given
        val imageUrl = testCat.imageUrl
        val fileName = "${testCat.name}_${System.currentTimeMillis()}.jpg"
        val resultFlow = flow {
            emit(DownloadStatus.Downloading(0))
            delay(1)
            emit(DownloadStatus.Downloading(50))
            delay(1)
            emit(DownloadStatus.Success(fileName))
        }
        coEvery { imageDownloaderRepository.downloadImage(imageUrl, any()) } returns resultFlow

        viewModel = CatDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCatDetailsUseCase = getCatDetailsUseCase,
            downloadImageUseCase = downloadImageUseCase
        )

        // When
        val downloadStatusCollector = mutableListOf<DownloadStatus>()
        val downloadStatusJob = launch {
            viewModel.downloadStatus.collect {
                downloadStatusCollector.add(it)
            }
        }

        viewModel.downloadImage(imageUrl)

        testScheduler.advanceUntilIdle()

        downloadStatusJob.cancel()

        // Then
        assertEquals(4, downloadStatusCollector.size)
        assertEquals(DownloadStatus.Idle, downloadStatusCollector.first())
        assertEquals(DownloadStatus.Downloading(0), downloadStatusCollector[1])
        assertEquals(DownloadStatus.Downloading(50), downloadStatusCollector[2])
        assertEquals(DownloadStatus.Success(fileName), downloadStatusCollector.last())

        coVerify { imageDownloaderRepository.downloadImage(imageUrl, any()) }
    }

    @Test
    fun `downloadImage imageDownloader should not be called when imageUrl is blank`() = runTest {
        // Given
        val imageUrl = ""
        val fileName = "${testCat.name}_${System.currentTimeMillis()}.jpg"
        val resultFlow = flow {
            emit(DownloadStatus.Downloading(0))
            delay(1)
            emit(DownloadStatus.Downloading(50))
            delay(1)
            emit(DownloadStatus.Success(fileName))
        }
        coEvery { imageDownloaderRepository.downloadImage(imageUrl, any()) } returns resultFlow

        viewModel = CatDetailsViewModel(
            savedStateHandle = savedStateHandle,
            getCatDetailsUseCase = getCatDetailsUseCase,
            downloadImageUseCase = downloadImageUseCase
        )

        // When
        val downloadStatusCollector = mutableListOf<DownloadStatus>()
        val downloadStatusJob = launch {
            viewModel.downloadStatus.collect {
                downloadStatusCollector.add(it)
            }
        }

        viewModel.downloadImage(imageUrl)

        testScheduler.advanceUntilIdle()

        downloadStatusJob.cancel()

        // Then
        assertEquals(1, downloadStatusCollector.size)
        assertEquals(DownloadStatus.Idle, downloadStatusCollector.first())

        coVerify(exactly = 0) { imageDownloaderRepository.downloadImage(any(), any()) }
    }
}
package com.ralphevmanzano.catlibrary.presentation.cat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.CatListItem
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.previewCat
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme
import com.ralphevmanzano.catlibrary.utils.presentation.ObserveAsEvents
import com.ralphevmanzano.catlibrary.utils.toString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

private const val REFRESH_ANIMATION_DURATION = 1000L
private const val GRID_CELLS_COUNT = 2

@Composable
fun CatListScreen(
    modifier: Modifier = Modifier,
    viewModel: CatListViewModel = koinViewModel(),
    onNavigateToCatDetail: (String) -> Unit
) {
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(events = viewModel.errorEvents) {
        snackBarHostState.showSnackbar(
            message = it.toString(context),
            duration = SnackbarDuration.Short
        )
    }

    CatListContent(
        modifier = modifier,
        state = state,
        snackBarHostState = snackBarHostState,
        onNavigateToCatDetail = onNavigateToCatDetail,
        onRefresh = { viewModel.getCats(true) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListContent(
    modifier: Modifier = Modifier,
    state: CatListState,
    snackBarHostState: SnackbarHostState,
    onNavigateToCatDetail: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val gridState = rememberLazyStaggeredGridState()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.cat_library)) })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading && state.cats.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.cats.isNotEmpty()) {
                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        onRefresh()

                        // Simulate refresh animation, as response is instant, the refresh animation
                        // cannot be seen without this delay
                        scope.launch {
                            delay(REFRESH_ANIMATION_DURATION)
                            isRefreshing = false
                        }
                    }
                ) {
                    LazyVerticalStaggeredGrid(
                        state = gridState,
                        columns = StaggeredGridCells.Fixed(GRID_CELLS_COUNT),
                        contentPadding = PaddingValues(16.dp),
                        verticalItemSpacing = 16.dp,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(items = state.cats, key = { it.id }) { cat ->
                            CatListItem(
                                catUi = cat,
                                onItemClick = { onNavigateToCatDetail(cat.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatListContentPreview() {
    CatLibraryTheme {
        CatListContent(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            state = CatListState(
                cats = (1..10).map {
                    previewCat.copy(
                        id = it.toString(),
                        imageAspectRatio = Random.nextFloat().coerceIn(0.5f, 2f),
                    )
                }
            ),
            snackBarHostState = SnackbarHostState(),
            onNavigateToCatDetail = {},
            onRefresh = {}
        )
    }
}




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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.CatListItem
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.previewCat
import com.ralphevmanzano.catlibrary.presentation.model.CatUi
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme
import com.ralphevmanzano.catlibrary.utils.ObserveAsEvents
import com.ralphevmanzano.catlibrary.utils.toString
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random

@Composable
fun CatListScreen(
    modifier: Modifier = Modifier,
    viewModel: CatListViewModel = koinViewModel(),
    onNavigateToCatDetail: (CatUi) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(events = viewModel.errorEvents) {
        snackBarHostState.showSnackbar(
            message = it.toString(context),
            duration = SnackbarDuration.Long
        )
    }

    CatListContent(
        modifier = modifier,
        state = state,
        snackBarHostState = snackBarHostState,
        onNavigateToCatDetail = onNavigateToCatDetail
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListContent(
    modifier: Modifier = Modifier,
    state: CatListState,
    snackBarHostState: SnackbarHostState,
    onNavigateToCatDetail: (CatUi) -> Unit
) {

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TopAppBar(title = { Text(text = "Cat Library") })
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
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.cats) { cat ->
                        CatListItem(
                            catUi = cat,
                            onItemClick = { onNavigateToCatDetail(cat) },
                            modifier = Modifier.fillMaxWidth()
                        )
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
                        imageAspectRatio = Random.nextFloat().coerceIn(0.5f, 1f)
                    )
                }
            ),
            snackBarHostState = SnackbarHostState(),
            onNavigateToCatDetail = {}
        )
    }
}




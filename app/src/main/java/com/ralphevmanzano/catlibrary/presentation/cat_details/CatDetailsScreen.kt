package com.ralphevmanzano.catlibrary.presentation.cat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.presentation.cat_details.components.CatDetailSection
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.previewCat
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme
import com.ralphevmanzano.catlibrary.utils.presentation.ObserveAsEvents
import com.ralphevmanzano.catlibrary.utils.toString
import org.koin.androidx.compose.koinViewModel

@Composable
fun CatDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: CatDetailsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val downloadStatus by viewModel.downloadStatus.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.errorEvents) {
        snackBarHostState.showSnackbar(
            message = it.toString(context),
            duration = SnackbarDuration.Short
        )
    }

    LaunchedEffect(downloadStatus) {
        when (downloadStatus) {
            is DownloadStatus.Success -> {
                val fileName = (downloadStatus as DownloadStatus.Success).fileName
                snackBarHostState.showSnackbar(
                    message = context.getString(R.string.image_saved, fileName),
                    duration = SnackbarDuration.Short
                )
            }
            is DownloadStatus.Error -> {
                val message = (downloadStatus as DownloadStatus.Error).message
                snackBarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {  }
        }
    }

    CatDetailsContent(
        modifier = modifier,
        state = state,
        snackBarHostState = snackBarHostState,
        onDownloadImage = {
            viewModel.downloadImage(state.cat?.imageUrl.orEmpty())
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsContent(
    modifier: Modifier = Modifier,
    state: CatDetailsState,
    snackBarHostState: SnackbarHostState,
    onDownloadImage: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val catUi = state.cat

    var optionsMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                    title = { Text(text = catUi?.name.orEmpty(), color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { optionsMenuExpanded = !optionsMenuExpanded }
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                tint = Color.White,
                                contentDescription = stringResource(R.string.download)
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = optionsMenuExpanded,
                    onDismissRequest = {
                        optionsMenuExpanded = false
                    },
                    offset = DpOffset(x = (-16).dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.save)) },
                        onClick = {
                            optionsMenuExpanded = false
                            onDownloadImage()
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(innerPadding)
        ) {
            if (state.isLoading && catUi == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (catUi != null) {
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        model = catUi.imageUrl,
                        contentDescription = catUi.name,
                        contentScale = ContentScale.FillWidth
                    )

                    CatDetailSection(
                        modifier = Modifier.fillMaxWidth(),
                        catUi = catUi
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatDetailsScreenPreview() {
    CatLibraryTheme {
        CatDetailsContent(
            modifier = Modifier.fillMaxSize(),
            state = CatDetailsState(isLoading = false, cat = previewCat),
            snackBarHostState = SnackbarHostState(),
            onDownloadImage = {},
            onNavigateBack = {}
        )
    }
}
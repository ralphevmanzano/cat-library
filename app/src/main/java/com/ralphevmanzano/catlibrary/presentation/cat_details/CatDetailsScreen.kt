package com.ralphevmanzano.catlibrary.presentation.cat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.presentation.cat_details.components.IconWithText
import com.ralphevmanzano.catlibrary.presentation.cat_list.components.previewCat
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme
import com.ralphevmanzano.catlibrary.utils.ObserveAsEvents
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
    val name by viewModel.catName.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.errorEvents) {
        snackBarHostState.showSnackbar(
            message = it.toString(context),
            duration = SnackbarDuration.Long
        )
    }

    CatDetailsContent(
        modifier = modifier,
        state = state,
        snackBarHostState = snackBarHostState,
        appBarTitle = name,
        onDownloadImage = {
            // TODO: Download image
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
    appBarTitle: String,
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
                    title = { Text(text = appBarTitle, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
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
                                contentDescription = "Download"
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
                            onDownloadImage()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.share)) },
                        onClick = {
                            // TODO: open share sheet
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

                    Surface(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = catUi.description,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                                IconWithText(
                                    id = R.drawable.ic_life,
                                    contentDescription = stringResource(R.string.life_span),
                                    text = catUi.lifeSpanFormatted
                                )
                                IconWithText(
                                    id = R.drawable.ic_weight,
                                    contentDescription = stringResource(R.string.weight),
                                    text = catUi.weightFormatted
                                )
                            }
                        }
                    }
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
            appBarTitle = previewCat.name,
            snackBarHostState = SnackbarHostState(),
            onDownloadImage = {},
            onNavigateBack = {}
        )
    }
}
package com.ralphevmanzano.catlibrary.presentation.cat_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.presentation.model.CatCardUi
import kotlin.random.Random

private const val IMAGE_SIZE = 300

@Composable
fun CatListItem(
    modifier: Modifier = Modifier,
    catCardUi: CatCardUi,
    onItemClick: (CatCardUi) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onItemClick(catCardUi) }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(catCardUi.imageAspectRatio)
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxSize(),
                    model = ImageRequest.Builder(context)
                        .data(catCardUi.imageUrl)
                        .crossfade(true)
                        .size(IMAGE_SIZE)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(48.dp),
                                painter = painterResource(id = R.drawable.ic_placeholder),
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                    },
                    contentDescription = catCardUi.name,
                    contentScale = ContentScale.Crop
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = catCardUi.name,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun CatListItemPreview() {
    CatListItem(
        catCardUi = previewCatCardUi,
        onItemClick = {}
    )
}

internal val previewCatCardUi = CatCardUi(
    id = "abys",
    name = "Abyssinian",
    imageUrl = "https://cdn2.thecatapi.com/images/MTYwNjQwMg.jpg",
    imageAspectRatio = Random.nextFloat().coerceIn(0.5f, 2f),
)
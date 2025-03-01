package com.ralphevmanzano.catlibrary.presentation.cat_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ralphevmanzano.catlibrary.presentation.model.CatUi

@Composable
fun CatListItem(
    modifier: Modifier = Modifier,
    catUi: CatUi,
    onItemClick: (CatUi) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onItemClick(catUi) }
    ) {
        Column {
            AsyncImage(
                modifier = Modifier.fillMaxSize()
                    .aspectRatio(catUi.imageAspectRatio),
                model = catUi.imageUrl,
                contentDescription = catUi.name,
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            ) {
                Text(
                    text = catUi.name,
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
        catUi = previewCat,
        onItemClick = {}
    )
}

internal val previewCat = CatUi(
    id = "abys",
    name = "Abyssinian",
    imageUrl = "https://cdn2.thecatapi.com/images/MTYwNjQwMg.jpg",
    imageAspectRatio = 0.5f,
    description = "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
    weightFormatted = "4 - 5 kg",
    lifeSpanFormatted = "10 - 15 years",
)
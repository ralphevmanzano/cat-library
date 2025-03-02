package com.ralphevmanzano.catlibrary.presentation.cat_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.presentation.model.CatUi
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme

@Composable
fun CatDetailSection(modifier: Modifier = Modifier, catUi: CatUi) {
    Surface(modifier = modifier) {
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

@Preview
@Composable
private fun CatDetailsSectionPreview() {
    CatLibraryTheme {
        CatDetailSection(
            catUi = CatUi(
                id = "1",
                name = "Abyssinian",
                description = "The Abyssinian is a breed of domestic short-haired cat with a distinctive 'ticked' tabby coat in which individual hairs are banded with different colors.",
                lifeSpanFormatted = "14 - 15 years",
                weightFormatted = "8 - 12 kgs",
                imageUrl = ""
            )
        )
    }
}
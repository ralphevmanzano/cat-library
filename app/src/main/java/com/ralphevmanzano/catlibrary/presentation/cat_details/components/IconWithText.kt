package com.ralphevmanzano.catlibrary.presentation.cat_details.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.ui.theme.CatLibraryTheme

@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    contentDescription: String,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painterResource(id),
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text
        )
    }
}

@Preview
@Composable
private fun IconWithTextPreview() {
    CatLibraryTheme {
        IconWithText(
            id = R.drawable.ic_life,
            contentDescription = "Life span",
            text = "10 - 12 years"
        )
    }
}
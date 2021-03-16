package com.abhishek101.gametracker.ui.features.platform

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek101.gametracker.ui.theme.GameTrackerTheme
import com.google.accompanist.coil.CoilImage
import org.koin.androidx.compose.get

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlatformSelection(viewModel: PlatformSelectionViewModel = get()) {

    val isLoading = viewModel.isLoading
    val platformList = viewModel.platforms

    viewModel.getPlatforms()

    Column {
        Text(
            "Owned Platforms",
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Text(
            "We will use these platforms to tailor your search results",
            style = MaterialTheme.typography.subtitle2,
            color = Color.Gray
        )
        if (isLoading.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .semantics {
                        testTag = "loadingBar"
                    },
                color = MaterialTheme.colors.primaryVariant
            )
        } else {
            Spacer(Modifier.size(20.dp))
            LazyVerticalGrid(cells = GridCells.Fixed(count = 2)) {
                items(platformList.value.size) { index ->
                    val platform = platformList.value[index]
                    val url =
                        "https://images.igdb.com/igdb/image/upload/t_720p/${platform.logoUrl}.png"
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .background(Color.White)
                            .clickable {
                                viewModel.setFavoritePlatform(
                                    platform = platform,
                                    isFavorite = platform.isFavorite?.not() ?: false
                                )
                            }
                    ) {
                        if (platform.isFavorite == true) {
                            Text(text = "Owned")
                        }
                        CoilImage(
                            data = url,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp, 150.dp)
                        )
                        Text(
                            text = platform.name,
                            style = TextStyle(color = Color.Black, fontSize = 18.sp)
                        )
                    }

                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PlatformSelectionScreen() {
    GameTrackerTheme {
        PlatformSelection()
    }
}
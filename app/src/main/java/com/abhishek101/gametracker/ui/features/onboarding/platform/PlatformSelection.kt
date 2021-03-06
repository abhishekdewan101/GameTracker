package com.abhishek101.gametracker.ui.features.onboarding.platform

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
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
import com.abhishek101.core.db.Platform
import com.abhishek101.gametracker.ui.theme.GameTrackerTheme
import com.google.accompanist.coil.CoilImage
import org.koin.androidx.compose.get

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlatformSelection(
    viewModel: PlatformSelectionViewModel = get(),
    navigateToGenreScreen: () -> Unit
) {

    val isLoading = viewModel.isLoading
    val platformList = viewModel.platforms

    PlatformSelectionContent(
        isLoading = isLoading.value,
        platformList = platformList.value,
        onPlatformSelected = { platform: Platform, isOwned: Boolean ->
            viewModel.updateOwnedPlatform(platform, isOwned)
        },
        getOwnedPlatformCount = viewModel::getOwnedPlatformCount,
        navigateToGenreScreen = navigateToGenreScreen
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlatformSelectionContent(
    isLoading: Boolean,
    platformList: List<Platform>,
    onPlatformSelected: (Platform, Boolean) -> Unit,
    getOwnedPlatformCount: () -> Int,
    navigateToGenreScreen: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(top = 15.dp, start = 15.dp, end = 15.dp)) {
            Text(
                "Owned Platforms",
                style = MaterialTheme.typography.h4,
                color = Color.White
            )
            Text(
                "We will use these platforms to tailor your search results",
                style = MaterialTheme.typography.subtitle1,
                color = Color.Gray
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .semantics {
                                testTag = "loadingBar"
                            },
                        color = MaterialTheme.colors.primaryVariant
                    )
                } else {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(count = 2),
                        modifier = Modifier.padding(top = 20.dp)
                    ) {
                        items(platformList.size) { index ->
                            PlatformListItem(platform = platformList[index]) {
                                onPlatformSelected(it, it.isOwned?.not() ?: false)
                            }
                        }
                    }
                }
            }
        }
        if (getOwnedPlatformCount() > 0) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 15.dp, end = 15.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = navigateToGenreScreen,
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
fun PlatformListItem(platform: Platform, onPlatformSelected: (Platform) -> Unit) {
    val url =
        "https://images.igdb.com/igdb/image/upload/t_720p/${platform.logoUrl}.png"

    val nonHighLighted = Modifier
        .padding(10.dp)
        .background(Color.White)
        .clickable {
            onPlatformSelected(platform)
        }

    val highLighted = Modifier
        .border(2.dp, color = MaterialTheme.colors.secondaryVariant)
        .padding(10.dp)
        .background(Color.White)
        .clickable {
            onPlatformSelected(platform)
        }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = if (platform.isOwned == true) {
            highLighted
        } else {
            nonHighLighted
        }
    ) {
        //Todo: Refactor this to be a circular background that shows the icon and text and then shows an outline when the user has selected it
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

@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PlatformSelectionScreenLoadingState() {
    GameTrackerTheme {
        PlatformSelectionContent(
            isLoading = true,
            platformList = listOf(),
            onPlatformSelected = { _, _ -> },
            getOwnedPlatformCount = { return@PlatformSelectionContent 0 },
            navigateToGenreScreen = {})
    }
}

@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PlatformSelectionScreenListState() {
    GameTrackerTheme {
        PlatformSelectionContent(
            isLoading = false,
            platformList = listOf(Platform(0, "xbox", "xbox series x", 1080, 1080, "pleu", false)),
            onPlatformSelected = { _, _ -> },
            getOwnedPlatformCount = { return@PlatformSelectionContent 1 },
            navigateToGenreScreen = {})
    }
}
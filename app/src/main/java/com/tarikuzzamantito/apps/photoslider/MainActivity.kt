package com.tarikuzzamantito.apps.photoslider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tarikuzzamantito.apps.photoslider.ui.theme.ComposePhotoSliderTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePhotoSliderTheme {
                ImageSlider(
                    // These are high-resulation images and may take a moment to load
                    // Plaease use your images URLs.
                    images = listOf(
                        "https://images.pexels.com/photos/14023050/pexels-photo-14023050.jpeg",
                        "https://images.pexels.com/photos/18386191/pexels-photo-18386191/free-photo-of-massive.jpeg",
                        "https://images.pexels.com/photos/6552211/pexels-photo-6552211.jpeg",
                        "https://images.pexels.com/photos/4858545/pexels-photo-4858545.jpeg",
                        "https://images.pexels.com/photos/13572587/pexels-photo-13572587.jpeg",
                        "https://images.pexels.com/photos/1659438/pexels-photo-1659438.jpeg",
                        "https://images.pexels.com/photos/16918440/pexels-photo-16918440/free-photo-of-vertigo.jpeg",
                        "https://images.pexels.com/photos/25398551/pexels-photo-25398551/free-photo-of-rocks-on-cliff-on-seashore.jpeg",
                        "https://images.pexels.com/photos/4959951/pexels-photo-4959951.jpeg",
                    )
                )
            }
        }
    }
}

@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(
        pageCount = { images.size }, initialPage = 0
    )
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    coroutineScope.launch {
                        if (dragAmount > 0) {
                            pagerState.animateScrollToPage(
                                (pagerState.currentPage - 1).coerceAtLeast(0)
                            )
                        } else {
                            pagerState.animateScrollToPage(
                                (pagerState.currentPage + 1).coerceAtMost(images.size - 1)
                            )
                        }
                    }
                }
            }, contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 60.dp),
            pageSpacing = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = 1f - (0.2f * abs(pageOffset))
            var isLoading by remember { mutableStateOf(true) }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            ) {
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false },
                    modifier = Modifier
                        .size(450.dp)
                        .border(
                            width = 0.5.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clip(RoundedCornerShape(24.dp))
                )
                if (isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }

}
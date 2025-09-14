package com.fardeen.intervueai

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenRoot(onclick: () -> Unit) {

    val darkmode = isSystemInDarkTheme()
    val coroutineScope = rememberCoroutineScope()

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500) // Optional: small delay before showing
        isVisible = true
    }

    Scaffold(

        floatingActionButton = {

            AnimatedVisibility(
                isVisible,
                enter = slideInHorizontally(initialOffsetX = { it }),
                exit = slideOutHorizontally(targetOffsetX = { it })
            ) {

                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            isVisible = false
                            delay(300) // Delay before navigating
                            onclick()
                        }
                    },
                    shape = MaterialShapes.Cookie7Sided.toShape(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Image(
                        painter = painterResource(R.drawable.outline_arrow_forward_24),
                        contentDescription = "Localized description",
                        colorFilter = ColorFilter.tint(if (darkmode) Color.White else Color.Black)
                    )
                }
            }

        }

    ) { innerPadding ->


        HomeScreenContent(innerPadding, isVisible)
    }

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenContent(innerPadding: PaddingValues, animatedVisibility: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("ai_loader.json"))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .clip(MaterialShapes.Clover4Leaf.toShape())
        ) {


            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }


        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(.5f)) {

            AnimatedVisibility(
                animatedVisibility,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {


                Column {

                    Text(
                        "Hello There", style = TextStyle(
                            fontStyle = FontStyle.Normal,
                            fontWeight = FontWeight.W700,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier.padding(16.dp)
                    )


                    Text(
                        "Lets get started", style = TextStyle(
                            fontStyle = FontStyle.Normal,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier.padding(16.dp)
                    )

                }


            }


        }
    }


}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreenRoot { }
}
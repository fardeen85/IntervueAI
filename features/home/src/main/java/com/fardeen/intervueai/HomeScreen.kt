package com.fardeen.intervueai

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(onclick:()->Unit){

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("ai_loader.json"))
    val progress by animateLottieCompositionAsState(composition, iterations =   LottieConstants.IterateForever)

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = { /* do something */ }, shape = MaterialShapes.Cookie7Sided.toShape()) {
                Image(painter = painterResource(R.drawable.outline_arrow_forward_24), contentDescription = "Localized description")
            }
        }

    ){ innerPadding->

        Column(modifier = Modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally) {




            Box(modifier = Modifier.fillMaxWidth().weight(2f).clip(MaterialShapes.Clover4Leaf.toShape())) {


                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                        .align(Alignment.Center)
                )
            }


            Column(modifier = Modifier.fillMaxWidth().weight(.5f)) {


                Text("Hello There", style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W700,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(16.dp))


                Text("Lets get started", style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(16.dp))

            }


        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    HomeScreen {  }
}
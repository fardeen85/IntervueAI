package com.fardeen.intervueai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun selectTopicScreen(onClick:()->Unit){

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        Box{

            Text("Write which topic you want to start practicing", style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W700,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp)
            )

        }






    }
}

@Composable
@Preview(showBackground = true)
fun SelectTopicScreen(){
    selectTopicScreen {  }

}

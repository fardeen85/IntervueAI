package com.fardeen.intervueai.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fardeen.intervueai.R

@Composable
fun BottomChatBar(){

    Card(
        shape = RoundedCornerShape(90.dp),
    ){

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier  = Modifier.background(Color.Blue).padding(8.dp)
        ){

            Image(modifier = Modifier.weight(0.5f),painter = painterResource(R.drawable.outline_add_comment_24), contentDescription = "add")
            Image(modifier = Modifier.weight(0.5f),painter = painterResource(R.drawable.outline_voice_chat_24), contentDescription = "voice")
            Card(modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(100.dp)){

                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Type Someting") }

                )
            }



        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewBottomChatBar(){
    BottomChatBar()

}
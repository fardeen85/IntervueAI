package com.fardeen.intervueai

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fardeen.intervueai.components.GradientButton
import com.fardeen.intevueai.model.RequestState
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SelectTopicScreenRoot(selectTopicViewModel: SelectTopicViewModel= koinViewModel<SelectTopicViewModel>(), onClick:()->Unit){

    val windowinfo = rememberWindowInfo()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current


    val resultState  by  selectTopicViewModel.verifyTopic.collectAsStateWithLifecycle()

    var isloading = resultState is RequestState.Loading
    var errorMessage by remember { mutableStateOf("") }
    val state = resultState




    LaunchedEffect(key1 = state) {
        when(state){

            is RequestState.Success -> {
                //navigate
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                onClick()
            }
            is RequestState.Error -> {

                showBottomSheet = true
                errorMessage = state.message.ifEmpty { "Something went wrong" }
                Log.d("TAG",errorMessage)


            }
            is RequestState.Loading -> {

                Log.d("TAG","loading Screen")
            }
            null -> {}
        }


    }


    BottomSheetDialog(
        showBottomSheet,
        errorMessage,
        onRetru = {
            showBottomSheet = false
            Log.d("TAG","retry clicked")
            // Retry logic
        },
        onOK = {
            showBottomSheet = false
            Log.d("TAG","ok clicked")
            // OK logic
        },
        onDismiss = {
            showBottomSheet = false
            Log.d("TAG","dismiss clicked")
            // Dismiss logic
        }

    )

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {


        when(windowinfo.screenWidthInfo){
            WindowType.Compact -> SelectTopicScreenContent(selectTopicViewModel,isloading)
            WindowType.Medium -> SelectTopicScreenContent(selectTopicViewModel,isloading)
            WindowType.Expanded -> SelectTopicScreenContentLarge(selectTopicViewModel,isloading)
        }


    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectTopicScreenContent(
    selectTopicViewModel: SelectTopicViewModel,
    isloading: Boolean
){

    var textfieldValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var context = LocalContext.current



    val imagesizeTransform by animateFloatAsState(
        targetValue = if (expanded) 1.5f else 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // Start animation on first appearance
    LaunchedEffect(Unit) {
        expanded = true
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){


        Image(painter = painterResource(com.fardeen.intervueai.select_topic.R.drawable.undraw_interview_yz52),
            modifier = Modifier
                .weight(imagesizeTransform)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .animateContentSize() ,
            contentDescription = "Localized description",
            contentScale = ContentScale.Fit
        )


        Column(modifier = Modifier.weight(1f)) {

            Text("Write which topic you want to start practicing", style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W700,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = textfieldValue,
                onValueChange = { textfieldValue = it },
                label = { Text("Enter Topic") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // padding outside the text field
                shape = RoundedCornerShape(20.dp) // rounded corners
            )



            Spacer(modifier = Modifier.height(16.dp))


            if(!isloading) {

                GradientButton(onClick = {


                    if (textfieldValue.isNotEmpty()) {

                        selectTopicViewModel.verifyTopicByGemini(textfieldValue)
                       // selectTopicViewModel.f()


                    }

                    else{

                        Toast.makeText(context, "Please enter a topic", Toast.LENGTH_SHORT).show()
                    }



                                         },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    text = "Start Practicing"
                )
            }
            else{

                Box(modifier = Modifier.fillMaxWidth()) {

                    CircularWavyProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectTopicScreenContentLarge(selectTopicViewModel: SelectTopicViewModel,isloading:Boolean){

    var textfieldValue by remember { mutableStateOf("") }
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){


        Image(painter = painterResource(com.fardeen.intervueai.select_topic.R.drawable.undraw_interview_yz52),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .animateContentSize() ,
            contentDescription = "Localized description",
            contentScale = ContentScale.Fit
        )


        Column(modifier = Modifier.weight(1f)) {

            Text("Write which topic you want to start practicing", style = TextStyle(
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.W700,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            ), modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = textfieldValue,
                onValueChange = { textfieldValue = it },
                label = { Text("Enter Topic") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // padding outside the text field
                shape = RoundedCornerShape(20.dp) // rounded corners
            )



            Spacer(modifier = Modifier.height(16.dp))

            if(!isloading) {

                GradientButton(onClick = {

                    selectTopicViewModel.verifyTopicByGemini(textfieldValue)

                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    text = "Start Practicing"
                )
            }
            else{

                Box(modifier = Modifier.fillMaxWidth()) {

                    CircularWavyProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog( showSheet:Boolean,message:String,onRetru: () -> Unit, onOK: () -> Unit,onDismiss:()->Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    if (showSheet) {


    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(message, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { onOK() }, modifier = Modifier.padding(end = 8.dp)) {
                    Text("OK")
                }

                Button(onClick = { onRetru() }, modifier = Modifier.padding(end = 8.dp)) {
                    Text("Retru")
                }

            }
        }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PreviewSelectTopicScreen() {
    SelectTopicScreenRoot {  }

}

@Composable
@TabletPreview
@Preview(showBackground = true)
fun PreviewSelectTopicScreenLarge() {
    SelectTopicScreenRoot { }
}

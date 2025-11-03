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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.core.layout.WindowWidthSizeClass
import com.fardeen.intervueai.components.GradientButton
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.RequestState
import kotlinx.coroutines.flow.first
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SelectTopicScreenRoot(
    selectTopicViewModel: SelectTopicViewModel = koinViewModel<SelectTopicViewModel>(),
    onClick: () -> Unit,
    onClick1: () -> Unit
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var showBottomSheet by remember { mutableStateOf(false) }
    var showSelectChoiceModel by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current


    val resultState by selectTopicViewModel.verifyTopic.collectAsStateWithLifecycle()

    var isloading = resultState is RequestState.Loading
    var errorMessage by remember { mutableStateOf("") }


    var showContinueButton by remember { mutableStateOf(false) }
    val chatListState by selectTopicViewModel.chatLisingData.collectAsStateWithLifecycle()
    val state1 = chatListState

    LaunchedEffect(Unit) {

        selectTopicViewModel.getChatListingData()
    }

    LaunchedEffect(state1) {

        when (state1) {

            is RequestState.Loading -> {}
            is RequestState.Success<*> -> {
                val data = state1.data as List<ChatsListingModel>
                if (data.isNotEmpty()) {
                    showContinueButton = true
                } else {
                    showContinueButton = false
                }
            }

            is RequestState.Error -> {

                showContinueButton = false
            }

            else -> {

                showContinueButton = false
            }
        }

    }


    val combinedState by selectTopicViewModel.combinedState.collectAsStateWithLifecycle()

    LaunchedEffect(combinedState) {
        val gemini = combinedState.geminiResult
        val chatList = combinedState.chatListResult

        when (gemini) {
            is RequestState.Success -> {
                if (gemini.data != null) {

                    val reply: String = gemini.data?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                    if(reply.contains("Y")) {
                        onClick()
                    }
                    else{
                        Toast.makeText(context, "This is not a valid topic Please try again with different topic", Toast.LENGTH_SHORT).show()
                    }

                    selectTopicViewModel.resetCombinedState()
                }
            }

            is RequestState.Error -> {
                showBottomSheet = true
                errorMessage = gemini.message.ifEmpty { "Something went wrong" }
            }

            is RequestState.Loading -> {
                Log.d("TAG", "Loading...")
            }

            else -> Unit
        }
    }



    BottomSheetDialog(
        showBottomSheet,
        errorMessage,
        onRetru = {
            showBottomSheet = false
            Log.d("TAG", "retry clicked")
            // Retry logic
        },
        onOK = {
            showBottomSheet = false
            Log.d("TAG", "ok clicked")
            // OK logic
        },
        onDismiss = {
            showBottomSheet = false
            Log.d("TAG", "dismiss clicked")
            // Dismiss logic
        }

    )

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {


        when (windowSizeClass.windowWidthSizeClass) {
            WindowWidthSizeClass.COMPACT -> SelectTopicScreenContent(
                selectTopicViewModel,
                isloading,
                showContinueButton
            ) {
                onClick1()
            }

            WindowWidthSizeClass.MEDIUM -> SelectTopicScreenContent(
                selectTopicViewModel,
                isloading,
                showContinueButton
            ) {
                onClick1()
            }

            WindowWidthSizeClass.EXPANDED -> SelectTopicScreenContentLarge(
                selectTopicViewModel,
                isloading,
                showContinueButton
            ) {
                onClick1()
            }
        }


    }
}


suspend fun checkIfDataExists(viewModel: SelectTopicViewModel): Boolean {
    val result = viewModel.chatListingData.first() // only take first emission

    return try {
        when (result) {
            is RequestState.Success<List<ChatsListingModel>> -> result.data.isEmpty()
            is RequestState.Error -> false
            is RequestState.Loading -> false
            else -> false
        }
    } catch (e: Exception) {
        return false
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectTopicScreenContent(
    selectTopicViewModel: SelectTopicViewModel,
    isloading: Boolean,
    showContinueButton: Boolean,
    onClick1: () -> Unit
) {

    var textfieldValue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var context = LocalContext.current


    val imagesizeTransform by animateFloatAsState(
        targetValue = if (expanded) 1f else .5f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // Start animation on first appearance
    LaunchedEffect(Unit) {
        expanded = true
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(com.fardeen.intervueai.select_topic.R.drawable.undraw_interview_yz52),
            modifier = Modifier
                .weight(imagesizeTransform)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .animateContentSize(),
            contentDescription = "Localized description",
            contentScale = ContentScale.Fit
        )


        Column(modifier = Modifier.weight(1f)) {

            Text(
                "Write which topic you want to start practicing", style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W700,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(16.dp)
            )

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


            if (!isloading) {

                GradientButton(
                    onClick = {


                        if (textfieldValue.isNotEmpty()) {

                            selectTopicViewModel.verifyTopicByGemini(textfieldValue)
                            // selectTopicViewModel.f()

                        } else {
                            Toast.makeText(context, "Please enter a topic", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    text = "Start Practicing"
                )


                if (showContinueButton) {
                    GradientButton(
                        onClick = { onClick1() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .padding(16.dp),
                        text = "Already have interviews left"
                    )
                }

            } else {

                Box(modifier = Modifier.fillMaxWidth()) {

                    CircularWavyProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectTopicScreenContentLarge(
    selectTopicViewModel: SelectTopicViewModel,
    isloading: Boolean,
    showContinueButton: Boolean,
    onClick1: () -> Unit
) {

    var textfieldValue by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {


        Image(
            painter = painterResource(com.fardeen.intervueai.select_topic.R.drawable.undraw_interview_yz52),
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .animateContentSize(),
            contentDescription = "Localized description",
            contentScale = ContentScale.Fit
        )


        Column(modifier = Modifier.weight(1f)) {

            Text(
                "Write which topic you want to start practicing", style = TextStyle(
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.W700,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(16.dp)
            )

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

            if (!isloading) {

                GradientButton(
                    onClick = {

                        selectTopicViewModel.verifyTopicByGemini(textfieldValue)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    text = "Start Practicing"
                )


                if (showContinueButton) {
                    GradientButton(
                        onClick = { onClick1() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .padding(16.dp),
                        text = "Already have interviews left"
                    )
                }
            } else {

                Box(modifier = Modifier.fillMaxWidth()) {

                    CircularWavyProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialog(
    showSheet: Boolean,
    message: String,
    onRetru: () -> Unit,
    onOK: () -> Unit,
    onDismiss: () -> Unit
) {
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
    SelectTopicScreenRoot(onClick = {}, onClick1 = {})

}

@Composable
@TabletPreview
@Preview(showBackground = true)
fun PreviewSelectTopicScreenLarge() {
    SelectTopicScreenRoot(onClick = {}, onClick1 = {})
}

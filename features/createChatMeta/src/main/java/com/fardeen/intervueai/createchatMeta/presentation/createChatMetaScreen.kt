package com.fardeen.intervueai.createchatMeta.presentation

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
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
import androidx.window.core.layout.WindowWidthSizeClass
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.RequestState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateChatMetaScreenRoot(onClick: () -> Unit) {

    val viewModel : createChatMetaViewModel = koinViewModel()
    var title = remember { mutableStateOf("") }
    var description = remember { mutableStateOf("") }
    val chatDataSaveState  by  viewModel.addChatListingData.collectAsStateWithLifecycle(initialValue = "error")
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val state = chatDataSaveState

    // 👇 This ensures Snackbar shows whenever the errorMessage changes
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }


    LaunchedEffect(chatDataSaveState) {
        snapshotFlow { state }
            .collect { currentState ->
                when (currentState) {
                    is RequestState.Loading -> {
                        isLoading = true
                    }

                    is RequestState.Success<*> -> {
                        val result = currentState.data as? String
                        if (result.equals("success", ignoreCase = true)) {
                            onClick()
                            viewModel.clearState()
                        }
                    }

                    is RequestState.Error -> {
                        errorMessage = currentState.message
                    }

                    else -> {
                        errorMessage = "Something went wrong"
                    }
                }
            }
    }



    /*
        LaunchedEffect(state) {

            when(state){

                is RequestState.Loading -> {
                    isLoading = true
                }
                is RequestState.Success<*> -> {

                   if ( (state.data as String).lowercase().equals("success")){
                       onClick()
                       viewModel.clearState()
                   }

                }
                is RequestState.Error -> {
                    val error = state.message
                    errorMessage = error
                }
                else -> {


                    errorMessage = "something went wrong"
                }

            }

        }*/



    Scaffold(
        topBar = { HeaderSection(onClick) },
        bottomBar = { BottomSection(title,description,viewModel,isLoading) },
        snackbarHost = {

            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {

            MainSection(title, description)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderSection(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Create Chat",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        },

        actions = {
            /*Image(onClick = onClick,imageVector = Icons.Default.Close, contentDescription = "Close")*/
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSection(title: MutableState<String>, description: MutableState<String>) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    when(windowSizeClass.windowWidthSizeClass){

         WindowWidthSizeClass.COMPACT -> {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Chat Title") },
                    placeholder = { Text("Enter chat title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    placeholder = { Text("Enter short description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 4
                )
            }
        }
         WindowWidthSizeClass.MEDIUM ->   {


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Chat Title") },
                    placeholder = { Text("Enter chat title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    placeholder = { Text("Enter short description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 4
                )
            }
        }
         WindowWidthSizeClass.EXPANDED ->  {


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = { Text("Chat Title") },
                    placeholder = { Text("Enter chat title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Description") },
                    placeholder = { Text("Enter short description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 4
                )
            }
        }
    }



}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BottomSection(title:MutableState<String>, description:MutableState<String>, viewModel: createChatMetaViewModel,isLoading: Boolean) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            if(!isLoading) {
                Button(
                    onClick = {
                        viewModel.saveChatListingData(
                            ChatsListingModel(
                                title = title.value,
                                description = description.value
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Create Chat",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            else{
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    CircularWavyProgressIndicator()
                }
            }
        }
    }
}

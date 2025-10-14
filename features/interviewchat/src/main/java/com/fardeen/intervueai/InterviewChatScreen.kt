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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource // For dummy images
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.navigation.NavigableSupportingPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fardeen.intervueai.interviewchat.R

data class Message(
    val content: String,
    val isFromAI: Boolean,
    val senderName: String = ""
)

enum class WindowType {
    Compact,
    Medium,
    Expanded
}

data class WindowInfo(
    val screenWidthInfo: WindowType
)


sealed interface DiscussionPane {
    data object Main : DiscussionPane
    data object ChatListing : DiscussionPane
    //data class ChatDetail( chatId: String) : DiscussionPane
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun interviwChatRootScreen(){

    val navController = rememberSupportingPaneScaffoldNavigator<DiscussionPane>()
    val scope = rememberCoroutineScope()
    NavigableSupportingPaneScaffold(

        navigator = navController,
        supportingPane = {

            SupportingPane()
        },
        mainPane = {

            AnimatedPane(
                modifier = Modifier
                    .safeContentPadding()

            ){
                /*if (navController.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                    MainPane()
                }*/
                MainPane(){

                    if (navController.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden){

                        scope.launch {
                            navController.navigateTo(SupportingPaneScaffoldRole.Supporting)
                        }

                    }
                }
            }
        },

    )
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldPaneScope.MainPane(onclick:()-> Unit){

    val messages = remember {
        listOf(
            Message("For sure! Who was the artist we listened to in the taxi?", true, "Gemini"),
            Message("Odesza! One of my faves", false),
            Message("We've got that group playlist!", false),
            Message("Hang tight! I'm making a shared album now for everything", true, "Gemini"),
            Message("Oh I've got some bangers", true, "Gemini"),
            Message("Post them up!", false),
            Message("I got some nice shots too", true, "Gemini")
        )
    }
    var messageText by remember { mutableStateOf("") }

    // Main chat area
    Scaffold (

        topBar = {
            ChatHeaderLarge(){
                onclick()
            }
        },

        bottomBar = {
            MessageInput(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendMessage = { messageText = "" }
            )
        }
    ) { innerPadding->


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            items(messages.take(3)) { message ->
                MessageItemLarge(message = message)
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NICE",
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        modifier = Modifier.rotate(-12f)
                    )
                }
            }

            items(messages.drop(3)) { message ->
                MessageItemLarge(message = message)
            }
        }


    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldPaneScope.SupportingPane(){

    // Sidebar for large screens
    Surface(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight(),
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusBar()

            Text(
                text = "Recent Chats",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            repeat(3) { index ->
                Surface(
                    color = if (index == 0) Color.White.copy(alpha = 0.2f)
                    else Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF4285F4),
                                            Color(0xFF9C27B0),
                                            Color(0xFFE91E63)
                                        )
                                    )
                                )
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawGeminiStar(this)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = if (index == 0) "Music night out ✨" else "Chat ${index + 1}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Last message preview...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun MessagingScreenWithWindowSize(windowInfo: WindowInfo) {
    interviwChatRootScreen()
}

@Composable
fun MessagingScreenContent() {
    val messages = remember {
        listOf(
            Message("For sure! Who was the artist we listened to in the taxi?", true, "Gemini"),
            Message("Odesza! One of my faves", false),
            Message("We've got that group playlist!", false),
            Message("Hang tight! I'm making a shared album now for everything", true, "Gemini"),
            Message("Oh I've got some bangers", true, "Gemini"),
            Message("Post them up!", false),
            Message("I got some nice shots too", true, "Gemini")
        )
    }

    var messageText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9C4DCD),
                        Color(0xFF7B1FA2),
                        Color(0xFF6A1B9A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StatusBar()
            ChatHeader()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding( 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages.take(3)) { message ->
                    MessageItem(message = message)
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NICE",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.rotate(-12f)
                        )
                    }
                }

                items(messages.drop(3)) { message ->
                    MessageItem(message = message)
                }
            }

            MessageInput(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                onSendMessage = { messageText = "" }
            )
        }

        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color(0xFF6A1B9A)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_24),
                contentDescription = "Play",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun MessagingScreenContentLarge() {
    val messages = remember {
        listOf(
            Message("For sure! Who was the artist we listened to in the taxi?", true, "Gemini"),
            Message("Odesza! One of my faves", false),
            Message("We've got that group playlist!", false),
            Message("Hang tight! I'm making a shared album now for everything", true, "Gemini"),
            Message("Oh I've got some bangers", true, "Gemini"),
            Message("Post them up!", false),
            Message("I got some nice shots too", true, "Gemini")
        )
    }

    var messageText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9C4DCD),
                        Color(0xFF7B1FA2),
                        Color(0xFF6A1B9A)
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {



        }

        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color(0xFF6A1B9A)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_24),
                contentDescription = "Play",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun StatusBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "9:30",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height((8 + index * 2).dp)
                        .background(Color.Black, RoundedCornerShape(1.dp))
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(
                modifier = Modifier
                    .width(24.dp)
                    .height(12.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(
                        color = Color.Black,
                        size = size,
                        cornerRadius = CornerRadius(4f)
                    )
                    drawRoundRect(
                        color = Color.Black,
                        topLeft = Offset(2f, 2f),
                        size = Size(size.width * 0.8f, size.height - 4f),
                        cornerRadius = CornerRadius(2f)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatHeader() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_24),
                contentDescription = "Back",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            GroupAvatarCluster(size = 40.dp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Music night out ✨",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Image(
                    painter = painterResource(R.drawable.baseline_video_camera_back_24),
                    contentDescription = "Video call",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(20.dp)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_video_camera_back_24),
                    contentDescription = "Voice call",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ChatHeaderLarge(onclick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GroupAvatarCluster(size = 48.dp)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Music night out ✨",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "4 participants",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    painter = painterResource(R.drawable.baseline_video_camera_back_24),
                    contentDescription = "Video call",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(24.dp).clickable{
                        onclick()
                    }
                )
                Image(
                    painter = painterResource(R.drawable.baseline_call_24),
                    contentDescription = "Voice call",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(24.dp)
                )
                Image(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription = "More options",
                    colorFilter = ColorFilter.tint(Color.Black),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GroupAvatarCluster(size: Dp) {
    Box(modifier = Modifier.size(size)) {
        Box(
            modifier = Modifier
                .size(size * 0.6f)
                .clip(CircleShape)
                .background(Color(0xFF6A1B9A))
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(size * 0.5f)
                .clip(CircleShape)
                .background(Color(0xFFFF9800))
                .align(Alignment.TopEnd)
        )
        Box(
            modifier = Modifier
                .size(size * 0.45f)
                .clip(CircleShape)
                .background(Color(0xFF2196F3))
                .align(Alignment.BottomEnd)
        )
        Box(
            modifier = Modifier
                .size(size * 0.4f)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50))
                .align(Alignment.BottomStart)
        )
    }
}

@Composable
fun MessageItem(message: Message) {
    if (message.isFromAI) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            GeminiAvatar(size = 32.dp)
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = message.senderName,
                    fontSize = 10.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    modifier = Modifier.widthIn(max = 280.dp)
                ) {
                    Text(
                        text = message.content,
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                color = Color(0xFF2196F3),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 4.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Text(
                    text = message.content,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun MessageItemLarge(message: Message) {
    if (message.isFromAI) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            GeminiAvatar(size = 40.dp)
            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = message.senderName,
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    ),
                    modifier = Modifier.widthIn(max = 400.dp)
                ) {
                    Text(
                        text = message.content,
                        fontSize = 16.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Surface(
                color = Color(0xFF2196F3),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 6.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                modifier = Modifier.widthIn(max = 400.dp)
            ) {
                Text(
                    text = message.content,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun GeminiAvatar(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4285F4),
                        Color(0xFF9C27B0),
                        Color(0xFFE91E63)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.5f)) {
            drawGeminiStar(this)
        }
    }
}

@Composable
fun MessageInput(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.White,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_attach_file_24),
                contentDescription = "Attach file",
                colorFilter = ColorFilter.tint(Color(0xFF2196F3)),
                modifier = Modifier.size(24.dp)
            )

            Image(
                painter = painterResource(R.drawable.baseline_image_24),
                contentDescription = "Image",
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(24.dp)
            )

            TextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                placeholder = { Text("Message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(R.drawable.baseline_mic_24),
                contentDescription = "Microphone",
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(20.dp)
            )

            IconButton(
                onClick = onSendMessage,
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF2196F3), CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_send_24),
                    contentDescription = "Send",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun MessageInputLarge(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        color = Color.White,
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_attach_file_24),
                contentDescription = "Attach file",
                colorFilter = ColorFilter.tint(Color(0xFF2196F3)),
                modifier = Modifier.size(28.dp)
            )

            Image(
                painter = painterResource(R.drawable.baseline_image_24),
                contentDescription = "Image",
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(28.dp)
            )

            TextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                placeholder = { Text("Message...", color = Color.Gray, fontSize = 16.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )

            Image(
                painter = painterResource(R.drawable.baseline_mic_24),
                contentDescription = "Microphone",
                colorFilter = ColorFilter.tint(Color.Gray),
                modifier = Modifier.size(24.dp)
            )

            IconButton(
                onClick = onSendMessage,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF2196F3), CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = "Send",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun drawGeminiStar(drawScope: DrawScope) {
    val path = Path()
    val size = drawScope.size
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 4

    for (i in 0 until 8) {
        val angle = (i * 45f - 90f) * (Math.PI / 180f).toFloat()
        val r = if (i % 2 == 0) radius * 1.5f else radius * 0.7f
        val x = centerX + r * cos(angle)
        val y = centerY + r * sin(angle)

        if (i == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()

    drawScope.drawPath(
        path = path,
        color = Color.White
    )
}

// Previews
@Preview(name = "Compact Screen", widthDp = 360, heightDp = 640)
@Composable
fun MessagingScreenCompactPreview() {
    MaterialTheme {
        MessagingScreenWithWindowSize(
            WindowInfo(screenWidthInfo = WindowType.Compact)
        )
    }
}

@Preview(name = "Medium Screen", widthDp = 600, heightDp = 800)
@Composable
fun MessagingScreenMediumPreview() {
    MaterialTheme {
        MessagingScreenWithWindowSize(
            WindowInfo(screenWidthInfo = WindowType.Medium)
        )
    }
}

@Preview(name = "Large Screen", widthDp = 900, heightDp = 600)
@Composable
fun MessagingScreenLargePreview() {
    MaterialTheme {
        MessagingScreenWithWindowSize(
            WindowInfo(screenWidthInfo = WindowType.Expanded)
        )
    }
}

@Preview(name = "Tablet Portrait", widthDp = 768, heightDp = 1024)
@Composable
fun MessagingScreenTabletPortraitPreview() {
    MaterialTheme {
        MessagingScreenWithWindowSize(
            WindowInfo(screenWidthInfo = WindowType.Expanded)
        )
    }
}

@Preview(name = "Foldable Unfolded", widthDp = 840, heightDp = 900)
@Composable
fun MessagingScreenFoldablePreview() {
    MaterialTheme {
        MessagingScreenWithWindowSize(
            WindowInfo(screenWidthInfo = WindowType.Expanded)
        )
    }
}
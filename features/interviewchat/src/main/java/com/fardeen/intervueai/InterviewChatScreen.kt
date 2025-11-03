package com.fardeen.intervueai

import android.util.Log
import com.fardeen.intevueai.model.Message
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.cos
import kotlin.math.sin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.fardeen.intervueai.interviewchat.R
import com.fardeen.intevueai.model.ChatsListingModel
import com.fardeen.intevueai.model.GeminiResponseModel
import com.fardeen.intevueai.model.RequestState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel


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
fun InterviewChatRootScreen(chatId: String?, onback: () -> Unit) {

    val navController = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    val viewModel: InterviewChatViewModel = koinViewModel()
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState()
    val snackbarHostState = SnackbarHostState()
    val isDialogLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val isListVisible =
        navController.scaffoldValue[ListDetailPaneScaffoldRole.List] != PaneAdaptedValue.Hidden
    val isDetailVisible =
        navController.scaffoldValue[ListDetailPaneScaffoldRole.Detail] != PaneAdaptedValue.Hidden
    val isExpanded = isListVisible && isDetailVisible
    val isCompact = !isExpanded



    LaunchedEffect(Unit) {
        viewModel.loadChatList()
        chatId?.let {
            val longId = it.toInt()
            viewModel.updateSelectedChatId(longId)
        }
        viewModel.loadMessages()


    }



    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.Snackbar.Show -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is UiEvent.Navigation.To -> {}

                UiEvent.Navigation.Back -> {}

                UiEvent.Dialog.ShowLoading -> {}
                UiEvent.Dialog.HideLoading -> {}
                UiEvent.Dialog.ShowSuccess -> showDialog = true
                UiEvent.Dialog.Hide -> showDialog = false

                is UiEvent.Toast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // Main scaffold
    ListDetailPaneScaffold(
        directive = PaneScaffoldDirective.Default,
        value = navController.scaffoldValue,


        listPane = {
            SupportingPane(state.value.chatList, state.value.selectedChatId ?: 1, isExpanded, onLongClick = {

                viewModel.sendShowDialogEvent()
            }, onclick = {

                Log.d("TAG", "Selected ID ${it}")
                viewModel.updateSelectedChatId(it.id)
                viewModel.updateChatName(it.title ?: "")
                viewModel.loadMessages()
                scope.launch {
                    navController.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            })
        },

        detailPane = {
            AnimatedPane(
                modifier = Modifier.safeContentPadding()
            ) {
                MainPane(
                    chats = state.value.chats,
                    geminiResponse = state.value.geminiResponse,
                    title = state.value.chatName ?: "",
                    onclick = {},
                    onback = {
                        if (navController.scaffoldValue[ListDetailPaneScaffoldRole.Detail] != PaneAdaptedValue.Hidden) {


                            scope.launch {
                                navController.navigateTo(ListDetailPaneScaffoldRole.List)
                            }


                        } else {
                            onback()
                        }
                    },

                    sendClick = {
                        viewModel.sendMessage(it)
                    },

                    loadInitial = {
                        if (it) {
                            viewModel.sendMessage("Hi i am here to practice interview questions of ${state.value.chatName}")
                        }


                    }
                )
            }
        }

    )

    DeleteConfirmationDialog(showDialog = showDialog, onConfirmDelete = {
        showDialog = false
        viewModel.deleteChatListItem()
    }, onDismiss = {
        showDialog = false
    })


}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldPaneScope.MainPane(
    title: String,
    chats: RequestState<List<Message>>,
    geminiResponse: RequestState<GeminiResponseModel?>,
    sendClick: (message: String) -> Unit,
    onclick: () -> Unit,
    onback: () -> Unit,
    loadInitial: (b: Boolean) -> Unit
) {


    var geminiSendLoading by remember { mutableStateOf(false) }

    var messageText by remember { mutableStateOf("") }

    var messages by remember {
        mutableStateOf(emptyList<Message>())
    }

    val listState = rememberLazyListState()



    LaunchedEffect(chats) {

        when (chats) {
            is RequestState.Error -> {

                val error = (chats as RequestState.Error).message
                Log.d("TAG", error)
            }

            RequestState.Idl -> {

            }

            RequestState.Loading -> {

            }

            is RequestState.Success<*> -> {

                messages = (chats as RequestState.Success<List<Message>>).data
                Log.d("TAG", messages.toString())
                loadInitial(messages.isEmpty())
                // Scroll to the last item only if the list is not empty
                if (messages.isNotEmpty()) {
                    listState.animateScrollToItem(messages.lastIndex)
                }
            }
        }

    }


    LaunchedEffect(geminiResponse) {

        when (geminiResponse) {
            is RequestState.Error -> {

                geminiSendLoading = false
            }

            is RequestState.Idl -> {}
            is RequestState.Loading -> {

                geminiSendLoading = true
            }

            is RequestState.Success<*> -> {

                geminiSendLoading = false
                val data = (geminiResponse as RequestState.Success<GeminiResponseModel?>).data


            }

        }
    }



    BackHandler() {

        onback()
    }

    // Main chat area
    Scaffold(

        topBar = {
            chatHeader(title) {
                onclick()
            }
        },

        bottomBar = {
            MessageInput(
                messageText = messageText,
                onMessageTextChange = { messageText = it },
                loading = geminiSendLoading,
                onSendMessage = {
                    sendClick(messageText)
                    messageText = ""

                }
            )
        }
    ) { innerPadding ->


        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {

            if (messages.isNotEmpty()) {

                items(messages.take(3)) { message ->
                    MessageItem(message = message)
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
                    MessageItem(message = message)
                }


            } else {

                item {


                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

                        Text("No Messages Found")
                    }
                }
            }


        }


    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ThreePaneScaffoldPaneScope.SupportingPane(
    chatListData: RequestState<List<ChatsListingModel>>,
    selectedId: Int,
    isExpanded: Boolean,
    onclick: (ChatsListingModel) -> Unit,
    onLongClick: (ChatsListingModel) -> Unit

    ) {


    when (chatListData) {
        is RequestState.Loading -> {
            LoadingView()
        }

        is RequestState.Error -> {

            ErrorView((chatListData as RequestState.Error).message)
        }

        is RequestState.Success<*> -> {

            val chats = (chatListData as RequestState.Success<List<ChatsListingModel>>).data
            ChatListScreen(chats, selectedId, isExpanded, onChatClick ={ it->onclick(it)}, onLongClick = {it->onLongClick(it)})
        }

        RequestState.Idl -> {}
        else -> {}
    }


}

@Composable
fun ErrorView(error: String) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Text("Oops!" + error)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingView() {


    LazyColumn(modifier = Modifier.fillMaxSize()) {

        // show 5 shimmer placeholders
        items(5) {
            ChatListItem(chat = null, isLoading = true)
        }
    }

}


@Composable
fun ChatListScreen(
    chats: List<ChatsListingModel>?,
    selectedId: Int,
    isExpanded: Boolean,
    onChatClick: (ChatsListingModel) -> Unit,
    onLongClick: (ChatsListingModel) -> Unit
) {

    val list = chats ?: emptyList()
    Scaffold { innerPadding ->


        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {

            itemsIndexed(list) { index, chat ->

                val showBorder = if (chat.id == selectedId && isExpanded) true else false
                ChatListItem(chat = chat, onClick = onChatClick, showBorder = showBorder, onLongClick = onLongClick)
            }
            /*    // show 5 shimmer placeholders
                items(5) {
                    ChatListItem(chat = null, isLoading = true)
                }*/


        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListItem(
    chat: ChatsListingModel?,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    showBorder: Boolean = false,
    onClick: (ChatsListingModel) -> Unit = {},
    onLongClick: (ChatsListingModel) -> Unit = {},
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    if (showBorder) 2.dp else 0.dp,
                    if (showBorder) Color.Blue else Color.White
                )
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .combinedClickable(
                enabled = !isLoading && chat != null,
                onClick = { chat?.let { onClick(it) } },
                onLongClick = { chat?.let { onLongClick(it) } }
            ),
        headlineContent = {
            Text(
                text = chat?.title ?: "",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.placeholder(
                    visible = isLoading,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
        },
        supportingContent = {
            Text(
                text = chat?.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.placeholder(
                    visible = isLoading,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .placeholder(
                        visible = isLoading,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.gemini),
                    contentDescription = ""
                )
            }
        },

        shadowElevation = 3.dp
    )
}


@Composable
fun MessagingScreenWithWindowSize(windowInfo: WindowInfo) {
    InterviewChatRootScreen("", onback = {})
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
fun chatHeader(title: String, onclick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GroupAvatarCluster(size = 48.dp)

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessageInput(
    messageText: String,
    loading: Boolean,
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


            TextField(
                value = messageText,
                onValueChange = onMessageTextChange,
                placeholder = { Text("write a message...", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
            )




            if (loading) {

                CircularWavyProgressIndicator()

            } else {
                IconButton(
                    onClick = { onSendMessage() },
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
                placeholder = {
                    Text(
                        "com.fardeen.intevueai.model.Message...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                },
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

@Composable
fun DeleteConfirmationDialog(
    showDialog: Boolean,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Delete Chat?")
            },
            text = {
                Text("Are you sure you want to delete this chat? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

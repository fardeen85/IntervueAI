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
import androidx.compose.foundation.shape.RoundedCornerShape
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

// --- Data Classes (Simplified) ---
data class User(val id: String, val name: String, val avatarResId: Int) // Use painterResource for avatar

data class Message(
    val id: String,
    val user: User,
    val text: String,
    val timestamp: String, // For simplicity
    val isMine: Boolean
)

// --- Dummy Data ---
val dummyUser1 = User("user1", "James", R.drawable.ic_dummy_avatar_1) // Replace with your drawable
val dummyUser2 = User("user2", "Odette", R.drawable.ic_dummy_avatar_1)
val dummyUser3 = User("user3", "Aisha", R.drawable.ic_dummy_avatar_1)
val dummyUser4 = User("user4", "Josefina", R.drawable.ic_dummy_avatar_1)
val dummyUser5 = User("user5", "Elie", R.drawable.ic_dummy_avatar_1)
val currentUser = User("currentUser", "Me", R.drawable.ic_dummy_avatar_current_user)

val dummyMessages = listOf(
    Message("1", dummyUser1, "For sure! Who was the artist we listened to in the taxi?", "9:30 AM", false),
    Message("2", dummyUser2, "Odette! One of my faves", "9:31 AM", false),
    Message("3", dummyUser2, "Who's got that group photo?", "9:31 AM", false),
    Message("4", dummyUser3, "Hang tight I'm making a shared album now for everything", "9:32 AM", false),
    Message("5", dummyUser4, "Oh I've got some keepers", "9:33 AM", false),
    Message("6", dummyUser5, "Post them up!", "9:34 AM", false),
    Message("7", currentUser, "I got some nice shots too", "9:35 AM", true),
)

// --- Composable Functions ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var messages by remember { mutableStateOf(dummyMessages.take(4)) } // Start with a few messages
    var showNiceAnimation by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Simulate new messages arriving
    LaunchedEffect(Unit) {
        delay(2000)
        messages = messages + dummyMessages[4]
        listState.animateScrollToItem(messages.size -1)
        delay(1500)
        showNiceAnimation = true // Trigger NICE animation
        messages = messages + dummyMessages[5]
        listState.animateScrollToItem(messages.size -1)
        delay(1000)
        messages = messages + dummyMessages[6]
        listState.animateScrollToItem(messages.size -1)
        delay(2000)
        showNiceAnimation = false // Hide NICE animation
    }


    Scaffold(
        topBar = { ChatTopBar() },
        bottomBar = { ChatInputBar { newMessageText ->
            val newMessage = Message(
                (messages.size + 1).toString(),
                currentUser,
                newMessageText,
                "Now",
                true
            )
            messages = messages + newMessage
            // Scroll to the new message
            LaunchedEffect(messages.size) {
                listState.animateScrollToItem(messages.size - 1)
            }
        } },
        containerColor = Color(0xFFE6E0FF) // Light purple background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(messages, key = { _, message -> message.id }) { index, message ->
                    AnimatedVisibility(
                        visible = true, // Messages are always visible once added
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(durationMillis = 300, delayMillis = 100 * index.coerceAtMost(5))
                        ) + fadeIn(animationSpec = tween(durationMillis = 300)),
                        exit = fadeOut(animationSpec = tween(durationMillis = 100)) // Can be added if messages are removed
                    ) {
                        ChatMessageItem(message = message)
                    }
                }
            }

            // "NICE" Animation Overlay
            AnimatedVisibility(
                visible = showNiceAnimation,
                enter = fadeIn(animationSpec = tween(500)) + scaleIn(animationSpec = tween(500), initialScale = 0.5f),
                exit = fadeOut(animationSpec = tween(500)) + scaleOut(animationSpec = tween(500), targetScale = 0.5f),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = "NICE",
                    fontSize = 120.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00C6FF), // Bright blue
                    style = TextStyle(
                        // Add shadow or other effects if needed
                    )
                )
            }
        }
    }
}

@Composable
fun ChatTopBar() {
    Surface(
        color = Color.Transparent, // Make it blend with the screen background initially
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle back */ }) {
               Image(painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "Back")
            }
            Spacer(Modifier.width(8.dp))
            // Dummy Group Avatars (replace with actual logic)
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 0..3) { // Show a few overlapping avatars
                    Image(
                        painter = painterResource(id = dummyMessages[i % dummyMessages.size].user.avatarResId),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .offset(x = (-8 * i).dp) // Overlap effect
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Music night out", fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text("Group chat", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { /* Handle video call */ }) {
                Image(painterResource(R.drawable.baseline_video_camera_back_24), contentDescription = "Back")

            }
            IconButton(onClick = { /* Handle call */ }) {

                Image(painterResource(R.drawable.baseline_call_24), contentDescription = "Back")

            }
        }
    }
}

@Composable
fun ChatMessageItem(message: Message) {
    val bubbleColor = if (message.isMine) Color(0xFFD0BCFF) else Color.White // Purple for mine, White for others
    val textColor = if (message.isMine) Color.Black else Color.Black
    val horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    val bubbleShape = if (message.isMine) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isMine) {
            Image(
                painter = painterResource(id = message.user.avatarResId),
                contentDescription = message.user.name,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            shape = bubbleShape,
            color = bubbleColor,
            modifier = Modifier.weight(1f, fill = false) // Important for bubble to wrap content
        ) {
            Text(
                text = message.text,
                color = textColor,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 15.sp
            )
        }
        if (message.isMine) {
            // Could add read receipts or timestamp here if needed next to the bubble
        }
    }
}

@Composable
fun ChatInputBar(onSendMessage: (String) -> Unit) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }
    val iconsColor = Color(0xFF007AFF) // Blue for icons

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent // Or a slightly different shade from background
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle add */ }) {

                Image(painterResource(R.drawable.outline_add_comment_24), contentDescription = "Back")

            }
            IconButton(onClick = { /* Handle camera */ }) {

                Image(painterResource(R.drawable.baseline_video_camera_back_24), contentDescription = "Back")

            }
            IconButton(onClick = { /* Handle voice memo */ }) {

                Image(painterResource(R.drawable.baseline_call_24), contentDescription = "Back")

            }

            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (textState.text.isEmpty()) {
                            Text("Message", color = Color.Gray, fontSize = 16.sp)
                        }
                        innerTextField()
                    }
                }
            )

            AnimatedVisibility(
                visible = textState.text.isNotEmpty(),
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
            ) {
                IconButton(onClick = {
                    if (textState.text.isNotBlank()) {
                        onSendMessage(textState.text)
                        textState = TextFieldValue("") // Clear input
                    }
                }) {
                    androidx.compose.material3.Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = iconsColor,
                        modifier = Modifier
                            .background(Color(0xFF00C6FF).copy(alpha = 0.1f), CircleShape) // Light blue circle bg
                            .padding(6.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = textState.text.isEmpty(),
                enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
            ) {
                IconButton(onClick = { /* Handle emoji */ }) {
                    androidx.compose.material3.Icon(Icons.Filled.EmojiEmotions, contentDescription = "Emoji", tint = iconsColor)
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun DefaultChatScreenPreview() {
    MaterialTheme { // Ensure a MaterialTheme is applied for previews
        ChatScreen()
    }
}

// --- Dummy Drawable Resources (Important!) ---
// You need to create these drawable resources in your `res/drawable` folder.
// For example, `ic_dummy_avatar_1.xml` (Vector Drawable or import an image):
/*
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
  <path
      android:fillColor="#FF4081"
      android:pathData="M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z"/>
</vector>
*/
// Create similar dummy drawables:
// R.drawable.ic_dummy_avatar_1
// R.drawable.ic_dummy_avatar_2
// R.drawable.ic_dummy_avatar_3
// R.drawable.ic_dummy_avatar_4
// R.drawable.ic_dummy_avatar_5
// R.drawable.ic_dummy_avatar_current_user
// If you don't have these, painterResource() will crash.
// As a quick placeholder, you can use built-in icons like Icons.Filled.Person
// e.g., painter = rememberVectorPainter(Icons.Filled.Person)
// However, the image shows distinct avatars, so creating placeholders is better.

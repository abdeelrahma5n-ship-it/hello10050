package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.User
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.ChatMessage

@Composable
fun FloatingAiAssistant(
    officeChatMessages: List<ChatMessage> = emptyList(),
    groupChatMessages: List<ChatMessage> = emptyList(),
    chatMessages: List<ChatMessage> = emptyList(), // fallback
    isAiThinking: Boolean = false,
    currentUser: User? = null,
    isGuestMode: Boolean = true,
    isAdminLoggedIn: Boolean = false,
    onSendOfficeMessage: (String) -> Unit = {},
    onSendGroupMessage: (text: String, mediaUrl: String?, mediaType: String?, isAnnouncement: Boolean) -> Unit = { _, _, _, _ -> },
    onSendMessage: (String) -> Unit = {}, // fallback
    onOpenAuthModal: () -> Unit = {},
    isOpenExternally: Boolean = false,
    initialTab: Int = 0,
    onDismissExternal: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isExpandedInternal by remember { mutableStateOf(false) }
    val isExpanded = isExpandedInternal || isOpenExternally

    // Selected Tab: 0 = محادثة المكتب (Private Office Chat), 1 = المحادثة الجماعية (Community Group Chat)
    var selectedTab by remember { mutableStateOf(initialTab) }

    LaunchedEffect(initialTab, isOpenExternally) {
        if (isOpenExternally) {
            selectedTab = initialTab
        }
    }

    var inputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val officeListState = rememberLazyListState()
    val groupListState = rememberLazyListState()

    // Admin role check
    val isAdmin = isAdminLoggedIn || currentUser?.phone == "admin" || currentUser?.phone == "01010634040" || (currentUser?.name?.contains("عبدالرحمن") == true) || (currentUser?.loginMethod == "أدمن")

    // Admin Media Attachment Dialog State
    var showAdminMediaDialog by remember { mutableStateOf(false) }
    var mediaUrlInput by remember { mutableStateOf("") }
    var mediaTypeInput by remember { mutableStateOf("IMAGE") } // "IMAGE" or "VIDEO"
    var isAnnouncementToggle by remember { mutableStateOf(false) }

    val effectiveOfficeMessages = if (officeChatMessages.isNotEmpty()) officeChatMessages else chatMessages
    val effectiveGroupMessages = groupChatMessages

    val officeSuggestions = listOf(
        "متوسط سعر المتر بالكردون 📊",
        "الفرق بين الأرض داخل وخارج الكردون 🏛️",
        "صفحة الفيسبوك والتواصل مع أ/ عبدالرحمن 📘",
        "طلب معاينة عقار 🏠",
        "تسعير أرض أو منزل 📐",
        "عرض عقار للبيع في التطبيق 📝"
    )

    // Auto scroll for office chat
    LaunchedEffect(effectiveOfficeMessages.size, selectedTab) {
        if (selectedTab == 0 && effectiveOfficeMessages.isNotEmpty()) {
            officeListState.animateScrollToItem(effectiveOfficeMessages.size - 1)
        }
    }

    // Auto scroll for group chat
    LaunchedEffect(effectiveGroupMessages.size, selectedTab) {
        if (selectedTab == 1 && effectiveGroupMessages.isNotEmpty()) {
            groupListState.animateScrollToItem(effectiveGroupMessages.size - 1)
        }
    }

    Box(modifier = modifier) {
        // Floating Action Button
        if (!isExpanded) {
            FloatingActionButton(
                onClick = { isExpandedInternal = true },
                containerColor = RoyalGold,
                contentColor = Color.Black,
                shape = CircleShape,
                modifier = Modifier
                    .shadow(10.dp, CircleShape)
                    .border(2.dp, SoftGold, CircleShape)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "المحادثة",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "المحادثة 💬",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Expanded Chat Dialog Window
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.80f)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = DarkEmeraldCard,
                border = BorderStroke(2.dp, RoyalGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Header Bar with Close Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkEmerald, RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SoftGold,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "مركز المحادثات والتواصل 💬",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = SoftGold
                                )
                                Text(
                                    text = "سمسارك بـ أولاد صقر والشرقية",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        IconButton(onClick = {
                            isExpandedInternal = false
                            onDismissExternal()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // DUAL-TAB SEGMENTED CONTROL (تقسيم صفحة المحادثة لتبويبين)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF052218), RoundedCornerShape(16.dp))
                            .border(1.dp, DarkGold, RoundedCornerShape(16.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Tab 1: محادثة المكتب
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedTab == 0) RoyalGold else Color.Transparent)
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.SupportAgent,
                                    contentDescription = null,
                                    tint = if (selectedTab == 0) Color.Black else SoftGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "محادثة المكتب 🏢",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (selectedTab == 0) Color.Black else TextWhite
                                )
                            }
                        }

                        // Tab 2: المحادثة الجماعية
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (selectedTab == 1) RoyalGold else Color.Transparent)
                                .clickable { selectedTab = 1 }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = if (selectedTab == 1) Color.Black else SoftGold,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "المحادثة الجماعية 👥",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (selectedTab == 1) Color.Black else TextWhite
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // CONTENT FOR SELECTED TAB
                    if (selectedTab == 0) {
                        // ==========================================
                        // TAB 1: PRIVATE OFFICE CHAT (محادثة المكتب)
                        // ==========================================
                        
                        // Direct Call & WhatsApp Quick Shortcuts
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkEmerald),
                            border = BorderStroke(1.dp, DarkGold.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "مدير المكتب (أ/ عبدالرحمن):",
                                        fontSize = 11.sp,
                                        color = RoyalGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "للمعاينات الفورية والاستفسارات",
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Phone call
                                    Surface(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                                data = Uri.parse("tel:01010634040")
                                            }
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = RoyalGold
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Call,
                                                contentDescription = "اتصال",
                                                tint = Color.Black,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("01010634040", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    // WhatsApp
                                    Surface(
                                        onClick = {
                                            val waMsg = Uri.encode("أهلاً بك أ/ عبدالرحمن، أريد الاستفسار عن المعاينات المتاحة بالمكتب.")
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://wa.me/201010634040?text=$waMsg")
                                            }
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(0xFF25D366)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("💬", fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("واتساب", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Suggestion Chips
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(officeSuggestions) { hint ->
                                Surface(
                                    onClick = {
                                        if (onSendOfficeMessage != {}) {
                                            onSendOfficeMessage(hint)
                                        } else {
                                            onSendMessage(hint)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = DarkEmerald,
                                    border = BorderStroke(1.dp, DarkGold)
                                ) {
                                    Text(
                                        text = hint,
                                        fontSize = 11.sp,
                                        color = SoftGold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Office Messages List
                        LazyColumn(
                            state = officeListState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(effectiveOfficeMessages) { msg ->
                                val isUser = msg.isUser
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isUser) 16.dp else 2.dp,
                                            bottomEnd = if (isUser) 2.dp else 16.dp
                                        ),
                                        color = if (isUser) RoyalGold else DarkEmerald,
                                        border = if (!isUser) BorderStroke(1.dp, DarkGold) else null,
                                        modifier = Modifier.fillMaxWidth(0.88f)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            if (!isUser) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.SupportAgent,
                                                        contentDescription = null,
                                                        tint = SoftGold,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = msg.senderName.ifBlank { "أ/ عبدالرحمن (مدير المكتب)" },
                                                        color = SoftGold,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }

                                            Text(
                                                text = msg.text,
                                                color = if (isUser) Color.Black else TextWhite,
                                                fontSize = 13.sp,
                                                fontWeight = if (isUser) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    }
                                }
                            }

                            if (isAiThinking) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = SoftGold,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "جاري كتابة الرد من المكتب...",
                                            fontSize = 12.sp,
                                            color = SoftGold
                                        )
                                    }
                                }
                            }
                        }

                        // Office Input Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("أكتب استفسارك للمكتب هنا...", fontSize = 12.sp, color = TextMuted) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedContainerColor = DarkEmerald,
                                    unfocusedContainerColor = DarkEmerald
                                )
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank()) {
                                        if (onSendOfficeMessage != {}) {
                                            onSendOfficeMessage(inputText)
                                        } else {
                                            onSendMessage(inputText)
                                        }
                                        inputText = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(RoyalGold, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "إرسال",
                                    tint = Color.Black
                                )
                            }
                        }

                    } else {
                        // ===============================================
                        // TAB 2: COMMUNITY GROUP CHAT (المحادثة الجماعية)
                        // ===============================================

                        // Group Info Banner
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF082E20)),
                            border = BorderStroke(1.dp, SoftGold.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📢", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("المجموعة الجماعية لمشتركي سمسارك (1,240 عضو)", color = SoftGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        if (isAdmin) "👑 حساب مدير المنصة: يمكنك نشر الصور، الفيديوهات والإعلانات الرسمية."
                                        else "🔒 الصور والفيديوهات مخصصة للإدارة | يتيح للأعضاء كتابة النص فقط لتفادي الإزعاج.",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        // GUEST ACCESS BARRIER (حاجز الدخول للضيوف)
                        if (isGuestMode || currentUser == null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(Color(0xFF041B13), RoundedCornerShape(20.dp))
                                    .border(1.5.dp, RoyalGold, RoundedCornerShape(20.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = RoyalGold,
                                        modifier = Modifier.size(52.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "انضم إلى مجتمع المشتركين 🔒",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SoftGold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "عفواً، يرجى تسجيل الدخول للانضمام إلى المحادثة الجماعية والتواصل مع المكتب",
                                        fontSize = 13.sp,
                                        color = TextWhite,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                    Spacer(modifier = Modifier.height(18.dp))
                                    Button(
                                        onClick = {
                                            isExpandedInternal = false
                                            onDismissExternal()
                                            onOpenAuthModal()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                        shape = RoundedCornerShape(14.dp),
                                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = "تسجيل الدخول / حساب جديد 📲",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        } else {
                            // Group Chat Feed
                            LazyColumn(
                                state = groupListState,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(effectiveGroupMessages) { msg ->
                                    val isMsgAdmin = msg.isAdmin || msg.isManager
                                    
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (msg.isAnnouncement) Color(0xFF0F3A2A)
                                                            else if (isMsgAdmin) Color(0xFF0A2E20)
                                                            else DarkEmerald
                                        ),
                                        border = BorderStroke(
                                            width = if (msg.isAnnouncement) 2.dp else 1.dp,
                                            color = if (msg.isAnnouncement) RoyalGold else DarkGold.copy(alpha = 0.5f)
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            // Message Header (Sender Info & Badge)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = if (isMsgAdmin) RoyalGold else Color(0xFF1B4D3A),
                                                        modifier = Modifier.size(28.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Person,
                                                            contentDescription = null,
                                                            tint = if (isMsgAdmin) Color.Black else TextWhite,
                                                            modifier = Modifier.padding(5.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Column {
                                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                            Text(
                                                                text = msg.senderName,
                                                                color = if (isMsgAdmin) RoyalGold else TextWhite,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 13.sp
                                                            )
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Surface(
                                                                shape = RoundedCornerShape(6.dp),
                                                                color = if (isMsgAdmin) RoyalGold.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f),
                                                                border = BorderStroke(0.5.dp, if (isMsgAdmin) RoyalGold else DarkGold)
                                                            ) {
                                                                Text(
                                                                    text = if (isMsgAdmin) "👑 إدارة المنصة" else "👤 عضو",
                                                                    color = if (isMsgAdmin) RoyalGold else TextMuted,
                                                                    fontSize = 9.sp,
                                                                    fontWeight = FontWeight.Bold,
                                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                Text(
                                                    text = msg.timestampStr,
                                                    color = TextMuted,
                                                    fontSize = 10.sp
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            // Official Announcement Banner
                                            if (msg.isAnnouncement) {
                                                Surface(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 6.dp),
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = RoyalGold,
                                                    contentColor = Color.Black
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Campaign,
                                                            contentDescription = null,
                                                            tint = Color.Black,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Text("📢 إعلان رسمي من الإدارة", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    }
                                                }
                                            }

                                            // Text Content
                                            Text(
                                                text = msg.text,
                                                color = TextWhite,
                                                fontSize = 13.sp
                                            )

                                            // Media Attachment Preview (if any)
                                            if (!msg.mediaUrl.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                if (msg.mediaType == "VIDEO") {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(140.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(Color.Black),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                            Icon(
                                                                imageVector = Icons.Default.Videocam,
                                                                contentDescription = null,
                                                                tint = RoyalGold,
                                                                modifier = Modifier.size(40.dp)
                                                            )
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text("معاينة فيديو العقار ▶️", color = SoftGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                        }
                                                    }
                                                } else {
                                                    AsyncImage(
                                                        model = msg.mediaUrl,
                                                        contentDescription = "صورة مرفقة",
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(160.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .border(1.dp, DarkGold, RoundedCornerShape(12.dp)),
                                                        contentScale = ContentScale.Crop
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Group Input Bar & Media Controls
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // ADMIN MEDIA CONTROLS BAR (Only visible to Admin)
                                if (isAdmin) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 6.dp)
                                            .background(DarkEmerald, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "أدوات نشر الوسائط (خاصة بالإدارة):",
                                            fontSize = 10.sp,
                                            color = RoyalGold,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            // Image attachment button
                                            Surface(
                                                onClick = {
                                                    mediaTypeInput = "IMAGE"
                                                    showAdminMediaDialog = true
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF0F4733),
                                                border = BorderStroke(1.dp, SoftGold)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Image,
                                                        contentDescription = null,
                                                        tint = SoftGold,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("📷 صورة", color = SoftGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }

                                            // Video attachment button
                                            Surface(
                                                onClick = {
                                                    mediaTypeInput = "VIDEO"
                                                    showAdminMediaDialog = true
                                                },
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF0F4733),
                                                border = BorderStroke(1.dp, SoftGold)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Videocam,
                                                        contentDescription = null,
                                                        tint = SoftGold,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("🎥 فيديو", color = SoftGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // Regular Users Text-Only Badge Notice
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 4.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "💡 متاح لك الكتابة النصية فقط (إرفاق الصور حصري للإدارة)",
                                            fontSize = 10.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                // Main Text Input Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = inputText,
                                        onValueChange = { inputText = it },
                                        placeholder = { Text("أكتب رسالتك للمجموعة هنا...", fontSize = 12.sp, color = TextMuted) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(16.dp),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = RoyalGold,
                                            unfocusedBorderColor = DarkGold,
                                            focusedTextColor = TextWhite,
                                            unfocusedTextColor = TextWhite,
                                            focusedContainerColor = DarkEmerald,
                                            unfocusedContainerColor = DarkEmerald
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    IconButton(
                                        onClick = {
                                            if (inputText.isNotBlank()) {
                                                onSendGroupMessage(inputText, null, null, false)
                                                inputText = ""
                                            }
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(RoyalGold, CircleShape)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "إرسال",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ADMIN MEDIA ATTACHMENT DIALOG
        if (showAdminMediaDialog) {
            AlertDialog(
                onDismissRequest = { showAdminMediaDialog = false },
                containerColor = DarkEmeraldCard,
                icon = { Icon(imageVector = if (mediaTypeInput == "IMAGE") Icons.Default.Image else Icons.Default.Videocam, contentDescription = null, tint = RoyalGold) },
                title = {
                    Text(
                        text = if (mediaTypeInput == "IMAGE") "نشر صورة في المحادثة الجماعية 📷" else "نشر فيديو عقار في المحادثة 🎥",
                        color = SoftGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "بصفتك مديراً للمنصة، يمكنك اختيار أحد العقارات المعروضة أو إدخال رابط الصورة/الفيديو لنشره مباشرة للجميع:",
                            color = TextWhite,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Preset Quick Options
                        Text("نماذج صور جاهزة:", fontSize = 11.sp, color = RoyalGold, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            item {
                                Button(
                                    onClick = { mediaUrlInput = "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=600" },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkEmerald),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("منزل حي السلام 🏠", fontSize = 10.sp, color = SoftGold)
                                }
                            }
                            item {
                                Button(
                                    onClick = { mediaUrlInput = "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=600" },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkEmerald),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("أرض زراعية تلراك 🌾", fontSize = 10.sp, color = SoftGold)
                                }
                            }
                            item {
                                Button(
                                    onClick = { mediaUrlInput = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=600" },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkEmerald),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("فيلا الشارع الرئيسي 🏰", fontSize = 10.sp, color = SoftGold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = mediaUrlInput,
                            onValueChange = { mediaUrlInput = it },
                            placeholder = { Text("أدخل رابط الوسائط هنا...", fontSize = 11.sp, color = TextMuted) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalGold,
                                unfocusedBorderColor = DarkGold,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Toggle for Official Announcement
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isAnnouncementToggle = !isAnnouncementToggle }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = if (isAnnouncementToggle) RoyalGold else TextMuted
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "تميز كـ إعلان رسمي في أعلى القائمة 📢",
                                color = if (isAnnouncementToggle) RoyalGold else TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (mediaUrlInput.isNotBlank()) {
                                onSendGroupMessage(
                                    inputText.ifBlank { if (mediaTypeInput == "IMAGE") "إليكم معاينة صورة عقار جديد بمكتب سمسارك 🏠" else "معاينة فيديو حصرية لعقار جديد 🎥" },
                                    mediaUrlInput,
                                    mediaTypeInput,
                                    isAnnouncementToggle
                                )
                                inputText = ""
                                mediaUrlInput = ""
                                showAdminMediaDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold)
                    ) {
                        Text("نشر للجميع 🚀", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAdminMediaDialog = false }) {
                        Text("إلغاء", color = SoftGold)
                    }
                }
            )
        }
    }
}

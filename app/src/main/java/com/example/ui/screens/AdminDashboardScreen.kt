package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DealType
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyStatus
import com.example.data.model.User
import com.example.data.utils.NotificationHelper
import com.example.ui.theme.AdminBackground
import com.example.ui.theme.AdminCardBg
import com.example.ui.theme.AdminCardElevated
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    isAdminLoggedIn: Boolean,
    pendingProperties: List<Property>,
    allProperties: List<Property>,
    registeredUsers: List<User>,
    callClicksCount: Int,
    whatsappClicksCount: Int,
    onLoginAdmin: (String) -> Boolean,
    onLogoutAdmin: () -> Unit,
    onApproveProperty: (Long) -> Unit,
    onRejectProperty: (Long, String) -> Unit,
    onUpdatePropertyStatus: (Long, String) -> Unit,
    onUpdatePropertyDetails: (Property) -> Unit,
    onAddDirectProperty: (Property) -> Unit,
    onDeleteProperty: (Property) -> Unit,
    onToggleBlockUser: (Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passcodeText by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }

    // Active Admin Section Navigation (0 = Vertical Main Menu List)
    var activeAdminView by remember { mutableIntStateOf(0) }
    var publishedFilterStatus by remember { mutableStateOf("الكل") }

    // Rejection Dialog state
    var rejectingPropertyId by remember { mutableStateOf<Long?>(null) }
    var rejectionReasonText by remember { mutableStateOf("") }

    // Edit Property Dialog state
    var editingProperty by remember { mutableStateOf<Property?>(null) }

    // Broadcast Push State
    var broadcastTitle by remember { mutableStateOf("") }
    var broadcastBody by remember { mutableStateOf("") }
    var broadcastSuccess by remember { mutableStateOf(false) }

    // User Search Query
    var userSearchQuery by remember { mutableStateOf("") }

    // Embedded Direct Add Property Form States
    var adminTitle by remember { mutableStateOf("") }
    var adminCategory by remember { mutableStateOf(PropertyCategory.HOUSES) }
    var adminCategoryExpanded by remember { mutableStateOf(false) }
    var adminPrice by remember { mutableStateOf("") }
    var adminIsNegotiable by remember { mutableStateOf(true) }
    var adminArea by remember { mutableStateOf("") }
    var adminDimensions by remember { mutableStateOf("") }
    var adminVillage by remember { mutableStateOf("أولاد صقر - المدينة") }
    var adminVillageExpanded by remember { mutableStateOf(false) }
    var adminAddressDetails by remember { mutableStateOf("") }
    var adminMapUrl by remember { mutableStateOf("") }
    var adminVideoUrl by remember { mutableStateOf("") }
    var adminDesc by remember { mutableStateOf("") }
    var adminPhone by remember { mutableStateOf("01010634040") }
    var adminWhatsapp by remember { mutableStateOf("201010634040") }
    var adminPhotosList = remember {
        mutableStateListOf(
            "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
            "https://images.unsplash.com/photo-1600585154340-be6161a56a0c"
        )
    }
    var adminNewPhotoInput by remember { mutableStateOf("") }
    var adminShowPhotoInput by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val categories = PropertyCategory.entries
    val villages = awladSaqrVillages.filter { it != "الكل" }

    val presetSamplePhotos = listOf(
        "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9" to "واجهة منزل مودرن",
        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750" to "فيلا / منزل عائلي",
        "https://images.unsplash.com/photo-1500382017468-9049fed747ef" to "أرض مباني / زراعية",
        "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2" to "شقة تمليك تشطيب فاخر"
    )

    val activePropertiesCount = allProperties.count { it.status == PropertyStatus.APPROVED.titleArabic || it.status == "نشط" }
    val soldOrRentedCount = allProperties.count { it.status == "تم البيع" || it.status == "تم الإيجار" }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = AdminBackground,
        topBar = {
            TopAppBar(
                title = { Text("لوحة تحكم الإدارة", fontWeight = FontWeight.Bold, color = SoftGold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                    }
                },
                actions = {
                    if (isAdminLoggedIn) {
                        IconButton(onClick = {
                            onLogoutAdmin()
                            activeAdminView = 0
                        }) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = "خروج", tint = StatusRejected)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AdminCardBg)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isAdminLoggedIn) {
                // PIN Login Screen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                        border = BorderStroke(2.dp, RoyalGold)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color(0xFF1E3A2B), CircleShape)
                                    .border(2.dp, RoyalGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Admin",
                                    tint = RoyalGold,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Text("دخول الأدمن والمدير العام", fontWeight = FontWeight.Black, fontSize = 20.sp, color = SoftGold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("أ/ عبدالرحمن - سمسارك في أولاد صقر", fontSize = 13.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("أدخل رمز المرور PIN أو البريد الإلكتروني للوصول", fontSize = 11.sp, color = TextMuted)

                            Spacer(modifier = Modifier.height(20.dp))

                            OutlinedTextField(
                                value = passcodeText,
                                onValueChange = {
                                    passcodeText = it
                                    loginError = false
                                },
                                label = { Text("رمز المرور أو الإيميل", color = SoftGold) },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoyalGold,
                                    unfocusedBorderColor = DarkGold,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite
                                )
                            )

                            if (loginError) {
                                Text(
                                    "رمز المرور غير صحيح! (جرب 01010634040 أو abdulrahma5n@gmail.com أو 1234)",
                                    color = StatusRejected,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(
                                onClick = {
                                    val ok = onLoginAdmin(passcodeText)
                                    if (!ok) loginError = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = "Login", tint = Color.Black)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("تسجيل الدخول كأدمن", color = Color.Black, fontWeight = FontWeight.Black)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                TextButton(onClick = { passcodeText = "01010634040" }) {
                                    Text("تجربة بـ 01010634040", fontSize = 11.sp, color = SoftGold)
                                }
                                TextButton(onClick = { passcodeText = "1234" }) {
                                    Text("تجربة بـ 1234", fontSize = 11.sp, color = SoftGold)
                                }
                            }
                        }
                    }
                }
            } else {
                // LOGGED IN ADMIN DASHBOARD CONTENT
                Column(modifier = Modifier.fillMaxSize()) {

                    // SECTION HEADER OR BACK TO VERTICAL LIST BUTTON
                    if (activeAdminView != 0) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = AdminCardBg,
                            border = BorderStroke(1.dp, RoyalGold)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Button(
                                    onClick = { activeAdminView = 0 },
                                    colors = ButtonDefaults.buttonColors(containerColor = DarkGold),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع للقائمة", tint = TextWhite)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("⬅️ العودة للقائمة الرئيسية للوحة التحكم", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quick Horizontal Chip Bar to jump between sections
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val menuTabs = listOf(
                                        1 to "📥 المعلقة (${pendingProperties.size})",
                                        2 to "➕ إضافة عقار",
                                        3 to "📋 إدارة المنشور (${allProperties.size})",
                                        4 to "📢 بث الإشعارات",
                                        5 to "👥 المستخدمين (${registeredUsers.size})",
                                        6 to "📊 الإحصائيات"
                                    )
                                    items(menuTabs) { (id, label) ->
                                        val isSelected = activeAdminView == id
                                        Surface(
                                            onClick = { activeAdminView = id },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) RoyalGold else AdminCardElevated,
                                            border = BorderStroke(1.dp, if (isSelected) SoftGold else DarkGold)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 11.sp,
                                                color = if (isSelected) Color.Black else TextWhite,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // MAIN SWITCH VIEW (0 = VERTICAL DASHBOARD LIST, 1..6 = SUB VIEWS)
                    when (activeAdminView) {
                        0 -> {
                            // -----------------------------------------------------------------
                            // 1. VERTICAL ADMIN DASHBOARD (ترتيب لوحة التحكم كقائمة رأسية)
                            // -----------------------------------------------------------------
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Admin Greeting Banner
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                    border = BorderStroke(1.5.dp, RoyalGold)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("👑 مرحباً بك أ/ عبدالرحمن", fontWeight = FontWeight.Black, color = SoftGold, fontSize = 18.sp)
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text("لوحة التحكم الرئيسية لسمسارك بأولاد صقر", fontSize = 12.sp, color = TextWhite)
                                        }

                                        OutlinedButton(
                                            onClick = onLogoutAdmin,
                                            border = BorderStroke(1.dp, StatusRejected),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("خروج", color = StatusRejected, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Text(
                                    text = "قائمة مهام وإدارة التطبيق (اختر قسم للبدء):",
                                    fontWeight = FontWeight.Bold,
                                    color = SoftGold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                // VERTICAL LIST ITEM 1: Pending Approvals
                                AdminVerticalMenuItemCard(
                                    title = "1. 📥 طلبات الإضافة المعلقة",
                                    subtitle = "مراجعة وإقرار طلبات نشر العقارات الجديدة المرسلة من الأهالي",
                                    badgeText = "${pendingProperties.size} طلبات معلقة",
                                    badgeBg = if (pendingProperties.isNotEmpty()) StatusPending else DarkGold,
                                    icon = Icons.Default.HourglassTop,
                                    iconTint = StatusPending,
                                    onClick = { activeAdminView = 1 }
                                )

                                // VERTICAL LIST ITEM 2: Add New Property
                                AdminVerticalMenuItemCard(
                                    title = "2. ➕ إضافة عقار جديد ونشره",
                                    subtitle = "إدخال بيانات العقار كاملة (السعر، التفاوض، الصور، الفيديو، الخريطة) وتفعيله فوراً",
                                    badgeText = "مباشر 🚀",
                                    badgeBg = RoyalGold,
                                    icon = Icons.Default.Add,
                                    iconTint = RoyalGold,
                                    onClick = { activeAdminView = 2 }
                                )

                                // VERTICAL LIST ITEM 3: Manage Live Listings
                                AdminVerticalMenuItemCard(
                                    title = "3. 📋 إدارة وتعديل العقارات المنشورة",
                                    subtitle = "تعديل الأسعار والتفاصيل، تغيير حالة العقار (تم البيع / تم الإيجار)، أو حذف الإعلانات",
                                    badgeText = "${allProperties.size} عقارات معروضة",
                                    badgeBg = StatusApproved,
                                    icon = Icons.Default.ListAlt,
                                    iconTint = StatusApproved,
                                    onClick = { activeAdminView = 3 }
                                )

                                // VERTICAL LIST ITEM 4: Broadcast Push
                                AdminVerticalMenuItemCard(
                                    title = "4. 📢 إرسال إشعار فوري لجميع المستخدمين",
                                    subtitle = "بث تنبيهات وإشعارات عاجلة تصل لهواتف كافة مستخدمي تطبيق سمسارك",
                                    badgeText = "إشعار فوري 🔔",
                                    badgeBg = SoftGold,
                                    icon = Icons.Default.NotificationsActive,
                                    iconTint = SoftGold,
                                    onClick = { activeAdminView = 4 }
                                )

                                // VERTICAL LIST ITEM 5: User Management
                                AdminVerticalMenuItemCard(
                                    title = "5. 👥 إدارة الحسابات والمستخدمين",
                                    subtitle = "عرض قائمة مستخدمي التطبيق، متابعة وسيلة التسجيل، وحظر أو تفعيل الحسابات",
                                    badgeText = "${registeredUsers.size} مستخدم مسجل",
                                    badgeBg = DarkGold,
                                    icon = Icons.Default.People,
                                    iconTint = TextWhite,
                                    onClick = { activeAdminView = 5 }
                                )

                                // VERTICAL LIST ITEM 6: Dashboard Analytics
                                AdminVerticalMenuItemCard(
                                    title = "6. 📊 إحصائيات العقارات والاتصالات",
                                    subtitle = "متابعة عداد اتصالات الهاتف والواتساب ونسب العقارات المباعة والنشطة",
                                    badgeText = "KPIs 📈",
                                    badgeBg = StatusApproved,
                                    icon = Icons.Default.Analytics,
                                    iconTint = StatusApproved,
                                    onClick = { activeAdminView = 6 }
                                )
                            }
                        }

                        1 -> {
                            // PENDING APPROVALS LIST VIEW
                            if (pendingProperties.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("🎉", fontSize = 48.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("لا توجد عقارات جديدة قيد المراجعة حالياً!", fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text("جميع طلبات المستخدمين تم البت فيها.", fontSize = 12.sp, color = TextMuted)
                                    }
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(pendingProperties, key = { it.id }) { prop ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(18.dp),
                                            colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                            border = BorderStroke(1.5.dp, StatusPending)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Surface(shape = RoundedCornerShape(8.dp), color = StatusPending) {
                                                        Text("قيد المراجعة ⏳", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                                    }
                                                    Text("${prop.priceEgp.toInt()} ج.م", color = SoftGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = prop.title,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite,
                                                    fontSize = 15.sp,
                                                    lineHeight = 20.sp,
                                                    textAlign = TextAlign.Right,
                                                    modifier = Modifier.fillMaxWidth()
                                                )

                                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "المنطقة", tint = RoyalGold, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("${prop.villageArea} • ${prop.addressDetails}", color = TextMuted, fontSize = 12.sp)
                                                }

                                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                                                    Icon(imageVector = Icons.Default.Person, contentDescription = "صاحب العقار", tint = SoftGold, modifier = Modifier.size(14.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("المعلن: ${prop.ownerName} (${prop.contactPhone})", color = TextWhite, fontSize = 12.sp)
                                                }

                                                if (prop.videoUrl.isNotBlank()) {
                                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                                        Icon(imageVector = Icons.Default.PlayCircle, contentDescription = "فيديو", tint = StatusApproved, modifier = Modifier.size(14.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("يتضمن مقطع فيديو للمعاينة", color = StatusApproved, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }

                                                Text("الوصف: ${prop.description}", color = TextMuted, fontSize = 12.sp, maxLines = 3, modifier = Modifier.padding(top = 4.dp))

                                                Spacer(modifier = Modifier.height(14.dp))

                                                // Action Buttons
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                ) {
                                                    Button(
                                                        onClick = { onApproveProperty(prop.id) },
                                                        colors = ButtonDefaults.buttonColors(containerColor = StatusApproved),
                                                        modifier = Modifier.weight(1.2f),
                                                        shape = RoundedCornerShape(10.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Check, contentDescription = "موافقة", tint = Color.White, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("موافقة ونشر", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    }

                                                    Button(
                                                        onClick = {
                                                            rejectingPropertyId = prop.id
                                                            rejectionReasonText = ""
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = StatusRejected),
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(10.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Close, contentDescription = "رفض", tint = Color.White, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text("رفض مع سبب", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                                    }

                                                    OutlinedButton(
                                                        onClick = { editingProperty = prop },
                                                        border = BorderStroke(1.dp, RoyalGold),
                                                        modifier = Modifier.weight(1f),
                                                        shape = RoundedCornerShape(10.dp)
                                                    ) {
                                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "تعديل", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(2.dp))
                                                        Text("تعديل", color = SoftGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            // -----------------------------------------------------------------
                            // 2. COMPLETE "ADD PROPERTY FORM" (خانات إضافة العقار الكاملة)
                            // -----------------------------------------------------------------
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                    border = BorderStroke(1.5.dp, RoyalGold)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(18.dp),
                                        verticalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {
                                        Text("➕ إضافة عقار جديد كـ أدمن وتفعيله فوراً:", fontWeight = FontWeight.Black, color = SoftGold, fontSize = 16.sp)

                                        // Title
                                        OutlinedTextField(
                                            value = adminTitle,
                                            onValueChange = { adminTitle = it },
                                            label = { Text("عنوان العقار (مثال: شقة تمليك 150م أو أرض مباني بالصوفية)", color = SoftGold) },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                        )

                                        // Category & Village Dropdowns Row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            ExposedDropdownMenuBox(
                                                expanded = adminCategoryExpanded,
                                                onExpandedChange = { adminCategoryExpanded = !adminCategoryExpanded },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                OutlinedTextField(
                                                    value = adminCategory.titleArabic,
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    label = { Text("التصنيف", color = SoftGold) },
                                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = adminCategoryExpanded) },
                                                    modifier = Modifier.menuAnchor(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = adminCategoryExpanded,
                                                    onDismissRequest = { adminCategoryExpanded = false }
                                                ) {
                                                    categories.forEach { cat ->
                                                        DropdownMenuItem(
                                                            text = { Text(cat.titleArabic) },
                                                            onClick = {
                                                                adminCategory = cat
                                                                adminCategoryExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                            ExposedDropdownMenuBox(
                                                expanded = adminVillageExpanded,
                                                onExpandedChange = { adminVillageExpanded = !adminVillageExpanded },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                OutlinedTextField(
                                                    value = adminVillage,
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    label = { Text("القرية / المنطقة", color = SoftGold) },
                                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = adminVillageExpanded) },
                                                    modifier = Modifier.menuAnchor(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                                )
                                                ExposedDropdownMenu(
                                                    expanded = adminVillageExpanded,
                                                    onDismissRequest = { adminVillageExpanded = false }
                                                ) {
                                                    villages.forEach { vil ->
                                                        DropdownMenuItem(
                                                            text = { Text(vil) },
                                                            onClick = {
                                                                adminVillage = vil
                                                                adminVillageExpanded = false
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // -------------------------------------------------------------
                                        // Requirement 2: Price & Negotiation Selector (السعر والتفاوض)
                                        // -------------------------------------------------------------
                                        Column {
                                            Text("السعر وحالة التفاوض:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 13.sp)
                                            Spacer(modifier = Modifier.height(6.dp))

                                            OutlinedTextField(
                                                value = adminPrice,
                                                onValueChange = { adminPrice = it },
                                                label = { Text("السعر المطلوب (بالجنيه المصري)", color = SoftGold) },
                                                leadingIcon = { Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "السعر", tint = RoyalGold) },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Surface(
                                                    onClick = { adminIsNegotiable = true },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = if (adminIsNegotiable) StatusApproved else AdminCardElevated,
                                                    border = BorderStroke(1.5.dp, if (adminIsNegotiable) Color.White else DarkGold)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(vertical = 10.dp),
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("🟢 قابل للتفاوض", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                                    }
                                                }

                                                Surface(
                                                    onClick = { adminIsNegotiable = false },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(12.dp),
                                                    color = if (!adminIsNegotiable) StatusRejected else AdminCardElevated,
                                                    border = BorderStroke(1.5.dp, if (!adminIsNegotiable) Color.White else DarkGold)
                                                ) {
                                                    Row(
                                                        modifier = Modifier.padding(vertical = 10.dp),
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text("🔴 غير قابل للتفاوض", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                                    }
                                                }
                                            }
                                        }

                                        // Area & Dimensions
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            OutlinedTextField(
                                                value = adminArea,
                                                onValueChange = { adminArea = it },
                                                label = { Text("المساحة (م²)", color = SoftGold) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )

                                            OutlinedTextField(
                                                value = adminDimensions,
                                                onValueChange = { adminDimensions = it },
                                                label = { Text("الأبعاد (مثال: 12م x 15م)", color = SoftGold) },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )
                                        }

                                        // -------------------------------------------------------------
                                        // Requirement 2: Photo Upload Section (خانة إضافة الصور)
                                        // -------------------------------------------------------------
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "صور", tint = SoftGold, modifier = Modifier.size(18.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("صور العقار (${adminPhotosList.size} صور مضافة)", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                                                }

                                                TextButton(onClick = { adminShowPhotoInput = !adminShowPhotoInput }) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة صورة", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                                        Spacer(modifier = Modifier.width(2.dp))
                                                        Text("إضافة صورة", color = RoyalGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                    }
                                                }
                                            }

                                            // Thumbnail Carousel with Delete Icons
                                            if (adminPhotosList.isNotEmpty()) {
                                                LazyRow(
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    modifier = Modifier.padding(vertical = 6.dp)
                                                ) {
                                                    itemsIndexed(adminPhotosList) { idx, photoUrl ->
                                                        Box(
                                                            modifier = Modifier
                                                                .size(80.dp)
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .border(1.dp, DarkGold, RoundedCornerShape(12.dp))
                                                        ) {
                                                            AsyncImage(
                                                                model = photoUrl,
                                                                contentDescription = "صورة $idx",
                                                                modifier = Modifier.matchParentSize(),
                                                                contentScale = ContentScale.Crop
                                                            )

                                                            Box(
                                                                modifier = Modifier
                                                                    .align(Alignment.TopEnd)
                                                                    .padding(4.dp)
                                                                    .size(22.dp)
                                                                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                                                                    .clickable { adminPhotosList.removeAt(idx) },
                                                                contentAlignment = Alignment.Center
                                                            ) {
                                                                Icon(imageVector = Icons.Default.Close, contentDescription = "حذف", tint = Color.White, modifier = Modifier.size(14.dp))
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            // Quick Preset Sample Photos
                                            Text("نماذج صور تجهيزية سريعة:", fontSize = 11.sp, color = TextMuted)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                itemsIndexed(presetSamplePhotos) { _, pair ->
                                                    Surface(
                                                        onClick = { adminPhotosList.add(pair.first) },
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = AdminCardElevated,
                                                        border = BorderStroke(1.dp, DarkGold)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = "إضافة", tint = SoftGold, modifier = Modifier.size(12.dp))
                                                            Spacer(modifier = Modifier.width(4.dp))
                                                            Text(pair.second, fontSize = 11.sp, color = TextWhite)
                                                        }
                                                    }
                                                }
                                            }

                                            if (adminShowPhotoInput) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                OutlinedTextField(
                                                    value = adminNewPhotoInput,
                                                    onValueChange = { adminNewPhotoInput = it },
                                                    label = { Text("أدخل رابط صورة (URL) جديدة", color = SoftGold) },
                                                    trailingIcon = {
                                                        IconButton(onClick = {
                                                            if (adminNewPhotoInput.isNotBlank()) {
                                                                adminPhotosList.add(adminNewPhotoInput.trim())
                                                                adminNewPhotoInput = ""
                                                                adminShowPhotoInput = false
                                                            }
                                                        }) {
                                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "إضافة", tint = RoyalGold)
                                                        }
                                                    },
                                                    singleLine = true,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = RoundedCornerShape(12.dp),
                                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                                )
                                            }
                                        }

                                        // -------------------------------------------------------------
                                        // Requirement 2: Video Input Section (خانة إضافة الفيديو)
                                        // -------------------------------------------------------------
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "فيديو", tint = SoftGold, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("فيديو العقار والمعاينة (YouTube / Drive / MP4):", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            OutlinedTextField(
                                                value = adminVideoUrl,
                                                onValueChange = { adminVideoUrl = it },
                                                label = { Text("رابط مقطع الفيديو", color = SoftGold) },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )

                                            Row(modifier = Modifier.padding(top = 4.dp)) {
                                                TextButton(onClick = {
                                                    adminVideoUrl = "https://www.youtube.com/watch?v=sample_awlad_saqr"
                                                }) {
                                                    Text("💡 إدراج نموذج فيديو توضيحي للمعاينة", fontSize = 11.sp, color = RoyalGold)
                                                }
                                            }
                                        }

                                        // -------------------------------------------------------------
                                        // Requirement 2: Google Maps Location Section (موقع خريطة جوجل)
                                        // -------------------------------------------------------------
                                        Column(modifier = Modifier.fillMaxWidth()) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(imageVector = Icons.Default.Map, contentDescription = "خرائط جوجل", tint = SoftGold, modifier = Modifier.size(18.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text("موقع خريطة جوجل Google Maps:", fontWeight = FontWeight.Bold, color = SoftGold, fontSize = 14.sp)
                                            }

                                            Spacer(modifier = Modifier.height(4.dp))

                                            OutlinedTextField(
                                                value = adminAddressDetails,
                                                onValueChange = { adminAddressDetails = it },
                                                label = { Text("العنوان التفصيلي والشارع", color = SoftGold) },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            OutlinedTextField(
                                                value = adminMapUrl,
                                                onValueChange = { adminMapUrl = it },
                                                label = { Text("رابط الموقع من Google Maps أو إحداثيات Pin", color = SoftGold) },
                                                leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = "موقع", tint = RoyalGold) },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            OutlinedButton(
                                                onClick = {
                                                    adminMapUrl = "https://maps.google.com/?q=Awlad+Saqr+$adminVillage"
                                                },
                                                border = BorderStroke(1.dp, RoyalGold),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "تحديد موقع", tint = RoyalGold, modifier = Modifier.size(16.dp))
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text("📍 إدراج موقع الدبوس تلقائياً في منطقة ($adminVillage)", fontSize = 11.sp, color = SoftGold, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }

                                        // Description
                                        OutlinedTextField(
                                            value = adminDesc,
                                            onValueChange = { adminDesc = it },
                                            label = { Text("وصف كامل ومفصل للعقار والمميزات", color = SoftGold) },
                                            modifier = Modifier.fillMaxWidth(),
                                            minLines = 3,
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        // Publish Button
                                        Button(
                                            onClick = {
                                                val price = adminPrice.toDoubleOrNull() ?: 500000.0
                                                val area = adminArea.toDoubleOrNull() ?: 120.0
                                                val combinedPhotos = if (adminPhotosList.isNotEmpty()) adminPhotosList.joinToString(",") else "https://images.unsplash.com/photo-1580587771525-78b9dba3b914"
                                                val finalAddress = if (adminAddressDetails.isNotBlank()) adminAddressDetails else "أولاد صقر - $adminVillage"

                                                val newProp = Property(
                                                    title = if (adminTitle.isNotBlank()) adminTitle else "عقار مباشر من الإدارة",
                                                    categoryId = adminCategory.id,
                                                    priceEgp = price,
                                                    isNegotiable = adminIsNegotiable,
                                                    areaSqm = area,
                                                    dimensions = adminDimensions,
                                                    villageArea = adminVillage,
                                                    addressDetails = if (adminMapUrl.isNotBlank()) "$finalAddress ($adminMapUrl)" else finalAddress,
                                                    description = if (adminDesc.isNotBlank()) adminDesc else "عقار متميز معروض ومفعل مباشرة من الإدارة بأولاد صقر.",
                                                    imageUrls = combinedPhotos,
                                                    videoUrl = adminVideoUrl,
                                                    contactPhone = adminPhone,
                                                    contactWhatsapp = adminWhatsapp,
                                                    ownerName = "أ/ عبدالرحمن",
                                                    status = PropertyStatus.APPROVED.titleArabic
                                                )
                                                onAddDirectProperty(newProp)

                                                // Reset inputs & switch to Live Listings
                                                adminTitle = ""
                                                adminPrice = ""
                                                adminArea = ""
                                                adminDesc = ""
                                                activeAdminView = 3
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("نشر العقار وتفعيله فوراً كـ أدمن 🚀", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }
                        }

                        3 -> {
                            // LIVE PROPERTY MANAGEMENT VIEW
                            Column(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(AdminCardBg)
                                        .padding(12.dp)
                                ) {
                                    Button(
                                        onClick = { activeAdminView = 2 },
                                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "إضافة مباشر", tint = Color.Black)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("إضافة عقار جديد كـ أدمن مع تفعيل فوري 🚀", color = Color.Black, fontWeight = FontWeight.Black)
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    val filterStatuses = listOf("الكل", "نشط", "تم البيع", "تم الإيجار", "مؤرشف", "قيد المراجعة")
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        items(filterStatuses) { status ->
                                            val isSelected = publishedFilterStatus == status
                                            Surface(
                                                onClick = { publishedFilterStatus = status },
                                                shape = RoundedCornerShape(12.dp),
                                                color = if (isSelected) RoyalGold else AdminCardElevated,
                                                border = BorderStroke(1.dp, if (isSelected) SoftGold else DarkGold)
                                            ) {
                                                Text(
                                                    text = status,
                                                    fontSize = 12.sp,
                                                    color = if (isSelected) Color.Black else TextWhite,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                val filteredProperties = allProperties.filter {
                                    when (publishedFilterStatus) {
                                        "الكل" -> true
                                        "نشط" -> it.status == PropertyStatus.APPROVED.titleArabic || it.status == "نشط"
                                        else -> it.status == publishedFilterStatus
                                    }
                                }

                                if (filteredProperties.isEmpty()) {
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text("لا توجد عقارات تحت تصنيف '$publishedFilterStatus'", color = TextWhite)
                                    }
                                } else {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(filteredProperties, key = { it.id }) { prop ->
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                                border = BorderStroke(1.5.dp, RoyalGold)
                                            ) {
                                                Column(modifier = Modifier.padding(14.dp)) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        StatusDropdownBadge(
                                                            currentStatus = prop.status,
                                                            onStatusSelected = { newStatus ->
                                                                onUpdatePropertyStatus(prop.id, newStatus)
                                                            }
                                                        )

                                                        Text("${prop.priceEgp.toInt()} ج.م", color = SoftGold, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                                    }

                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(prop.title, fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
                                                    Text("${prop.villageArea} • ${prop.dealType} • ${if (prop.isNegotiable) "قابل للتفاوض" else "سعر نهائي"}", color = TextMuted, fontSize = 11.sp)

                                                    Spacer(modifier = Modifier.height(10.dp))

                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                            OutlinedButton(
                                                                onClick = { editingProperty = prop },
                                                                border = BorderStroke(1.dp, RoyalGold),
                                                                shape = RoundedCornerShape(8.dp)
                                                            ) {
                                                                Icon(imageVector = Icons.Default.Edit, contentDescription = "تعديل", tint = RoyalGold, modifier = Modifier.size(14.dp))
                                                                Spacer(modifier = Modifier.width(4.dp))
                                                                Text("تعديل", color = SoftGold, fontSize = 11.sp)
                                                            }
                                                        }

                                                        IconButton(onClick = { onDeleteProperty(prop) }) {
                                                            Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = StatusRejected)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        4 -> {
                            // BROADCAST PUSH NOTIFICATION VIEW
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                    border = BorderStroke(1.5.dp, RoyalGold)
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = "Broadcast", tint = RoyalGold, modifier = Modifier.size(24.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("مركز بث الإشعارات الفورية لجميع الأجهزة", fontWeight = FontWeight.Black, color = SoftGold, fontSize = 15.sp)
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text("اختر نموذجاً سريعاً أو اكتب رسالة مخصصة تصل لجميع مستخدمي أولاد صقر:", fontSize = 12.sp, color = TextMuted)

                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text("نماذج إشعارات جاهزة بضغطة زر:", fontSize = 11.sp, color = SoftGold, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        val presets = listOf(
                                            "🔥 فرصة ممتازة: أراضي مباني داخل الكردون في تلراك!",
                                            "⚡ شقق تمليك بسعر لقطة في وسط البلد - أولاد صقر",
                                            "🏡 منازل عائلية للبيع وتسليم فوري بالصوفية وبني حسن"
                                        )
                                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            presets.forEach { preset ->
                                                Surface(
                                                    onClick = {
                                                        broadcastTitle = "تنبيه عقاري هام 🔔"
                                                        broadcastBody = preset
                                                    },
                                                    shape = RoundedCornerShape(10.dp),
                                                    color = AdminCardElevated,
                                                    border = BorderStroke(1.dp, DarkGold)
                                                ) {
                                                    Text(preset, fontSize = 11.sp, color = TextWhite, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        OutlinedTextField(
                                            value = broadcastTitle,
                                            onValueChange = { broadcastTitle = it },
                                            label = { Text("عنوان الإشعار", color = SoftGold) },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))

                                        OutlinedTextField(
                                            value = broadcastBody,
                                            onValueChange = { broadcastBody = it },
                                            label = { Text("نص الإشعار الفوري", color = SoftGold) },
                                            modifier = Modifier.fillMaxWidth(),
                                            minLines = 3,
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Button(
                                            onClick = {
                                                if (broadcastTitle.isNotBlank() && broadcastBody.isNotBlank()) {
                                                    NotificationHelper.showNotification(
                                                        context,
                                                        broadcastTitle,
                                                        broadcastBody
                                                    )
                                                    broadcastSuccess = true
                                                    broadcastTitle = ""
                                                    broadcastBody = ""
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Send, contentDescription = "بث", tint = Color.Black)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("بث الإشعار الآن للكل 📢", color = Color.Black, fontWeight = FontWeight.Black)
                                        }

                                        if (broadcastSuccess) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Text("✅ تم بث الإشعار بنجاح وإرساله إلى جميع الأجهزة!", color = StatusApproved, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        5 -> {
                            // USER MANAGEMENT VIEW
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp)
                            ) {
                                OutlinedTextField(
                                    value = userSearchQuery,
                                    onValueChange = { userSearchQuery = it },
                                    placeholder = { Text("ابحث عن مستخدم برقم الهاتف أو الاسم...", color = TextMuted, fontSize = 12.sp) },
                                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "بحث", tint = SoftGold) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                val filteredUsers = registeredUsers.filter {
                                    userSearchQuery.isBlank() ||
                                            it.name.contains(userSearchQuery, ignoreCase = true) ||
                                            it.phone.contains(userSearchQuery)
                                }

                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(filteredUsers, key = { it.id }) { user ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = AdminCardBg),
                                            border = BorderStroke(1.5.dp, if (user.isBlocked) StatusRejected else RoyalGold)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(14.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(user.name, fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 14.sp)
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Surface(
                                                            shape = RoundedCornerShape(6.dp),
                                                            color = if (user.isBlocked) StatusRejected else StatusApproved
                                                        ) {
                                                            Text(
                                                                if (user.isBlocked) "محظور 🛑" else "نشط ✅",
                                                                color = Color.White,
                                                                fontSize = 10.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                    }

                                                    Text("الهاتف: ${user.phone}", color = SoftGold, fontSize = 12.sp)
                                                    Text("وسيلة التسجيل: ${user.loginMethod} • ${user.cityLocation}", color = TextMuted, fontSize = 11.sp)
                                                }

                                                OutlinedButton(
                                                    onClick = { onToggleBlockUser(user.id) },
                                                    border = BorderStroke(1.dp, if (user.isBlocked) StatusApproved else StatusRejected),
                                                    shape = RoundedCornerShape(10.dp)
                                                ) {
                                                    Text(
                                                        if (user.isBlocked) "إلغاء الحظر" else "حظر 🛑",
                                                        color = if (user.isBlocked) StatusApproved else StatusRejected,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        6 -> {
                            // ANALYTICS VIEW
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = AdminCardBg,
                                    border = BorderStroke(1.5.dp, RoyalGold),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.Analytics, contentDescription = "Analytics", tint = RoyalGold, modifier = Modifier.size(32.dp))
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text("ملخص نشاط أولاد صقر المباشر", fontWeight = FontWeight.Black, color = SoftGold, fontSize = 16.sp)
                                            Text("متابعة فورية لاتصالات الزبائن وحالة العقارات والمستخدمين", fontSize = 11.sp, color = TextMuted)
                                        }
                                    }
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        KpiCard(
                                            title = "إجمالي العقارات النشطة",
                                            value = "$activePropertiesCount",
                                            icon = Icons.Default.CheckCircle,
                                            iconColor = StatusApproved,
                                            modifier = Modifier.weight(1f)
                                        )
                                        KpiCard(
                                            title = "طلبات قيد المراجعة",
                                            value = "${pendingProperties.size}",
                                            icon = Icons.Default.HourglassTop,
                                            iconColor = StatusPending,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        KpiCard(
                                            title = "العقارات المباعة / المؤجرة",
                                            value = "$soldOrRentedCount",
                                            icon = Icons.Default.ListAlt,
                                            iconColor = RoyalGold,
                                            modifier = Modifier.weight(1f)
                                        )
                                        KpiCard(
                                            title = "المستخدمين المسجلين",
                                            value = "${registeredUsers.size}",
                                            icon = Icons.Default.People,
                                            iconColor = SoftGold,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        KpiCard(
                                            title = "استفسارات الواتساب",
                                            value = "$whatsappClicksCount",
                                            icon = Icons.Default.Send,
                                            iconColor = Color(0xFF25D366),
                                            modifier = Modifier.weight(1f)
                                        )
                                        KpiCard(
                                            title = "اتصالات الهاتف",
                                            value = "$callClicksCount",
                                            icon = Icons.Default.Call,
                                            iconColor = RoyalGold,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // REJECTION REASON DIALOG
            if (rejectingPropertyId != null) {
                AlertDialog(
                    onDismissRequest = { rejectingPropertyId = null },
                    containerColor = AdminCardBg,
                    title = { Text("سبب رفض العقار", fontWeight = FontWeight.Bold, color = SoftGold) },
                    text = {
                        Column {
                            Text("أدخل سبب عدم الموافقة على نشر العقار ليظهر للمعلن:", fontSize = 12.sp, color = TextWhite)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = rejectionReasonText,
                                onValueChange = { rejectionReasonText = it },
                                placeholder = { Text("مثال: الصورة غير واضحة أو السعر مبالغ فيه", color = TextMuted) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                rejectingPropertyId?.let { id ->
                                    val reason = rejectionReasonText.ifBlank { "لم يستوفِ العقار شروط واشتراطات النشر بأولاد صقر" }
                                    onRejectProperty(id, reason)
                                }
                                rejectingPropertyId = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StatusRejected)
                        ) {
                            Text("تأكيد الرفض ❌", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { rejectingPropertyId = null }) {
                            Text("إلغاء", color = TextMuted)
                        }
                    }
                )
            }

            // EDIT PROPERTY DIALOG
            editingProperty?.let { prop ->
                var editTitle by remember { mutableStateOf(prop.title) }
                var editPrice by remember { mutableStateOf(prop.priceEgp.toInt().toString()) }
                var editArea by remember { mutableStateOf(prop.areaSqm.toInt().toString()) }
                var editDesc by remember { mutableStateOf(prop.description) }

                AlertDialog(
                    onDismissRequest = { editingProperty = null },
                    containerColor = AdminCardBg,
                    title = { Text("تعديل تفاصيل العقار", fontWeight = FontWeight.Bold, color = SoftGold) },
                    text = {
                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = editTitle,
                                onValueChange = { editTitle = it },
                                label = { Text("العنوان", color = SoftGold) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                            )
                            OutlinedTextField(
                                value = editPrice,
                                onValueChange = { editPrice = it },
                                label = { Text("السعر (ج.م)", color = SoftGold) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                            )
                            OutlinedTextField(
                                value = editArea,
                                onValueChange = { editArea = it },
                                label = { Text("المساحة (م²)", color = SoftGold) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                            )
                            OutlinedTextField(
                                value = editDesc,
                                onValueChange = { editDesc = it },
                                label = { Text("الوصف", color = SoftGold) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalGold, unfocusedBorderColor = DarkGold, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val updated = prop.copy(
                                    title = editTitle,
                                    priceEgp = editPrice.toDoubleOrNull() ?: prop.priceEgp,
                                    areaSqm = editArea.toDoubleOrNull() ?: prop.areaSqm,
                                    description = editDesc
                                )
                                onUpdatePropertyDetails(updated)
                                editingProperty = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold)
                        ) {
                            Text("حفظ التغييرات 💾", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { editingProperty = null }) {
                            Text("إلغاء", color = TextMuted)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AdminVerticalMenuItemCard(
    title: String,
    subtitle: String,
    badgeText: String,
    badgeBg: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AdminCardBg),
        border = BorderStroke(1.5.dp, RoyalGold),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Right: Icon inside a rounded gold frame
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(AdminBackground, CircleShape)
                    .border(1.5.dp, RoyalGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Center: Title & subtitle stacked cleanly vertically without text overflow
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Right
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = SoftGold,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Right
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Left: Dedicated container for Badge & Arrow indicator (Separated cleanly)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (badgeText.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = badgeBg,
                        border = BorderStroke(0.5.dp, RoyalGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = badgeText,
                            color = if (badgeBg == RoyalGold || badgeBg == StatusPending) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "عرض",
                    tint = RoyalGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AdminCardBg),
        border = BorderStroke(1.5.dp, RoyalGold)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Black, fontSize = 22.sp, color = TextWhite)
            Spacer(modifier = Modifier.height(2.dp))
            Text(title, fontSize = 11.sp, color = SoftGold, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun StatusDropdownBadge(
    currentStatus: String,
    onStatusSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val (badgeBg, badgeText) = when (currentStatus) {
        PropertyStatus.APPROVED.titleArabic, "نشط" -> Pair(StatusApproved, "نشط (معروض) ✅")
        "تم البيع" -> Pair(RoyalGold, "تم البيع 🏷️")
        "تم الإيجار" -> Pair(SoftGold, "تم الإيجار 🔑")
        "مؤرشف" -> Pair(DarkGold, "مؤرشف 📦")
        PropertyStatus.PENDING.titleArabic -> Pair(StatusPending, "قيد المراجعة ⏳")
        else -> Pair(StatusRejected, "مرفوض ❌")
    }

    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            color = badgeBg,
            border = BorderStroke(0.5.dp, RoyalGold.copy(alpha = 0.5f))
        ) {
            Text(
                text = badgeText,
                color = if (badgeBg == RoyalGold || badgeBg == SoftGold) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AdminCardBg)
        ) {
            DropdownMenuItem(
                text = { Text("نشط (معروض)", color = TextWhite) },
                onClick = {
                    onStatusSelected(PropertyStatus.APPROVED.titleArabic)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("تم البيع 🏷️", color = TextWhite) },
                onClick = {
                    onStatusSelected("تم البيع")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("تم الإيجار 🔑", color = TextWhite) },
                onClick = {
                    onStatusSelected("تم الإيجار")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("مؤرشف 📦", color = TextWhite) },
                onClick = {
                    onStatusSelected("مؤرشف")
                    expanded = false
                }
            )
        }
    }
}

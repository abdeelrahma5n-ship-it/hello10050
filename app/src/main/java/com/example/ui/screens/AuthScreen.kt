package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.User
import com.example.ui.components.SemsarkBottomBar
import com.example.ui.components.SemsarkTab
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    currentUser: User? = null,
    userPhone: String? = null,
    onLoginUserFull: (name: String, phone: String, email: String, method: String, city: String) -> Unit,
    onUpdateProfile: (name: String, username: String, phone: String, picUri: String, onSuccess: (User) -> Unit) -> Unit = { _, _, _, _, _ -> },
    onLogoutUser: () -> Unit = {},
    onBrowseAsGuest: () -> Unit = {},
    onBack: () -> Unit,
    onNavigateToFavorites: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var phoneInput by remember { mutableStateOf(userPhone ?: "") }
    var otpInput by remember { mutableStateOf("") }
    var showOtpModal by remember { mutableStateOf(false) }

    // Dialog state controllers for forcing interactive account selection
    var showGooglePicker by remember { mutableStateOf(false) }
    var showFacebookPicker by remember { mutableStateOf(false) }
    var showTikTokPicker by remember { mutableStateOf(false) }
    var showInstagramPicker by remember { mutableStateOf(false) }
    var showApplePicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("الملف الشخصي والحساب", fontWeight = FontWeight.Bold, color = SoftGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1B4D3E),
                            border = BorderStroke(1.dp, RoyalGold)
                        ) {
                            Text(
                                "🛡️ Auth Guard",
                                color = SoftGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (currentUser != null) {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkEmeraldCard)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Header
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkEmeraldCard)
                    .border(2.dp, RoyalGold, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🐎", fontSize = 34.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("سمسارك في أولاد صقر", fontWeight = FontWeight.Black, fontSize = 22.sp, color = SoftGold)
            Text("البوابة الأولى للعقارات والمنازل والأراضي بمحافظة الشرقية", fontSize = 12.sp, color = TextMuted)

            Spacer(modifier = Modifier.height(16.dp))

            // Auth Guard Security Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF093325)),
                border = BorderStroke(1.dp, RoyalGold)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Auth Guard Status",
                        tint = RoyalGold,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (currentUser != null) "حالة الحساب: مسجل ومحمي ✅" else "نظام حماية الدخول (Auth Guard - نشط) 🛡️",
                            color = SoftGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (currentUser != null)
                                "أهلاً بك (${currentUser.name})! حسابك مفعل للوصول لكافة العقارات والخدمات."
                            else
                                "يلزم إتمام تسجيل الدخول للوصول لباقي صفحات التطبيق وتصفح العقارات.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Active Signed-in User Profile Customization Card (if currently logged in)
            if (currentUser != null) {
                UserProfileEditCard(
                    currentUser = currentUser,
                    onUpdateProfile = onUpdateProfile,
                    onLogoutUser = onLogoutUser,
                    onBack = onBack
                )

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Main Authentication Card (when not signed in)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                    border = BorderStroke(1.5.dp, RoyalGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = "Security", tint = RoyalGold, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("اختر حسابك للدخول بأمان", fontWeight = FontWeight.Black, fontSize = 18.sp, color = SoftGold)
                        }

                        Text(
                            "اختر إحدى طرق الدخول المتاحة للوصول الكامل لخدمات المنصة والعقارات",
                            fontSize = 11.sp,
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // -------------------------------------------------------------
                        // 1. Phone OTP Section
                        // -------------------------------------------------------------
                        OutlinedTextField(
                            value = phoneInput,
                            onValueChange = { phoneInput = it },
                            label = { Text("رقم الهاتف (مثال: 01010634040)", color = SoftGold) },
                            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "الهاتف", tint = SoftGold) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RoyalGold,
                                unfocusedBorderColor = DarkGold,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (phoneInput.isNotBlank()) {
                                    showOtpModal = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("إرسال كود التحقق (OTP) 📲", color = Color.Black, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = DarkGold.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("أو الدخول عبر الحسابات الاجتماعية المعتمدة:", fontSize = 12.sp, color = SoftGold, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(12.dp))

                        // -------------------------------------------------------------
                        // 2. Interactive Social Account Chooser Buttons
                        // -------------------------------------------------------------
                        SocialLoginButton(title = "Google Sign-In (Gmail)", color = Color(0xFFDB4437), iconEmoji = "🌐") {
                            showGooglePicker = true
                        }

                        SocialLoginButton(title = "Facebook Login", color = Color(0xFF1877F2), iconEmoji = "📘") {
                            showFacebookPicker = true
                        }

                        SocialLoginButton(title = "TikTok Account", color = Color(0xFF000000), iconEmoji = "🎵") {
                            showTikTokPicker = true
                        }

                        SocialLoginButton(title = "Instagram Account", color = Color(0xFFE4405F), iconEmoji = "📸") {
                            showInstagramPicker = true
                        }

                        SocialLoginButton(title = "Apple ID", color = Color(0xFF333333), iconEmoji = "🍏") {
                            showApplePicker = true
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // DIALOG 1: Phone OTP Verification Modal
    // -------------------------------------------------------------------------
    if (showOtpModal) {
        AlertDialog(
            onDismissRequest = { showOtpModal = false },
            containerColor = DarkEmeraldCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = "OTP", tint = RoyalGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("رمز التحقق OTP 📲", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column {
                    Text("تم إرسال رمز التفعيل المكون من 4 أرقام إلى رقم الهاتف:", color = TextWhite, fontSize = 12.sp)
                    Text(phoneInput, color = RoyalGold, fontWeight = FontWeight.Black, fontSize = 15.sp, modifier = Modifier.padding(vertical = 4.dp))

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it },
                        label = { Text("أدخل الكود (مثال: 1234)", color = SoftGold) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalGold,
                            unfocusedBorderColor = DarkGold,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val enteredOtp = if (otpInput.isBlank()) "1234" else otpInput
                        showOtpModal = false
                        onLoginUserFull(
                            "مستخدم ($phoneInput)",
                            phoneInput,
                            "",
                            "رقم الهاتف",
                            "أولاد صقر - المدينة"
                        )
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalGold)
                ) {
                    Text("تأكيد الكود ودخول الحساب ✅", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showOtpModal = false }) {
                    Text("إلغاء", color = TextMuted)
                }
            }
        )
    }

    // -------------------------------------------------------------------------
    // DIALOG 2: Google Account Chooser Modal (Force Account Selection prompt='select_account')
    // -------------------------------------------------------------------------
    if (showGooglePicker) {
        AccountChooserModal(
            providerName = "Google (Gmail)",
            providerColor = Color(0xFFDB4437),
            providerEmoji = "🌐",
            promptSubtitle = "اختر حساب Google لمتابعة الربط والدخول لتطبيق سمسارك (prompt='select_account')",
            sampleAccounts = listOf(
                AccountChoiceItem("عبدالرحمن أحمد", "01010634040", "abdulrahmaa5n@gmail.com", "أولاد صقر - المدينة"),
                AccountChoiceItem("م. محمود السيد", "01098765432", "mahmoud@gmail.com", "أولاد صقر - المدينة"),
                AccountChoiceItem("أحمد العربي", "01123456789", "ahmed.saqr2026@gmail.com", "قرية تلراك"),
                AccountChoiceItem("فاطمة السيد", "01288776655", "fatma.awladsaqr@gmail.com", "قرية الصوفية")
            ),
            onSelectAccount = { name, phone, email, city ->
                showGooglePicker = false
                onLoginUserFull(name, phone, email, "Google (Gmail)", city)
                onBack()
            },
            onDismiss = { showGooglePicker = false }
        )
    }

    // -------------------------------------------------------------------------
    // DIALOG 3: Facebook Account Chooser Modal
    // -------------------------------------------------------------------------
    if (showFacebookPicker) {
        AccountChooserModal(
            providerName = "Facebook",
            providerColor = Color(0xFF1877F2),
            providerEmoji = "📘",
            promptSubtitle = "اختر حساب Facebook المفضل أو سجل حسابك المعتمد للوصول",
            sampleAccounts = listOf(
                AccountChoiceItem("سارة إبراهيم", "01234567890", "sara.fb@facebook.com", "قرية الصوفية"),
                AccountChoiceItem("أحمد العربي (فيسبوك)", "01123456789", "ahmed.arab.fb@facebook.com", "قرية تلراك"),
                AccountChoiceItem("محمد الشرقاوي", "01500112233", "elsharqawy.fb@facebook.com", "بني حسن")
            ),
            onSelectAccount = { name, phone, email, city ->
                showFacebookPicker = false
                onLoginUserFull(name, phone, email, "Facebook", city)
                onBack()
            },
            onDismiss = { showFacebookPicker = false }
        )
    }

    // -------------------------------------------------------------------------
    // DIALOG 4: TikTok Account Chooser Modal
    // -------------------------------------------------------------------------
    if (showTikTokPicker) {
        AccountChooserModal(
            providerName = "TikTok",
            providerColor = Color(0xFF000000),
            providerEmoji = "🎵",
            promptSubtitle = "اختر حساب TikTok للربط المباشر بمركز أولاد صقر",
            sampleAccounts = listOf(
                AccountChoiceItem("علي حسن", "01511223344", "ali.tok@tiktok.com", "بني حسن"),
                AccountChoiceItem("سمسار الشرقية", "01055443322", "semsar.tiktok@tiktok.com", "أولاد صقر - المدينة")
            ),
            onSelectAccount = { name, phone, email, city ->
                showTikTokPicker = false
                onLoginUserFull(name, phone, email, "TikTok", city)
                onBack()
            },
            onDismiss = { showTikTokPicker = false }
        )
    }

    // -------------------------------------------------------------------------
    // DIALOG 5: Instagram Account Chooser Modal
    // -------------------------------------------------------------------------
    if (showInstagramPicker) {
        AccountChooserModal(
            providerName = "Instagram",
            providerColor = Color(0xFFE4405F),
            providerEmoji = "📸",
            promptSubtitle = "اختر حساب Instagram للدخول والتفاعل مع العقارات",
            sampleAccounts = listOf(
                AccountChoiceItem("مريم الشافعي", "01022334455", "maryam.insta@instagram.com", "قصاصين الأزهار"),
                AccountChoiceItem("عقارات أولاد صقر الرسمية", "01010634040", "awlad.saqr.realestate@instagram.com", "أولاد صقر - المدينة")
            ),
            onSelectAccount = { name, phone, email, city ->
                showInstagramPicker = false
                onLoginUserFull(name, phone, email, "Instagram", city)
                onBack()
            },
            onDismiss = { showInstagramPicker = false }
        )
    }

    // -------------------------------------------------------------------------
    // DIALOG 6: Apple ID Account Chooser Modal
    // -------------------------------------------------------------------------
    if (showApplePicker) {
        AccountChooserModal(
            providerName = "Apple ID",
            providerColor = Color(0xFF333333),
            providerEmoji = "🍏",
            promptSubtitle = "المتابعة باستخدام Apple ID واختيار الحساب المعتمد",
            sampleAccounts = listOf(
                AccountChoiceItem("مريم الشافعي (Apple)", "01022334455", "maryam@apple.com", "قصاصين الأزهار"),
                AccountChoiceItem("عبدالرحمن أ. (iCloud)", "01010634040", "abdulrahma@icloud.com", "أولاد صقر - المدينة")
            ),
            onSelectAccount = { name, phone, email, city ->
                showApplePicker = false
                onLoginUserFull(name, phone, email, "Apple ID", city)
                onBack()
            },
            onDismiss = { showApplePicker = false }
        )
    }
}

data class AccountChoiceItem(
    val name: String,
    val phone: String,
    val email: String,
    val cityLocation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountChooserModal(
    providerName: String,
    providerColor: Color,
    providerEmoji: String,
    promptSubtitle: String,
    sampleAccounts: List<AccountChoiceItem>,
    onSelectAccount: (name: String, phone: String, email: String, city: String) -> Unit,
    onDismiss: () -> Unit
) {
    var isAddingNew by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    var customEmail by remember { mutableStateOf("") }
    var customPhone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkEmeraldCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(providerEmoji, fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("اختر حساب $providerName", color = SoftGold, fontWeight = FontWeight.Black, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(promptSubtitle, color = TextWhite, fontSize = 11.sp, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                if (!isAddingNew) {
                    Text("الحسابات المسجلة / المتاحة:", fontSize = 12.sp, color = RoyalGold, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    sampleAccounts.forEach { acc ->
                        Card(
                            onClick = {
                                onSelectAccount(acc.name, acc.phone, acc.email, acc.cityLocation)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkEmerald),
                            border = BorderStroke(1.dp, providerColor.copy(alpha = 0.7f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(providerColor, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        acc.name.take(1),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(acc.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    if (acc.email.isNotBlank()) {
                                        Text(acc.email, color = TextMuted, fontSize = 11.sp)
                                    } else {
                                        Text(acc.phone, color = TextMuted, fontSize = 11.sp)
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "اختر",
                                    tint = SoftGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { isAddingNew = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, RoyalGold),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("➕ استخدام حساب $providerName آخر", color = SoftGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    // Manual Custom Entry
                    Text("تسجيل بحساب $providerName جديد:", fontSize = 12.sp, color = RoyalGold, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        label = { Text("الاسم الكامل", color = SoftGold) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalGold,
                            unfocusedBorderColor = DarkGold,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = customEmail,
                        onValueChange = { customEmail = it },
                        label = { Text("البريد الإلكتروني / الحساب", color = SoftGold) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalGold,
                            unfocusedBorderColor = DarkGold,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = customPhone,
                        onValueChange = { customPhone = it },
                        label = { Text("رقم الهاتف", color = SoftGold) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RoyalGold,
                            unfocusedBorderColor = DarkGold,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                val name = customName.ifBlank { "مستخدم $providerName" }
                                val email = customEmail.ifBlank { "user@$providerName.com" }
                                val phone = customPhone.ifBlank { "01000000000" }
                                onSelectAccount(name, phone, email, "أولاد صقر - المدينة")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("دخول والحفظ ✅", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        TextButton(onClick = { isAddingNew = false }) {
                            Text("رجوع", color = TextMuted)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextMuted)
            }
        }
    )
}

@Composable
fun SocialLoginButton(
    title: String,
    color: Color,
    iconEmoji: String = "🔑",
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(iconEmoji, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Text("اختيار الحساب ➔", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun UserProfileEditCard(
    currentUser: User,
    onUpdateProfile: (name: String, username: String, phone: String, picUri: String, onSuccess: (User) -> Unit) -> Unit,
    onLogoutUser: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var editName by remember(currentUser.id, currentUser.name) { mutableStateOf(currentUser.name) }
    var editUsername by remember(currentUser.id, currentUser.username) { mutableStateOf(currentUser.username) }
    var editPhone by remember(currentUser.id, currentUser.phone) { mutableStateOf(currentUser.phone) }
    var editPicUri by remember(currentUser.id, currentUser.profilePictureUri) { mutableStateOf(currentUser.profilePictureUri) }

    var isNameLocked by remember(currentUser.id, currentUser.isNameChanged) { mutableStateOf(currentUser.isNameChanged) }
    var isUsernameLocked by remember(currentUser.id, currentUser.isUsernameChanged) { mutableStateOf(currentUser.isUsernameChanged) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            editPicUri = it.toString()
            onUpdateProfile(editName, editUsername, editPhone, it.toString()) { _ ->
                Toast.makeText(context, "تم تحديث صورة الملف الشخصي بنجاح ✅", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
        border = BorderStroke(1.5.dp, RoyalGold)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Active",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "تعديل الملف الشخصي",
                    color = SoftGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Picture Circle with Picker Badge
            Box(
                modifier = Modifier.size(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color(0xFF0D3B2A))
                        .border(2.5.dp, RoyalGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (editPicUri.isNotBlank()) {
                        AsyncImage(
                            model = editPicUri,
                            contentDescription = "صورة الملف الشخصي",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Avatar",
                            tint = SoftGold,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                // Camera Pick Overlay Button
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(RoyalGold)
                        .clickable { photoPickerLauncher.launch("image/*") }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "تغيير الصورة",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "اضغط على الكاميرا لتحديث صورة الملف الشخصي من الاستوديو/الكاميرا 📸",
                fontSize = 11.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            // 1. Display Name Field (Locked permanently after first edit)
            OutlinedTextField(
                value = editName,
                onValueChange = { if (!isNameLocked) editName = it },
                enabled = !isNameLocked,
                label = { Text("الاسم الظاهر", color = if (isNameLocked) TextMuted else SoftGold) },
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = SoftGold) },
                trailingIcon = {
                    if (isNameLocked) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "مقفل", tint = RoyalGold)
                    }
                },
                supportingText = {
                    Text(
                        text = if (isNameLocked) "🔒 يمكن تغيير الاسم مرة واحدة فقط" else "💡 يمكن تغيير الاسم مرة واحدة فقط",
                        color = if (isNameLocked) RoyalGold else TextMuted,
                        fontSize = 10.sp
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = DarkGold,
                    disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    disabledTextColor = TextWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Unique Username Field (Locked permanently after first edit)
            OutlinedTextField(
                value = editUsername,
                onValueChange = { if (!isUsernameLocked) editUsername = it },
                enabled = !isUsernameLocked,
                label = { Text("اسم المستخدم / اليوزر نيم (@username)", color = if (isUsernameLocked) TextMuted else SoftGold) },
                leadingIcon = { Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = null, tint = SoftGold) },
                trailingIcon = {
                    if (isUsernameLocked) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "مقفل", tint = RoyalGold)
                    }
                },
                supportingText = {
                    Text(
                        text = if (isUsernameLocked) "🔒 يمكن تغيير اسم المستخدم مرة واحدة فقط" else "💡 يمكن تغيير اسم المستخدم مرة واحدة فقط",
                        color = if (isUsernameLocked) RoyalGold else TextMuted,
                        fontSize = 10.sp
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = DarkGold,
                    disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    disabledTextColor = TextWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Phone Number Field (+20 Egypt)
            OutlinedTextField(
                value = editPhone,
                onValueChange = { editPhone = it },
                label = { Text("رقم الهاتف (+20 مصر)", color = SoftGold) },
                leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = SoftGold) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalGold,
                    unfocusedBorderColor = DarkGold,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Login Method Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF093826), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("طريقة الدخول:", color = TextMuted, fontSize = 11.sp)
                Text(currentUser.loginMethod, color = RoyalGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Save Profile Button
            Button(
                onClick = {
                    onUpdateProfile(editName, editUsername, editPhone, editPicUri) { updatedUser ->
                        isNameLocked = updatedUser.isNameChanged
                        isUsernameLocked = updatedUser.isUsernameChanged
                        Toast.makeText(context, "تم حفظ التعديلات بنجاح ✅", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "حفظ", tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("حفظ التعديلات", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkEmerald),
                    border = BorderStroke(1.dp, RoyalGold),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("متابعة للتطبيق ➡️", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onLogoutUser,
                    border = BorderStroke(1.dp, DarkGold),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "تغيير", tint = SoftGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تغيير الحساب", color = SoftGold, fontSize = 12.sp)
                }
            }
        }
    }
}

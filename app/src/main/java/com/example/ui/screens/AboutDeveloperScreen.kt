package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.DarkEmeraldCard
import com.example.ui.theme.DarkGold
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.SoftGold
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkEmerald,
        topBar = {
            TopAppBar(
                title = { Text("عن التطبيق والمطور", fontWeight = FontWeight.Bold, color = SoftGold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = TextWhite)
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Emblem Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(2.dp, RoyalGold)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🐎", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("سمسارك في أولاد صقر", fontWeight = FontWeight.Black, fontSize = 22.sp, color = SoftGold)
                    Text("تطبيق العقارات الموثوق لمركز أولاد صقر بمحافظة الشرقية", fontSize = 12.sp, color = TextMuted, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Developer Section Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "تم إنشاء وتصميم هذا التطبيق بواسطة",
                        fontSize = 14.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Abdulrahman Elsayed\n(عبدالرحمن السيد)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = SoftGold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Roles List
                    RoleChip(icon = Icons.Default.Code, text = "مبرمج ومصمم مواقع وتطبيقات")
                    RoleChip(icon = Icons.Default.VideoLibrary, text = "فني مونتاج وصناعة محتوى")
                    RoleChip(icon = Icons.Default.Security, text = "مهندس في الأمن والحماية واختبار الاختراق")

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personal Facebook Profile Button
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/abdelrahm5n"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("الصفحة الشخصية للمطور على فيسبوك 👤", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Agency & Contact Routing Matrix Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkEmeraldCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "إدارة وسماسر المكتب الرسمي:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftGold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "مدير العقارات: أ/ عبدالرحمن",
                        fontSize = 15.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Call Button
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01010634040"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalGold),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = "اتصل بنا", tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("اتصل بنا: 01010634040", color = Color.Black, fontWeight = FontWeight.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // WhatsApp Button
                    Button(
                        onClick = {
                            val waMsg = Uri.encode("أهلاً بك، أريد الاستفسار عن عقار في تطبيق سمسارك")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/201010634040?text=$waMsg"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("💬 تواصل واتساب المباشر: 01010634040", color = Color.White, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = DarkGold.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "صفحاتنا الرسمية على منصات التواصل:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SoftGold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SocialRow(label = "صفحة فيسبوك الرسمية", url = "https://www.facebook.com/semsark2", color = Color(0xFF1877F2), context = context)
                    SocialRow(label = "حساب تيك توك الرسمي", url = "https://www.tiktok.com/@semsark2", color = Color(0xFF000000), context = context)
                    SocialRow(label = "حساب إنستجرام الرسمي", url = "https://www.instagram.com/seemsark2", color = Color(0xFFE4405F), context = context)
                }
            }
        }
    }
}

@Composable
fun RoleChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = DarkEmerald,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGold)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text, tint = SoftGold, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun SocialRow(label: String, url: String, color: Color, context: android.content.Context) {
    Surface(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Icon(imageVector = Icons.Default.Language, contentDescription = label, tint = Color.White, modifier = Modifier.size(18.dp))
        }
    }
}

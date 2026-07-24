package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.R

private val DarkGreenBg = Color(0xFF071F17)
private val ElegantGold = Color(0xFFD4AF37)

data class SocialContactItem(
    val drawableRes: Int,
    val contentDescription: String,
    val onClick: (Context) -> Unit
)

@Composable
fun SocialContactRow(
    onTrackCallClick: () -> Unit = {},
    onTrackWhatsappClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val socialItems = listOf(
        SocialContactItem(
            drawableRes = R.drawable.call,
            contentDescription = "اتصال تلفوني مباشر",
            onClick = { ctx ->
                onTrackCallClick()
                try {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01010634040"))
                    ctx.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(ctx, "تعذر فتح لوحة الاتصال", Toast.LENGTH_SHORT).show()
                }
            }
        ),
        SocialContactItem(
            drawableRes = R.drawable.whatsapp,
            contentDescription = "محادثة واتساب",
            onClick = { ctx ->
                onTrackWhatsappClick()
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/201010634040"))
                    ctx.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(ctx, "تعذر فتح واتساب", Toast.LENGTH_SHORT).show()
                }
            }
        ),
        SocialContactItem(
            drawableRes = R.drawable.facebook,
            contentDescription = "صفحة الفيسبوك",
            onClick = { ctx ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/semsark2"))
                    ctx.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(ctx, "تعذر فتح فيسبوك", Toast.LENGTH_SHORT).show()
                }
            }
        ),
        SocialContactItem(
            drawableRes = R.drawable.tik_tok,
            contentDescription = "صفحة التيك توك",
            onClick = { ctx ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tiktok.com/@semsark2"))
                    ctx.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(ctx, "تعذر فتح تيك توك", Toast.LENGTH_SHORT).show()
                }
            }
        ),
        SocialContactItem(
            drawableRes = R.drawable.instagram,
            contentDescription = "صفحة الانستجرام",
            onClick = { ctx ->
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/seemsark2"))
                    ctx.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(ctx, "تعذر فتح انستجرام", Toast.LENGTH_SHORT).show()
                }
            }
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        socialItems.forEach { item ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DarkGreenBg)
                    .border(BorderStroke(1.5.dp, ElegantGold), CircleShape)
                    .clickable { item.onClick(context) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = item.drawableRes),
                    contentDescription = item.contentDescription,
                    tint = ElegantGold,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

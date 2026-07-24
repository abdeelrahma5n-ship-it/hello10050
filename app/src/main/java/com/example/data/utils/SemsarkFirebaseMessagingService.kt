package com.example.data.utils

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SemsarkFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data
        val targetTab = data["target_tab"] ?: "OFFICE"
        val senderName = data["sender"] ?: remoteMessage.notification?.title ?: "مكتب سمسارك"
        val messageBody = data["message"] ?: remoteMessage.notification?.body ?: "رسالة جديدة"
        val isAnnouncement = data["is_announcement"] == "true"

        if (targetTab == "GROUP") {
            NotificationHelper.sendGroupChatNotification(
                context = applicationContext,
                senderName = senderName,
                messageText = messageBody,
                isAnnouncement = isAnnouncement
            )
        } else {
            NotificationHelper.sendOfficeChatNotification(
                context = applicationContext,
                senderName = senderName,
                messageText = messageBody
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}

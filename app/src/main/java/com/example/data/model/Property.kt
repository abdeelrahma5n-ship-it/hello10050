package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PropertyCategory(val id: String, val titleArabic: String, val iconName: String) {
    HOUSES("houses", "منازل", "house"),
    BUILDING_CORDON("building_cordon", "أراضي كردون مباني", "map"),
    APARTMENTS_SALE("apartments_sale", "شقق تمليك", "apartment"),
    OUTSIDE_CORDON("outside_cordon", "أراضي خارج الكردون", "landscape"),
    APARTMENTS_RENT("apartments_rent", "شقق إيجار", "key"),
    AGRICULTURAL("agricultural", "أراضي زراعية", "grass")
}

enum class DealType(val titleArabic: String) {
    SALE("بيع"),
    RENT("إيجار"),
    BUY_REQUEST("طلب شراء")
}

enum class PropertyStatus(val titleArabic: String) {
    PENDING("قيد المراجعة"),
    APPROVED("تم الموافقة والمشر"),
    REJECTED("مرفوض")
}

@Entity(tableName = "properties")
data class Property(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val categoryId: String,
    val priceEgp: Double,
    val isNegotiable: Boolean = true,
    val areaSqm: Double,
    val dimensions: String = "",
    val floorNumber: String = "",
    val legalStatus: String = "عقد مسجل / ملكية رسمية",
    val dealType: String = DealType.SALE.titleArabic,
    val villageArea: String, // e.g. "أولاد صقر - المدينة", "تلراك", "الصوفية", "بني حسن", "قصاصين الأزهار", "زور أبو الليل", "كفر الفرايحة"
    val addressDetails: String,
    val description: String,
    val imageUrls: String, // Comma separated photo URLs
    val videoUrl: String = "",
    val contactPhone: String = "01010634040",
    val contactWhatsapp: String = "201010634040",
    val ownerName: String = "أ/ عبدالرحمن",
    val status: String = PropertyStatus.APPROVED.titleArabic,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

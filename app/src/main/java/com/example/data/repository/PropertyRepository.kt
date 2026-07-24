package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.PropertyDao
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import com.example.data.model.PropertyStatus
import com.example.data.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PropertyRepository(private val propertyDao: PropertyDao, private val context: Context) {

    val allProperties: Flow<List<Property>> = propertyDao.getAllProperties()
    val approvedProperties: Flow<List<Property>> = propertyDao.getApprovedProperties()
    val pendingProperties: Flow<List<Property>> = propertyDao.getPendingProperties()
    val favoriteProperties: Flow<List<Property>> = propertyDao.getFavoriteProperties()

    fun getPropertiesByCategory(categoryId: String): Flow<List<Property>> {
        return propertyDao.getPropertiesByCategory(categoryId)
    }

    suspend fun getPropertyById(id: Long): Property? = withContext(Dispatchers.IO) {
        propertyDao.getPropertyById(id)
    }

    suspend fun submitPropertyByUser(property: Property): Long = withContext(Dispatchers.IO) {
        // User submissions default to PENDING status
        val pendingProp = property.copy(
            status = PropertyStatus.PENDING.titleArabic
        )
        propertyDao.insertProperty(pendingProp)
    }

    suspend fun addDirectPropertyByAdmin(property: Property): Long = withContext(Dispatchers.IO) {
        val activeProp = property.copy(
            status = PropertyStatus.APPROVED.titleArabic
        )
        val id = propertyDao.insertProperty(activeProp)
        NotificationHelper.showNotification(
            context,
            "🏠 عقار جديد في أولاد صقر: ${property.title}",
            "السعر: ${property.priceEgp.toInt()} ج.م - المنطقة: ${property.villageArea} - اضغط للتفاصيل والفيديو"
        )
        id
    }

    suspend fun approvePropertyByAdmin(id: Long) = withContext(Dispatchers.IO) {
        propertyDao.updateStatus(id, PropertyStatus.APPROVED.titleArabic)
        val prop = propertyDao.getPropertyById(id)
        if (prop != null) {
            // Trigger FCM / Local Notification to all app users
            NotificationHelper.showNotification(
                context,
                "🏠 عقار جديد في أولاد صقر: ${prop.title}",
                "السعر: ${prop.priceEgp.toInt()} ج.م - المنطقة: ${prop.villageArea} - اضغط للتفاصيل والفيديو"
            )
        }
    }

    suspend fun rejectPropertyByAdmin(id: Long, reason: String = "") = withContext(Dispatchers.IO) {
        propertyDao.updateStatus(id, PropertyStatus.REJECTED.titleArabic)
        val prop = propertyDao.getPropertyById(id)
        val reasonText = if (reason.isNotBlank()) "السبب: $reason" else "يرجى مراجعة المواصفات والصور والتقديم مجدداً"
        NotificationHelper.showNotification(
            context,
            "تحديث بخصوص طلب عقارك: ${prop?.title ?: ""}",
            "تم رفض نشر العقار. $reasonText"
        )
    }

    suspend fun updatePropertyStatus(id: Long, newStatus: String) = withContext(Dispatchers.IO) {
        propertyDao.updateStatus(id, newStatus)
    }

    suspend fun updatePropertyDetails(property: Property) = withContext(Dispatchers.IO) {
        propertyDao.updateProperty(property)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        propertyDao.setFavorite(id, isFavorite)
    }

    suspend fun deleteProperty(property: Property) = withContext(Dispatchers.IO) {
        propertyDao.deleteProperty(property)
    }

    suspend fun seedSampleDataIfEmpty() = withContext(Dispatchers.IO) {
        if (propertyDao.getCount() == 0) {
            val sampleProperties = listOf(
                Property(
                    title = "منزل عائلي دورين تشطيب سوبر لوكس",
                    categoryId = PropertyCategory.HOUSES.id,
                    priceEgp = 1850000.0,
                    isNegotiable = true,
                    areaSqm = 160.0,
                    dimensions = "10m x 16m",
                    floorNumber = "دورين + أرضي",
                    legalStatus = "عقد ملكية حيازة نهائية مسجل",
                    dealType = "بيع",
                    villageArea = "أولاد صقر - حي السلام",
                    addressDetails = "بالقرب من الموقف الجديد ومدرسة الثانوي",
                    description = "منزل دورين كامل الخدمات (كهرباء، مياه، غاز) تشطيب سوبر لوكس جاهز للسكن مباشرة، واجهة بحرية شارع 10 متر.",
                    imageUrls = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914,https://images.unsplash.com/photo-1512917774080-9991f1c4c750",
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                ),
                Property(
                    title = "قطعة أرض كردون مباني موقع مميز جداً",
                    categoryId = PropertyCategory.BUILDING_CORDON.id,
                    priceEgp = 950000.0,
                    isNegotiable = true,
                    areaSqm = 175.0,
                    dimensions = "12.5m x 14m",
                    floorNumber = "أرضي بناء",
                    legalStatus = "داخل كردون المباني الرسمي - رخصة جاهزة",
                    dealType = "بيع",
                    villageArea = "قرية تلراك - أولاد صقر",
                    addressDetails = "على الطريق الرئيسي المباشر أمام المجمع الخدمي",
                    description = "قطعة أرض متميزة داخل الكردون، مربعة الشكل، واجهة 12.5 متر على الشارع الرئيسي، كاملة المرافق وجاهزة للبناء الفوري.",
                    imageUrls = "https://images.unsplash.com/photo-1500382017468-9049fed747ef,https://images.unsplash.com/photo-1524813686514-a57563d77965",
                    videoUrl = "https://www.w3schools.com/html/mov_bbb.mp4",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                ),
                Property(
                    title = "شقة تمليك 135م² تشطيب حديث دور ثانٍ",
                    categoryId = PropertyCategory.APARTMENTS_SALE.id,
                    priceEgp = 720000.0,
                    isNegotiable = false,
                    areaSqm = 135.0,
                    dimensions = "3 غرف + ريسبشن كبير + 2 حمام + مطبخ",
                    floorNumber = "الدور الثاني علوي",
                    legalStatus = "حصة في الأرض وسند ملكية مسجل",
                    dealType = "بيع",
                    villageArea = "أولاد صقر - وسط البلد",
                    addressDetails = "شارع مجلس المدينة القديم بجوار البنك",
                    description = "شقة تمليك ممتازة جداً، 3 غرف نوم ورسبشن واسع، تشطيب سيراميك وسباكة وكهرباء على أعلى مستوى، عداد كهرباء قديم ومياه مستقلم.",
                    imageUrls = "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688,https://images.unsplash.com/photo-1560448204-e02f11c3d0e2",
                    videoUrl = "",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                ),
                Property(
                    title = "أرض خارج الكردون قريبة جداً من البناء",
                    categoryId = PropertyCategory.OUTSIDE_CORDON.id,
                    priceEgp = 420000.0,
                    isNegotiable = true,
                    areaSqm = 200.0,
                    dimensions = "10m x 20m",
                    floorNumber = "أرض فضاء",
                    legalStatus = "عقد ابتدائي متسلسل الملكية",
                    dealType = "بيع",
                    villageArea = "قرية الصوفية - أولاد صقر",
                    addressDetails = "ملاصقة للبيوت والخدمات 50 متر عن الكردون",
                    description = "فرصة استثمارية واعدة جداً، قطعة أرض 200 متر متتاخمة للكتلة السكنية الحالية وتدخل الكردون القادم بإذن الله.",
                    imageUrls = "https://images.unsplash.com/photo-1500382017468-9049fed747ef",
                    videoUrl = "",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                ),
                Property(
                    title = "شقة إيجار مفروشة للإيجار الشهري",
                    categoryId = PropertyCategory.APARTMENTS_RENT.id,
                    priceEgp = 2500.0,
                    isNegotiable = true,
                    areaSqm = 110.0,
                    dimensions = "غرفتين + صالة + حمام + مطبخ",
                    floorNumber = "الدور الأول علوي",
                    legalStatus = "عقد إيجار محدد المدة",
                    dealType = "إيجار",
                    villageArea = "أولاد صقر - حي الزهور",
                    addressDetails = "قريبة من المستشفى العام والخدمات",
                    description = "شقة إيجار عائلية مفروشة بجميع الأجهزة الكهربائية والأثاث، تهوية ممتازة وشارع هادئ ومريح.",
                    imageUrls = "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267",
                    videoUrl = "",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                ),
                Property(
                    title = "أرض زراعية خصبة 2 فدان ري بحاري",
                    categoryId = PropertyCategory.AGRICULTURAL.id,
                    priceEgp = 1200000.0,
                    isNegotiable = true,
                    areaSqm = 8400.0,
                    dimensions = "2 فدان زراعي",
                    floorNumber = "أرض زراعية",
                    legalStatus = "حيازة زراعية منتظمة وكارت فلاح",
                    dealType = "بيع",
                    villageArea = "قصاصين الأزهار - أولاد صقر",
                    addressDetails = "على ترعة الممر الرئيسي للري",
                    description = "أرض زراعية فائقة الجودة، تربة سمراء منتجة لجميع المحاصيل الزراعية، ري بحاري منتظم مع فتحة ري خاصة وطريق خدمة واسع.",
                    imageUrls = "https://images.unsplash.com/photo-1500382017468-9049fed747ef,https://images.unsplash.com/photo-1500937386664-56d1dfef3854",
                    videoUrl = "",
                    contactPhone = "01010634040",
                    contactWhatsapp = "201010634040",
                    ownerName = "أ/ عبدالرحمن",
                    status = PropertyStatus.APPROVED.titleArabic
                )
            )

            for (prop in sampleProperties) {
                propertyDao.insertProperty(prop)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: PropertyRepository? = null

        fun getInstance(context: Context): PropertyRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val instance = PropertyRepository(db.propertyDao(), context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}

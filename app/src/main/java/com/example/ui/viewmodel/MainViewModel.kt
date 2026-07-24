package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Property
import com.example.data.model.PropertyCategory
import com.example.data.remote.GeminiClient
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.data.model.User

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val senderName: String = "المساعد الذكي",
    val text: String,
    val isUser: Boolean = false,
    val isManager: Boolean = false,
    val isAdmin: Boolean = false,
    val timestampStr: String = "الآن",
    val timestamp: Long = System.currentTimeMillis(),
    val mediaUrl: String? = null,
    val mediaType: String? = null, // "IMAGE", "VIDEO", "PROPERTY_LINK"
    val isAnnouncement: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PropertyRepository.getInstance(application)

    // Senior Citizens Mode Toggle
    private val _isSeniorMode = MutableStateFlow(false)
    val isSeniorMode: StateFlow<Boolean> = _isSeniorMode.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Active Category Filter
    private val _selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedCategoryId: StateFlow<String?> = _selectedCategoryId.asStateFlow()

    // Village / Area Filter
    private val _selectedVillage = MutableStateFlow("الكل")
    val selectedVillage: StateFlow<String> = _selectedVillage.asStateFlow()

    // Deal Type Filter (الكل / بيع / إيجار)
    private val _selectedDealType = MutableStateFlow("الكل")
    val selectedDealType: StateFlow<String> = _selectedDealType.asStateFlow()

    // Sort Option Filter (الأحدث / الأقل سعراً / الأعلى سعراً)
    private val _selectedSortOption = MutableStateFlow("الأحدث")
    val selectedSortOption: StateFlow<String> = _selectedSortOption.asStateFlow()

    // Advanced Filters: Price & Area & Posting Date
    private val _minPrice = MutableStateFlow<Double?>(null)
    val minPrice: StateFlow<Double?> = _minPrice.asStateFlow()

    private val _maxPrice = MutableStateFlow<Double?>(null)
    val maxPrice: StateFlow<Double?> = _maxPrice.asStateFlow()

    private val _minArea = MutableStateFlow<Double?>(null)
    val minArea: StateFlow<Double?> = _minArea.asStateFlow()

    private val _maxArea = MutableStateFlow<Double?>(null)
    val maxArea: StateFlow<Double?> = _maxArea.asStateFlow()

    private val _areaUnit = MutableStateFlow("م²") // "م²", "قيراط", "فدان"
    val areaUnit: StateFlow<String> = _areaUnit.asStateFlow()

    private val _postingDateFilter = MutableStateFlow("الكل") // "الكل", "اليوم", "هذا الأسبوع", "هذا الشهر"
    val postingDateFilter: StateFlow<String> = _postingDateFilter.asStateFlow()

    // Property Comparison State (Up to 3 properties)
    private val _comparedPropertyIds = MutableStateFlow<Set<Long>>(emptySet())
    val comparedPropertyIds: StateFlow<Set<Long>> = _comparedPropertyIds.asStateFlow()

    // All Properties for Admin
    val allProperties: StateFlow<List<Property>> = repository.allProperties
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // All Approved Properties with Advanced Filters
    val approvedProperties: StateFlow<List<Property>> = combine(
        repository.approvedProperties,
        _searchQuery,
        _selectedCategoryId,
        _selectedVillage,
        _selectedDealType,
        _selectedSortOption,
        _minPrice,
        _maxPrice,
        _minArea,
        _maxArea,
        _postingDateFilter
    ) { flows: Array<Any?> ->
        @Suppress("UNCHECKED_CAST")
        val properties = flows[0] as List<Property>
        val query = flows[1] as String
        val catId = flows[2] as String?
        val village = flows[3] as String
        val dealType = flows[4] as String
        val sortOption = flows[5] as String
        val minP = flows[6] as Double?
        val maxP = flows[7] as Double?
        val minA = flows[8] as Double?
        val maxA = flows[9] as Double?
        val postingDate = flows[10] as String

        val now = System.currentTimeMillis()

        val filteredList = properties.filter { prop ->
            val matchesQuery = query.isBlank() ||
                    prop.title.contains(query, ignoreCase = true) ||
                    prop.villageArea.contains(query, ignoreCase = true) ||
                    prop.description.contains(query, ignoreCase = true) ||
                    prop.addressDetails.contains(query, ignoreCase = true)

            val matchesCategory = catId == null || prop.categoryId == catId
            val matchesVillage = village == "الكل" || prop.villageArea.contains(village)
            val matchesDealType = dealType == "الكل" || prop.dealType.contains(dealType, ignoreCase = true)

            val matchesMinPrice = minP == null || prop.priceEgp >= minP
            val matchesMaxPrice = maxP == null || prop.priceEgp <= maxP

            val matchesMinArea = minA == null || prop.areaSqm >= minA
            val matchesMaxArea = maxA == null || prop.areaSqm <= maxA

            val matchesDate = when (postingDate) {
                "اليوم" -> (now - prop.createdAt) <= 24 * 3600 * 1000L
                "هذا الأسبوع" -> (now - prop.createdAt) <= 7 * 24 * 3600 * 1000L
                "هذا الشهر" -> (now - prop.createdAt) <= 30 * 24 * 3600 * 1000L
                else -> true
            }

            matchesQuery && matchesCategory && matchesVillage && matchesDealType &&
                    matchesMinPrice && matchesMaxPrice && matchesMinArea && matchesMaxArea && matchesDate
        }

        when (sortOption) {
            "الأقل سعراً" -> filteredList.sortedBy { it.priceEgp }
            "الأعلى سعراً" -> filteredList.sortedByDescending { it.priceEgp }
            else -> filteredList.sortedByDescending { it.id }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Derived List of Compared Properties (up to 3)
    val comparedProperties: StateFlow<List<Property>> = combine(
        allProperties,
        _comparedPropertyIds
    ) { allProps, ids ->
        allProps.filter { ids.contains(it.id) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Favorite Properties
    val favoriteProperties: StateFlow<List<Property>> = repository.favoriteProperties
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Admin Pending Properties
    val pendingProperties: StateFlow<List<Property>> = repository.pendingProperties
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Analytics Counter
    private val _callClicksCount = MutableStateFlow(34)
    val callClicksCount: StateFlow<Int> = _callClicksCount.asStateFlow()

    private val _whatsappClicksCount = MutableStateFlow(58)
    val whatsappClicksCount: StateFlow<Int> = _whatsappClicksCount.asStateFlow()

    // Registered Users Management
    private val _registeredUsers = MutableStateFlow<List<User>>(
        listOf(
            User(id = 1, name = "محمود السيد", phone = "01098765432", email = "mahmoud@gmail.com", loginMethod = "Google (Gmail)", isBlocked = false, cityLocation = "أولاد صقر - المدينة"),
            User(id = 2, name = "أحمد العربي", phone = "01123456789", email = "", loginMethod = "رقم الهاتف", isBlocked = false, cityLocation = "قرية تلراك"),
            User(id = 3, name = "سارة إبراهيم", phone = "01234567890", email = "sara.fb@facebook.com", loginMethod = "Facebook", isBlocked = false, cityLocation = "قرية الصوفية"),
            User(id = 4, name = "علي حسن", phone = "01511223344", email = "ali.tok@tiktok.com", loginMethod = "TikTok", isBlocked = false, cityLocation = "بني حسن"),
            User(id = 5, name = "مريم الشافعي", phone = "01022334455", email = "maryam@apple.com", loginMethod = "Apple ID", isBlocked = false, cityLocation = "قصاصين الأزهار"),
            User(id = 6, name = "حساب زائف سبام", phone = "01000000000", email = "spam@fake.com", loginMethod = "Instagram", isBlocked = true, cityLocation = "غير معروف")
        )
    )
    val registeredUsers: StateFlow<List<User>> = _registeredUsers.asStateFlow()

    // Private Office Chat Messages (Tab 1)
    private val _officeChatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                senderName = "أ/ عبدالرحمن (مدير المكتب)",
                text = "أهلاً بك في مكتب سمسارك بـ أولاد صقر والشرقية! 🏢 كيف يمكننا مساعدتك اليوم؟ يمكنك استفسار عن الأسعار، طلب معاينات حية، أو تحدد موعد زيارة بالمكتب.",
                isUser = false,
                isManager = true,
                timestampStr = "10:00 ص"
            )
        )
    )
    val officeChatMessages: StateFlow<List<ChatMessage>> = _officeChatMessages.asStateFlow()

    // Community Group Chat Messages (Tab 2)
    private val _groupChatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                senderName = "أ/ عبدالرحمن (إدارة المنصة)",
                text = "📢 أهلاً بالجميع في المجموعة الرسمية لتطبيق سمسارك أولاد صقر! يُرجى الالتزام بالتعليمات والخاص متاح للاستفسارات المباشرة والمعاينات.",
                isUser = false,
                isAdmin = true,
                isManager = true,
                timestampStr = "أمس 09:30 م",
                isAnnouncement = true
            ),
            ChatMessage(
                senderName = "محمود السيد",
                text = "السلام عليكم، هل في شقق إيجار متاحة بالقرب من مدرسة أولاد صقر الثانوية؟",
                isUser = false,
                timestampStr = "اليوم 11:15 ص"
            ),
            ChatMessage(
                senderName = "أحمد العربي",
                text = "وعليكم السلام، أ/ عبدالرحمن نزل عقار جديد بالمنطقة يفضل التواصل مع المكتب مباشرة عبر الزر أعلى الصفحة.",
                isUser = false,
                timestampStr = "اليوم 11:20 ص"
            ),
            ChatMessage(
                senderName = "أ/ عبدالرحمن (إدارة المنصة)",
                text = "بالفعل، تم إضافة شقة طابق ثاني بـ حي السلام بأسعار مناسبة. يمكنكم الاطلاع على صور تفاصيلها المرفقة 🏠",
                isUser = false,
                isAdmin = true,
                isManager = true,
                timestampStr = "اليوم 11:35 ص",
                mediaUrl = "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=600",
                mediaType = "IMAGE"
            )
        )
    )
    val groupChatMessages: StateFlow<List<ChatMessage>> = _groupChatMessages.asStateFlow()

    // Floating AI Assistant Chat
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "أهلاً بك! أنا المساعد الذكي لتطبيق سمسارك في أولاد صقر. إسألني عن أسعار الأراضي، كردون المباني، القرى والمنازل المتاحة!",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Auth Status Simulation
    private val _isGuestMode = MutableStateFlow(true)
    val isGuestMode: StateFlow<Boolean> = _isGuestMode.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _userPhone = MutableStateFlow<String?>(null)
    val userPhone: StateFlow<String?> = _userPhone.asStateFlow()

    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    fun trackCallClick() {
        _callClicksCount.value += 1
    }

    fun trackWhatsappClick() {
        _whatsappClicksCount.value += 1
    }

    fun toggleSeniorMode() {
        _isSeniorMode.value = !_isSeniorMode.value
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(categoryId: String?) {
        _selectedCategoryId.value = categoryId
    }

    fun selectVillage(village: String) {
        _selectedVillage.value = village
    }

    fun selectDealType(dealType: String) {
        _selectedDealType.value = dealType
    }

    fun selectSortOption(sortOption: String) {
        _selectedSortOption.value = sortOption
    }

    fun setPriceRange(min: Double?, max: Double?) {
        _minPrice.value = min
        _maxPrice.value = max
    }

    fun setAreaRange(min: Double?, max: Double?, unit: String) {
        _minArea.value = min
        _maxArea.value = max
        _areaUnit.value = unit
    }

    fun setPostingDateFilter(filter: String) {
        _postingDateFilter.value = filter
    }

    fun resetAllFilters() {
        _searchQuery.value = ""
        _selectedCategoryId.value = null
        _selectedVillage.value = "الكل"
        _selectedDealType.value = "الكل"
        _selectedSortOption.value = "الأحدث"
        _minPrice.value = null
        _maxPrice.value = null
        _minArea.value = null
        _maxArea.value = null
        _areaUnit.value = "م²"
        _postingDateFilter.value = "الكل"
    }

    // Comparison Management
    fun toggleCompareProperty(propertyId: Long): Boolean {
        val currentSet = _comparedPropertyIds.value
        return if (currentSet.contains(propertyId)) {
            _comparedPropertyIds.value = currentSet - propertyId
            true
        } else {
            if (currentSet.size >= 3) {
                false // Cannot add more than 3
            } else {
                _comparedPropertyIds.value = currentSet + propertyId
                true
            }
        }
    }

    fun removeFromCompare(propertyId: Long) {
        _comparedPropertyIds.value = _comparedPropertyIds.value - propertyId
    }

    fun clearComparison() {
        _comparedPropertyIds.value = emptySet()
    }

    fun toggleFavorite(propertyId: Long, currentFavStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(propertyId, !currentFavStatus)
        }
    }

    fun submitNewProperty(property: Property, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.submitPropertyByUser(property)
            onComplete()
        }
    }

    fun addDirectPropertyByAdmin(property: Property, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.addDirectPropertyByAdmin(property)
            onComplete()
        }
    }

    fun approvePendingProperty(propertyId: Long) {
        viewModelScope.launch {
            repository.approvePropertyByAdmin(propertyId)
        }
    }

    fun rejectPendingProperty(propertyId: Long, reason: String = "") {
        viewModelScope.launch {
            repository.rejectPropertyByAdmin(propertyId, reason)
        }
    }

    fun updatePropertyStatus(propertyId: Long, newStatus: String) {
        viewModelScope.launch {
            repository.updatePropertyStatus(propertyId, newStatus)
        }
    }

    fun updatePropertyDetails(property: Property) {
        viewModelScope.launch {
            repository.updatePropertyDetails(property)
        }
    }

    fun deleteProperty(property: Property) {
        viewModelScope.launch {
            repository.deleteProperty(property)
        }
    }

    fun toggleBlockUser(userId: Long) {
        val updatedList = _registeredUsers.value.map { user ->
            if (user.id == userId) user.copy(isBlocked = !user.isBlocked) else user
        }
        _registeredUsers.value = updatedList
    }

    fun loginAdmin(passcode: String): Boolean {
        if (passcode == "01010634040" || passcode == "1234" || passcode == "admin" || passcode.lowercase() == "abdulrahma5n@gmail.com") {
            _isAdminLoggedIn.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
    }

    fun registerAndLoginUser(
        name: String,
        phone: String,
        email: String = "",
        loginMethod: String,
        cityLocation: String = "أولاد صقر - المدينة"
    ) {
        val currentUsers = _registeredUsers.value.toMutableList()
        val existingIndex = currentUsers.indexOfFirst {
            (email.isNotBlank() && it.email.equals(email, ignoreCase = true)) ||
            (phone.isNotBlank() && it.phone == phone)
        }

        val userToLogin: User
        if (existingIndex >= 0) {
            val existing = currentUsers[existingIndex]
            userToLogin = existing.copy(
                name = name.ifBlank { existing.name },
                phone = phone.ifBlank { existing.phone },
                email = email.ifBlank { existing.email },
                loginMethod = loginMethod,
                cityLocation = cityLocation
            )
            currentUsers[existingIndex] = userToLogin
        } else {
            val newId = (currentUsers.maxOfOrNull { it.id } ?: 0L) + 1L
            userToLogin = User(
                id = newId,
                name = name.ifBlank { "مستخدم جديد" },
                phone = phone.ifBlank { "01000000000" },
                email = email,
                loginMethod = loginMethod,
                isBlocked = false,
                cityLocation = cityLocation,
                registeredAt = System.currentTimeMillis()
            )
            currentUsers.add(0, userToLogin)
        }

        _registeredUsers.value = currentUsers
        _currentUser.value = userToLogin
        _userPhone.value = userToLogin.phone
        _isGuestMode.value = false
    }

    fun enableGuestMode() {
        _isGuestMode.value = true
        _currentUser.value = null
        _userPhone.value = null
    }

    fun loginUser(phone: String) {
        registerAndLoginUser(
            name = "مستخدم ($phone)",
            phone = phone,
            email = "",
            loginMethod = "رقم الهاتف"
        )
    }

    fun logoutUser() {
        _isGuestMode.value = true
        _currentUser.value = null
        _userPhone.value = null
    }

    fun updateUserProfile(
        newName: String,
        newUsername: String,
        newPhone: String,
        newProfilePictureUri: String,
        onSuccess: (User) -> Unit
    ) {
        val user = _currentUser.value ?: return
        val currentList = _registeredUsers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == user.id || (user.phone.isNotBlank() && it.phone == user.phone) }

        val nameChangedNow = newName.isNotBlank() && newName != user.name && !user.isNameChanged
        val finalName = if (!user.isNameChanged && newName.isNotBlank()) newName else user.name
        val finalIsNameChanged = user.isNameChanged || nameChangedNow

        val formattedUsername = if (newUsername.isNotBlank()) {
            if (newUsername.startsWith("@")) newUsername else "@$newUsername"
        } else ""
        val usernameChangedNow = formattedUsername.isNotBlank() && formattedUsername != user.username && !user.isUsernameChanged
        val finalUsername = if (!user.isUsernameChanged && formattedUsername.isNotBlank()) formattedUsername else user.username
        val finalIsUsernameChanged = user.isUsernameChanged || usernameChangedNow

        val updatedUser = user.copy(
            name = finalName,
            username = finalUsername,
            phone = newPhone.ifBlank { user.phone },
            profilePictureUri = newProfilePictureUri.ifBlank { user.profilePictureUri },
            isNameChanged = finalIsNameChanged,
            isUsernameChanged = finalIsUsernameChanged
        )

        if (index >= 0) {
            currentList[index] = updatedUser
        } else {
            currentList.add(0, updatedUser)
        }

        _registeredUsers.value = currentList
        _currentUser.value = updatedUser
        _userPhone.value = updatedUser.phone

        onSuccess(updatedUser)
    }

    fun sendOfficeChatMessage(userText: String) {
        if (userText.isBlank()) return
        val userName = _currentUser.value?.name ?: "مستخدم"
        val currentList = _officeChatMessages.value.toMutableList()
        currentList.add(
            ChatMessage(
                senderName = userName,
                text = userText,
                isUser = true,
                timestampStr = "الآن"
            )
        )
        _officeChatMessages.value = currentList
        _isAiThinking.value = true

        viewModelScope.launch {
            val approvedCount = approvedProperties.value.size
            val contextInfo = "يوجد حالياً $approvedCount عقارات معروضة في مكتب أولاد صقر. مدير المكتب أ/ عبدالرحمن."
            val reply = GeminiClient.askAssistant(userText, contextInfo)
            
            val updatedList = _officeChatMessages.value.toMutableList()
            val managerSender = "أ/ عبدالرحمن (مدير المكتب)"
            updatedList.add(
                ChatMessage(
                    senderName = managerSender,
                    text = reply,
                    isUser = false,
                    isManager = true,
                    timestampStr = "الآن"
                )
            )
            _officeChatMessages.value = updatedList
            _isAiThinking.value = false

            // Trigger instant Private Office Chat Push Notification
            com.example.data.utils.NotificationHelper.sendOfficeChatNotification(
                getApplication(),
                senderName = managerSender,
                messageText = reply
            )
        }
    }

    fun sendGroupChatMessage(
        text: String,
        mediaUrl: String? = null,
        mediaType: String? = null,
        isAnnouncement: Boolean = false
    ) {
        if (text.isBlank() && mediaUrl == null) return

        val user = _currentUser.value
        val isAdmin = _isAdminLoggedIn.value || user?.phone == "admin" || user?.phone == "01010634040" || (user?.name?.contains("عبدالرحمن") == true)
        val sender = if (isAdmin) "أ/ عبدالرحمن (إدارة المنصة)" else (user?.name ?: "عضو المنصة")

        val newMsg = ChatMessage(
            senderName = sender,
            text = text,
            isUser = !isAdmin,
            isManager = isAdmin,
            isAdmin = isAdmin,
            timestampStr = "الآن",
            mediaUrl = mediaUrl,
            mediaType = mediaType,
            isAnnouncement = isAnnouncement
        )

        val currentList = _groupChatMessages.value.toMutableList()
        currentList.add(newMsg)
        _groupChatMessages.value = currentList

        // Trigger instant Community Group Chat Push Notification (Broadcast for announcements and messages)
        val previewText = if (text.isNotBlank()) text else if (mediaType == "VIDEO") "📹 فيديو جديد مضاف للمجموعة" else "📷 صورة جديدة مضافة للمجموعة"
        com.example.data.utils.NotificationHelper.sendGroupChatNotification(
            getApplication(),
            senderName = sender,
            messageText = previewText,
            isAnnouncement = isAnnouncement
        )
    }

    fun sendAiMessage(userText: String) {
        sendOfficeChatMessage(userText)
    }
}

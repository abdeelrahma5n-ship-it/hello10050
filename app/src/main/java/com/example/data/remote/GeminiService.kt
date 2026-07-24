package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiPart(
    val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    val content: GeminiContent?
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<GeminiCandidate>?
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    suspend fun askAssistant(userQuery: String, contextInfo: String = ""): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "عذراً، يجب ضبط مفتاح GEMINI_API_KEY في إعدادات التطبيق لخدمة المساعد الذكي."
        }

        val systemPrompt = """
            أنت "مساعد سمسارك العقاري ✨" الذكي الخبير والمستشار العقاري الموثوق لتطبيق ومكتب 'سمسارك في أولاد صقر' بمحافظة الشرقية، مصر.
            مدير المكتب المسئول: أ/ عبدالرحمن.
            رقم الهاتف والواتساب الرسمي: 01010634040.
            رابط الواتساب المباشر: https://wa.me/201010634040
            الصفحة الرسمية على الفيسبوك: https://www.facebook.com/semsark2

            تخصصك ودورك:
            1. تقديم استشارات وتحليلات دقيقة عن عقارات أولاد صقر بمركز ومدينة أولاد صقر والقرى التابعة لها (تلراك، الصوفية، بني حسن، قصاصين الأزهار، زور أبو الليل، كفر الفرايحة، الشفيان، جزيرة الشافعي، وغيرها).
            2. توضيح الفروق والأسعار التقريبية بين:
               - أراضي كردون مباني (أعلى سعراً، مرخصة وجاهزة للبناء المباشر).
               - أراضي خارج الكردون / زراعية (أسعار أنسب للمستقبل أو الاستثمار الزراعي/الداجني).
               - المنازل والبيوت المستقلة وشقق التمليك والإيجار.
            3. حساب المساحات وتوضيح التحويلات بين المتر المربع (م²)، القيراط (175 م²)، والفدان (24 قيراط / 4200 م²).
            4. توجيه المستخدم بأسلوب ودود ومحترف للتواصل المباشر مع أ/ عبدالرحمن (01010634040) أو زيارة صفحتنا الرسمية على الفيسبوك (https://www.facebook.com/semsark2) للحصول على المعاينة الميدانية وإتمام الاتفاق والتفاوض.
            
            معلومات العقارات المتوفرة حالياً في التطبيق:
            $contextInfo

            أجب دائماً باللغة العربية بأسلوب راقٍ، مهني، ومبسط.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                GeminiContent(
                    parts = listOf(GeminiPart(text = userQuery)),
                    role = "user"
                )
            ),
            systemInstruction = GeminiContent(
                parts = listOf(GeminiPart(text = systemPrompt))
            )
        )

        return try {
            val response = api.generateContent(apiKey, request)
            val replyText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

            replyText ?: "أهلاً بك! لم أتمكن من الحصول على إجابة حالياً. يمكنك التواصل مباشرة مع أ/ عبدالرحمن عبر الواتساب: 01010634040"
        } catch (e: Exception) {
            "حدث خطأ في الاتصال بالمساعد الذكي: ${e.localizedMessage ?: "تأكد من الاتصال بالإنترنت"}. للتواصل المباشر مع أ/ عبدالرحمن اتصل بنا على: 01010634040"
        }
    }
}

package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties ORDER BY createdAt DESC")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE status != 'قيد المراجعة' AND status != 'مرفوض' AND status != 'مؤرشف' ORDER BY createdAt DESC")
    fun getApprovedProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE (status != 'قيد المراجعة' AND status != 'مرفوض' AND status != 'مؤرشف') AND categoryId = :categoryId ORDER BY createdAt DESC")
    fun getPropertiesByCategory(categoryId: String): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE status = 'قيد المراجعة' ORDER BY createdAt DESC")
    fun getPendingProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE isFavorite = 1 AND status != 'قيد المراجعة' AND status != 'مرفوض' AND status != 'مؤرشف' ORDER BY createdAt DESC")
    fun getFavoriteProperties(): Flow<List<Property>>

    @Query("SELECT * FROM properties WHERE id = :id")
    suspend fun getPropertyById(id: Long): Property?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property): Long

    @Update
    suspend fun updateProperty(property: Property)

    @Delete
    suspend fun deleteProperty(property: Property)

    @Query("UPDATE properties SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE properties SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("SELECT COUNT(*) FROM properties")
    suspend fun getCount(): Int
}

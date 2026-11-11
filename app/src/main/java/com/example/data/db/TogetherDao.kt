package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CallEntity
import com.example.data.model.MessageEntity
import com.example.data.model.MomentEntity
import com.example.data.model.SourceType
import kotlinx.coroutines.flow.Flow

@Dao
interface TogetherDao {
    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE sourceType = :sourceType ORDER BY timestamp DESC")
    fun getMessagesBySource(sourceType: SourceType): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: Long)

    @Query("DELETE FROM messages WHERE sourceType = :sourceType")
    suspend fun clearMessagesBySource(sourceType: SourceType)

    @Query("DELETE FROM messages")
    suspend fun clearAllMessages()

    // Calls
    @Query("SELECT * FROM calls ORDER BY timestamp DESC")
    fun getAllCalls(): Flow<List<CallEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCall(call: CallEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalls(calls: List<CallEntity>)

    @Query("DELETE FROM calls WHERE id = :id")
    suspend fun deleteCall(id: Long)

    @Query("DELETE FROM calls")
    suspend fun clearAllCalls()

    // Moments
    @Query("SELECT * FROM moments ORDER BY timestamp DESC")
    fun getAllMoments(): Flow<List<MomentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoment(moment: MomentEntity): Long

    @Query("DELETE FROM moments WHERE id = :id")
    suspend fun deleteMoment(id: Long)

    @Query("DELETE FROM moments")
    suspend fun clearAllMoments()
}

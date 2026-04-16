package com.itlab.data.cloud

import android.content.Context
import timber.log.Timber
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.itlab.domain.cloud.SyncManager
import com.itlab.data.cloud.AuthManager

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val syncManager: SyncManager,
    private val authManager: AuthManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val userId = authManager.getCurrentUserId() ?: run {
            Timber.e("Sync failed: User is not authorized")
            return Result.failure()
        }
        return try {
            Timber.d("Starting sync for user: $userId")
            syncManager.sync(userId)

            Timber.d("Sync completed successfully")
            Result.success()
        }catch (e: Exception){
            Timber.e(e, "Sync error occurred: %s", e.message)

            if (e is java.io.IOException) Result.retry() else Result.failure()
        }
    }
}

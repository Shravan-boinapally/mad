package com.example.waterminder.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        NotificationHelper.showNotification(
            applicationContext,
            "💧 Water Reminder",
            "Take a sip and stay hydrated!"
        )

        val nextWork =
            OneTimeWorkRequestBuilder<WaterReminderWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(nextWork)

        return Result.success()
    }
}

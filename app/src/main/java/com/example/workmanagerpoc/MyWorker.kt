package com.example.workmanagerpoc

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
//        receiving the input array
        val arrayOfNo = inputData.getIntArray("array_of_no")
//       Doing Background task
        var sum = executingBGWork(arrayOfNo)
//Prepare output after BG task
        val outputData = workDataOf("sum" to sum)
//        Set result
        return Result.success(outputData)
    }

    private fun executingBGWork(arrayOfNo: IntArray?): Int {
        var sum = 0
        if (arrayOfNo != null) {
            for (i in arrayOfNo) {
                sum += i
            }
        }
        return sum
    }

}
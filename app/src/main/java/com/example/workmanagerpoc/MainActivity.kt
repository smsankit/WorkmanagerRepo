package com.example.workmanagerpoc

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var btnStartWork: AppCompatButton
    private lateinit var btnStartPeriodicWork: AppCompatButton
    private lateinit var btnCancelPeriodicWork: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartWork = findViewById(R.id.btnStartWork)
        btnStartPeriodicWork = findViewById(R.id.btnStartPeriodicWork)
        btnCancelPeriodicWork = findViewById(R.id.btnCancelPeriodicWork)
        btnStartWork.setOnClickListener {
            doBGWork()
        }
        btnStartPeriodicWork.setOnClickListener {
            doPeriodicBGWork()
        }
    }

    private fun doBGWork() {
//        using workDataOf() method from KTX to pass input data for workmanager
        val inputWorkData = workDataOf("array_of_no" to arrayOf(1, 2, 3, 4, 5, 6))

//        Build one time worker with input data
        val worker = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(inputWorkData)
            .build()
//        Enqueue the worker to execute
        WorkManager.getInstance(this).enqueue(worker);
//observing task state and and result
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(worker.id).observe(this, Observer {
            if (it.state.name == "SUCCEEDED") {
                Toast.makeText(
                    this@MainActivity,
                    "${it.state.name} sum = ${it.outputData.getInt("sum", 0)} ",
                    Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(this@MainActivity, it.state.name, Toast.LENGTH_SHORT).show();
            }
        })
    }

    private fun doPeriodicBGWork() {
        val inputWorkData = workDataOf("array_of_no" to arrayOf(1, 2, 3, 4, 5, 6))

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()

        /*Thiw worker will execute once all constraint met*/
        val worker = PeriodicWorkRequestBuilder<MyWorker>(40, TimeUnit.HOURS)
            .setInputData(inputWorkData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(worker);

        btnCancelPeriodicWork.setOnClickListener {
            WorkManager.getInstance(this).cancelWorkById(worker.id)
        }
    }
}
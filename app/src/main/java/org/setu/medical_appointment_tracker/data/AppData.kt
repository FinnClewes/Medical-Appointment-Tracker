package org.setu.medical_appointment_tracker.data

import android.content.Context
import java.io.File

object AppData {

    lateinit var placedMarks: AppointmentMemStore
        private set

    lateinit var placedMeds: MedicationMemStore
        private set

    fun initialize(context: Context) {
        val apptFile = File(
            context.filesDir,
            "appt_list.json"
        )
        val medsFile = File(
            context.filesDir,
            "meds_list.json"
        )

        placedMarks = AppointmentMemStore(apptFile)
        placedMeds = MedicationMemStore(medsFile)
    }
}
package org.setu.medical_appointment_tracker.adapters

import org.setu.medical_appointment_tracker.models.MedicationModel

interface OnMedsListener {
    fun onApptClick(mark: MedicationModel)

    fun onApptLongClick(mark: MedicationModel): Boolean
}
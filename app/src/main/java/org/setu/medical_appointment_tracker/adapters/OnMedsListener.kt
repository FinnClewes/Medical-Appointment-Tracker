package org.setu.medical_appointment_tracker.adapters

import org.setu.medical_appointment_tracker.models.MedicationModel

interface OnMedsListener {
    fun onMedsClick(mark: MedicationModel)

    fun onMedsLongClick(mark: MedicationModel): Boolean
}
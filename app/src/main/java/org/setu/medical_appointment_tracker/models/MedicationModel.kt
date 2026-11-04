package org.setu.medical_appointment_tracker.models

data class MedicationModel(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var doctor: String = "",
    var amount: Float = 0F
)
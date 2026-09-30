package org.setu.medical_appointment_tracker

/**
 * Data class representing a single Placemark item.
 * Kotlin automatically generates toString(), equals(), hashCode(), and copy().
 */
data class AppointmentModel(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var doctor: String = "",
    var medication: Array<String> = emptyArray(),
    var x: Float = 0F,
    var y: Float = 0F
)
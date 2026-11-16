package org.setu.medical_appointment_tracker.models

/**
 * Data class representing a single Placemark item.
 * Kotlin automatically generates toString(), equals(), hashCode(), and copy().
 */
data class AppointmentModel(
    var id: Long = 0L,
    var title: String = "",
    var description: String = "",
    var doctor: String = "",
    var date: String = "",
    var x: Float = 0F,
    var y: Float = 0F,
    var imageUri: String? = null
) {
    fun validate() {
        require(title.isNotBlank()) { "Name must not be empty." }
        require(x.isFinite() && x in -90f..90f) { "Latitude must be between -90 and 90." }
        require(y.isFinite() && y in -180f..180f) { "Longitude must be between -180 and 180." }
    }
}
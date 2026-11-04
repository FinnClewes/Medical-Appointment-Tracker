package org.setu.medical_appointment_tracker.data

import org.setu.medical_appointment_tracker.models.AppointmentModel
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

class AppointmentMemStore {
    private val appointments = ArrayList<AppointmentModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<AppointmentModel> {
        return appointments
    }

    fun create(appointment: AppointmentModel) {
        appointment.id = lastId.incrementAndGet()
        appointments.add(appointment)
        Timber.i("Created appointment: id=%d, title=%s", appointment.id, appointment.title)
    }

    fun update(appointment: AppointmentModel): Boolean {
        val foundAppointment = findOne(appointment.id)
        return if (foundAppointment != null) {
            foundAppointment.title = appointment.title
            foundAppointment.description = appointment.description
            foundAppointment.doctor = appointment.doctor
            foundAppointment.date = appointment.date
            foundAppointment.x = appointment.x
            foundAppointment.y = appointment.y
            Timber.i("Updated appointment: id=%d, title=%s", appointment.id, appointment.title)
            true
        } else {
            Timber.i("Update failed: no appointment with id=%d", appointment.id)
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundAppointment = findOne(id)
        return if (foundAppointment != null) {
            appointments.remove(foundAppointment)
            Timber.i("Deleted appointment: id=%d", id)
            true
        } else {
            Timber.w("Delete failed: no appointment with id=%d", id)
            false
        }
    }

    fun findOne(id: Long): AppointmentModel? {
        return appointments.find { p -> p.id == id }
    }
}
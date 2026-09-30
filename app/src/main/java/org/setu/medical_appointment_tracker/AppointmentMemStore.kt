package org.setu.medical_appointment_tracker

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
    }

    fun update(appointment: AppointmentModel): Boolean {
        val foundAppointment = findOne(appointment.id)
        return if (foundAppointment != null) {
            foundAppointment.title = appointment.title
            foundAppointment.description = appointment.description
            foundAppointment.doctor = appointment.doctor
            foundAppointment.medication = appointment.medication
            foundAppointment.x = appointment.x
            foundAppointment.y = appointment.y
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundAppointment = findOne(id)
        return if (foundAppointment != null) {
            appointments.remove(foundAppointment)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): AppointmentModel? {
        return appointments.find { p -> p.id == id }
    }
}

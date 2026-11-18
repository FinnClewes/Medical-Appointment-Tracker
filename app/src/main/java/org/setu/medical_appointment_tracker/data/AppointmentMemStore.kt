package org.setu.medical_appointment_tracker.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.setu.medical_appointment_tracker.models.AppointmentModel
import timber.log.Timber
import java.io.File
import java.util.concurrent.atomic.AtomicLong

class AppointmentMemStore(private val apptFile: File) {
    private val appointments = ArrayList<AppointmentModel>()
    private val lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    private fun load() {
        if (!apptFile.exists()) return

        val type = object : TypeToken<ArrayList<AppointmentModel>>() {}.type

        val loaded: ArrayList<AppointmentModel> =
            gson.fromJson(apptFile.readText(), type) ?: arrayListOf()

        appointments.clear()
        appointments.addAll(loaded)

        lastId.set(appointments.maxOfOrNull { it.id } ?: 0L)
    }

    private fun save() {
        apptFile.parentFile?.mkdirs()
        apptFile.writeText(gson.toJson(appointments))
    }

    fun findAll(): List<AppointmentModel> {
        return appointments
    }

    fun create(appointment: AppointmentModel) {
        appointment.validate()
        appointment.id = lastId.incrementAndGet()
        appointments.add(appointment)
        Timber.i("Created appointment: id=%d, title=%s", appointment.id, appointment.title)
        save()
    }

    fun update(appointment: AppointmentModel): Boolean {
        appointment.validate()
        val foundAppointment = findOne(appointment.id)
        return if (foundAppointment != null) {
            foundAppointment.title = appointment.title
            foundAppointment.description = appointment.description
            foundAppointment.doctor = appointment.doctor
            foundAppointment.x = appointment.x
            foundAppointment.y = appointment.y
            foundAppointment.imageUri = appointment.imageUri
            save()
            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundAppointment = findOne(id)
        return if (foundAppointment != null) {
            appointments.remove(foundAppointment)
            Timber.i("Deleted appointment: id=%d", id)
            save()
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
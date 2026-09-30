package org.setu.medical_appointment_tracker

import org.setu.medical_appointment_tracker.AppointmentMemStore
import org.setu.medical_appointment_tracker.AppointmentModel

val store = AppointmentMemStore()

fun main() {
    println("=== Appointment Console App (Lab 1) ===")
    var input: Int
    do {
        input = menu()
        when (input) {
            1 -> addAppointment()
            2 -> listAppointments()
            3 -> updateAppointment()
            4 -> deleteAppointment()
            5 -> searchAppointment()
            0 -> println("\nExiting Appointment application. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Appointment")
    println(" 2. List All Appointments")
    println(" 3. Update a Appointment")
    println(" 4. Delete a Appointment")
    println(" 5. Search Appointment by ID")
    println(" 0. Exit")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun addAppointment() {
    println("\n--- Add Appointment ---")
    print("Enter Title: ")
    val title = readlnOrNull()?.trim().orEmpty()
    print("Enter Description: ")
    val description = readlnOrNull()?.trim().orEmpty()
    print("Enter Doctor: ")
    val doctor = readlnOrNull()?.trim().orEmpty()

    if (title.isNotEmpty()) {
        val appointment = AppointmentModel(title = title, description = description, doctor = doctor)
        store.create(appointment)
        println("Appointment added successfully with ID: ${appointment.id}")
    } else {
        println("Title cannot be empty. Creation cancelled.")
    }
}

fun listAppointments() {
    println("\n--- All Appointments ---")
    val appointments = store.findAll()
    if (appointments.isEmpty()) {
        println("No appointments stored yet.")
    } else {
        appointments.forEach { println("ID: ${it.id} | Title: ${it.title} | Description: ${it.description}") }
    }
}

fun updateAppointment() {
    println("\n--- Update Appointment ---")
    listAppointments()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Appointment to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter New Title: ")
        val title = readlnOrNull()?.trim().orEmpty()
        print("Enter New Description: ")
        val description = readlnOrNull()?.trim().orEmpty()
        print("Enter New Doctor: ")
        val doctor = readlnOrNull()?.trim().orEmpty()

        if (title.isNotEmpty()) {
            val updated = store.update(AppointmentModel(id = id, title = title, description = description, doctor = doctor))
            if (updated) println("Appointment updated successfully.")
        } else {
            println("Title cannot be empty. Update cancelled.")
        }
    } else {
        println("Appointment with ID $id not found.")
    }
}

fun deleteAppointment() {
    println("\n--- Delete Appointment ---")
    listAppointments()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Appointment to delete: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val deleted = store.delete(id)
        if (deleted) {
            println("Appointment with ID $id deleted successfully.")
        } else {
            println("Appointment with ID $id not found.")
        }
    } else {
        println("Invalid ID entered.")
    }
}

fun searchAppointment() {
    println("\n--- Search Appointment ---")
    print("Enter ID: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val appointment = store.findOne(id)
        if (appointment != null) {
            println("Found: ID: ${appointment.id} | Title: ${appointment.title} | Description: ${appointment.description} | Doctor: ${appointment.doctor}")
        } else {
            println("No appointment found with ID $id.")
        }
    } else {
        println("Invalid ID entered.")
    }
}
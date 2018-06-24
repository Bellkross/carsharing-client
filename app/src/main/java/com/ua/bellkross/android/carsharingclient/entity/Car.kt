package com.bellkross.carsharingserver.entity

import java.util.*

class Car(
        val number: String = "",
        val fuelCardNumber: String = "",
        val address: String = "",
        val color: String? = "",
        val status: Boolean = true,
        val creatingDate: Date = Date(),
        val brand: String = "",
        val price: Double = 0.0
)
package com.parking.parking_lot.operationHours

import com.parking.parking_lot.parking.Parking
import jakarta.persistence.*

@Entity

data class OperatingHours (


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Enumerated(EnumType.STRING)
        val dateType: DateType,

        val openTime: String,

        val closeTIme: String,


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="parking_id")
        val parking : Parking






)
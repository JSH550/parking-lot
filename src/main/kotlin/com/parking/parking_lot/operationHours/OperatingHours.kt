package com.parking.parking_lot.operationHours

import com.parking.parking_lot.parking.Parking
import jakarta.persistence.*

@Entity

data class OperatingHours (


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Enumerated(EnumType.STRING)
        val dateType: DateType? = DateType.WEEKDAY,//평일,토요일,공휴일

        val openTime: String,//오픈시간

        val closeTime: String,//마감시간




        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="parking_id")
        val parking : Parking






)
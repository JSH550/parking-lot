package com.parking.parking_lot.fee

import com.parking.parking_lot.parking.Parking
import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Fee(


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        val id: Long? = null,

        @Enumerated
        val type: FeeType,

        val baseRate : Int?=0,//기본요금
        val baseTime : Int?=0,//기본요금 적용시간
        val additionalRate : Int? =0,//추가요금(단위별)
        val additionalTime : Int? =0,//시간단위


)
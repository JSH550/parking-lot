package com.parking.parking_lot.parking

import com.parking.parking_lot.fee.Fee
import com.parking.parking_lot.operationHours.OperatingHours
import jakarta.persistence.*

@Entity
@Table(name = "parking")
data class Parking(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(nullable = false)
        val name: String,//이름

        val address: String? = "", //주소

        val latitude: Double,//위도

        val longitude: Double,//경도

        val totalSpots: Int,


        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="fee_id")
        val fee: Fee? = null, //요금정보

        @OneToMany(mappedBy = "parking", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        val operatingHours: List<OperatingHours> = listOf()
)
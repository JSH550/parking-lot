package com.parking.parking_lot.parking

import com.parking.parking_lot.operationHours.OperatingHours
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "parking")
data class Parking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,  // Primary Constructor에서 id를 nullable로 유지

    @Column(nullable = false)
    val name: String,//주차장 이름

    val address: String? = "", //도로명 주소

    val latitude: Double,//위도

    val longitude: Double,//경도


    val totalSpots: Int,//주차장 최대수용


    val feePerHour: Int,//시간당요금

    @Enumerated(EnumType.STRING)
    val ownership: Ownership?= Ownership.PUBLIC,//공영/민영


    @OneToMany(mappedBy = "parking", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @BatchSize(size = 1000)
    val operatingHours: List<OperatingHours> = listOf()


)
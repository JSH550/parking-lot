package com.parking.parking_lot.parking

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

//Elastic-Search 전용

@Document(indexName = "parking")
data class ParkingDocument (


    @Id
    val id: Long,

    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,


)
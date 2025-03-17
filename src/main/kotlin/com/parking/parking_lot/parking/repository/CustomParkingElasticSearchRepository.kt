package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.parking.ParkingDocument
import org.springframework.data.domain.Pageable

interface CustomParkingElasticSearchRepository {
    fun searchByNameOrAddress(searchWord: String, pageable: Pageable): List<ParkingDocument>

}
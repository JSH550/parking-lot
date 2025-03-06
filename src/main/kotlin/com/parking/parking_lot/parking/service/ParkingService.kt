package com.parking.parking_lot.parking.service

import com.parking.parking_lot.parking.Parking

interface ParkingService {

    fun save(name:String):List<Parking>
}
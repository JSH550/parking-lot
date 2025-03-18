package com.parking.parking_lot.operationHours.service

import com.parking.parking_lot.operationHours.DateType
import com.parking.parking_lot.operationHours.OperatingHours
import com.parking.parking_lot.operationHours.OperatingHoursRepository
import com.parking.parking_lot.parking.Parking
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class OperatingHoursServiceImplTest() {

    private val operatingHoursRepository: OperatingHoursRepository = mockk()
    private val operatingHoursService = OperatingHoursServiceImpl(operatingHoursRepository)

    @Test
    fun `getByParkingIds should return list of OperatingHoursDto`() {
        // Given
        val mockParking1: Parking = Parking(
            id = 1L,
            name = "Nearby Parking",
            latitude = 32.22,
            longitude = 127.33,
            address = "주소",
            feePerHour = 100,
            operatingHours = emptyList(),
            totalSpots = 10
        )

        val mockParking2: Parking = Parking(
            id = 2L,
            name = "Nearby Parking",
            latitude = 32.22,
            longitude = 127.33,
            address = "주소",
            feePerHour = 100,
            operatingHours = emptyList(),
            totalSpots = 10
        )
        val parkingIds = listOf(1L, 2L)
        val mockOperatingHours = listOf(
            OperatingHours(
                id = 1L,
                parking = mockParking1,
                dateType = DateType.WEEKDAY,
                openTime = "08:00",
                closeTime = "22:00"
            ),
            OperatingHours(
                id = 2L,
                parking = mockParking2,
                dateType = DateType.WEEKDAY,
                openTime = "08:00",
                closeTime = "22:00"
            )
        )

        every { operatingHoursRepository.findByParkingIds(parkingIds) } returns mockOperatingHours

        // When
        val result = operatingHoursService.getByParkingIds(parkingIds)

        // Then
        assertEquals(2, result.size)
        assertEquals("WEEKDAY", result[0].dataType)
        assertEquals("WEEKDAY", result[1].dataType)
    }

    @Test
    fun `getByParkingId should return list of OperatingHoursDto`() {

        // Given
        val mockParking1: Parking = Parking(
            id = 1L,
            name = "Nearby Parking",
            latitude = 32.22,
            longitude = 127.33,
            address = "주소",
            feePerHour = 100,
            operatingHours = emptyList(),
            totalSpots = 10
        )


        val parkingIds = listOf(1L, 2L)
        val mockOperatingHours = listOf(
            OperatingHours(
                id = 1L,
                parking = mockParking1,
                dateType = DateType.WEEKDAY,
                openTime = "08:00",
                closeTime = "22:00"
            ),
            OperatingHours(
                id = 2L,
                parking = mockParking1,
                dateType = DateType.SATURDAY,
                openTime = "08:00",
                closeTime = "22:00"
            )
        )

        every { operatingHoursRepository.findByParkingIds(parkingIds) } returns mockOperatingHours

        // When
        val result = operatingHoursService.getByParkingIds(parkingIds)

        // Then
        assertEquals(2, result.size)
        assertEquals("WEEKDAY", result[0].dataType)
        assertEquals("SATURDAY", result[1].dataType)


    }


}
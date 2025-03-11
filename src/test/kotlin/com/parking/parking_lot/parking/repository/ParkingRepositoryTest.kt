package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.parking.Parking
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)

class ParkingRepositoryTest {

    private val parkingRepository = mockk<ParkingRepository>()

    @Test
    fun `findByIdIn should return list of parking`() {
        // Given: Mock 데이터 준비
        val ids = listOf(1L, 2L)
        val parkingList = listOf(
            Parking(id = 1L, name = "Parking A", address = "123 Main St", latitude = 37.7749, longitude = -122.4194, totalSpots = 10),
            Parking(id = 2L, name = "Parking B", address = "456 Side St", latitude = 37.7750, longitude = -122.4195, totalSpots = 10)
        )

        // Mock 설정: 특정 ID 리스트가 주어졌을 때, 예상 결과 반환
        every { parkingRepository.findByIdIn(ids) } returns parkingList

        // When: findByIdIn 호출
        val result = parkingRepository.findByIdIn(ids)

        // Then: 결과 검증
        assertEquals(2, result.size)
        assertEquals("Parking A", result[0].name)
        assertEquals("Parking B", result[1].name)
    }
}
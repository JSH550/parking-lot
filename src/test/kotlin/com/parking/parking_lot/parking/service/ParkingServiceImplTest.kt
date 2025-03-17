package com.parking.parking_lot.parking.service

import com.parking.parking_lot.parking.ParkingDocument
import com.parking.parking_lot.parking.dto.ParkingSearchDto
import com.parking.parking_lot.parking.repository.ParkingElasticSearchRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.geo.GeoPoint

@ExtendWith(MockitoExtension::class)

class ParkingServiceImplTest {

    private val parkingElasticSearchRepository: ParkingElasticSearchRepository = mockk()
    private lateinit var parkingService: ParkingServiceImpl

    @BeforeEach
    fun setUp() {
        parkingService = ParkingServiceImpl(parkingElasticSearchRepository)
    }

    @Test
    fun `searchBySearchWord should return list of ParkingSearchDto`() {
        // Given (테스트 준비)
        val searchWord = "Parking"
        val parkingId = 1L
        val location = GeoPoint(37.442288, 127.126662)
        val pageRequest:Pageable= PageRequest.of(0,10) //페이지네이션 정보
        //Elasticsearch Document 객체 생성
        val parkingDocument =
            ParkingDocument(
                id = parkingId,
                name = "Nearby Parking",
                location = location,
                address = "주소",
                feePerHour = 100,
                operatingHours = emptyList(),
            )

        val expectedDto = ParkingSearchDto.fromParkingDocument(parkingDocument)//예상 결과 DTO 생성, 검색결과와 동일해야함

        // ✅ Mock 설정

        every { parkingElasticSearchRepository.searchByNameOrAddress(searchWord,pageRequest) } returns listOf(parkingDocument)
        // When (메서드 실행)
        val result = parkingService.searchBySearchWord(searchWord)
        // Then (검증)

        assertIterableEquals(listOf(expectedDto), result)
    }

    @Test
    fun `searchByLocation should return list of ParkingSearchDto`() {
        // Given

        val distance = "5km"
        val latitude = 37.5665
        val longitude = 126.9780
        val geoPoint = GeoPoint(latitude, longitude)
        val parkingId = 1L
        val location = GeoPoint(latitude, longitude)

        //Elasticsearch Document 객체 생성
        val parkingDocument =
            ParkingDocument(
                id = parkingId,
                name = "Nearby Parking",
                location = location,
                address = "주소",
                feePerHour = 100,
                operatingHours = emptyList(),
            )
        val expectedDto = ParkingSearchDto.fromParkingDocument(parkingDocument)

        // ✅ Mock 설정
        every { parkingElasticSearchRepository.findByLocationNear(geoPoint, distance) } returns listOf(parkingDocument)


        // When
        val result = parkingService.searchByLocation(distance, latitude, longitude)

        // Then
        assertIterableEquals(listOf(expectedDto), result)
    }


}
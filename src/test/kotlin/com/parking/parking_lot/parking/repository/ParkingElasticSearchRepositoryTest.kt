package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.ParkingLotApplication
import com.parking.parking_lot.parking.ParkingDocument
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.core.geo.GeoPoint


@SpringBootTest(classes = [ParkingLotApplication::class]) // HTTPS 설정 포함된 Spring Boot Test 실행

class ParkingElasticSearchRepositoryTest {

    @Autowired
    lateinit var parkingElasticSearchRepository: ParkingElasticSearchRepository

    @BeforeEach
    fun setup() {
        //테스트 Document 생성
        val parking1 = ParkingDocument(id = 1, name = "Test Parking Lot", address = "주소",location = GeoPoint(37.5665, 126.9780))
        val parking2 = ParkingDocument(id = 2, name = "Nearby Parking",address = "주소", location = GeoPoint(37.5675, 126.9790))

        //테스트 Document 저장
        parkingElasticSearchRepository.saveAll(listOf(parking1, parking2))
    }

    @Test
    fun `should return results when searching by name`() {
        val result = parkingElasticSearchRepository.findByNameContaining("Test")

        assertEquals(1, result.size)
        assertEquals("Test Parking Lot", result.first().name)//이름이 같은지 검증
    }

    @Test
    fun `should return results when searching by location`() {
        val location = GeoPoint(37.5665, 126.9780)//테스트 좌표객체
        val result = parkingElasticSearchRepository.findByLocationNear(location, "5km")//5km 이내 탐색

        assertEquals(2, result.size) // 5km 내에 두 개의 주차장이 있다고 가정
    }
}
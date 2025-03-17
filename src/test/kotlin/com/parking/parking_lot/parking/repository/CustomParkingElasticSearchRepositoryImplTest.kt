package com.parking.parking_lot.parking.repository
import com.parking.parking_lot.ParkingLotApplication
import com.parking.parking_lot.parking.ParkingDocument
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.transaction.annotation.Transactional


@SpringBootTest(classes = [ParkingLotApplication::class]) // HTTPS 설정 포함된 Spring Boot Test 실행
@Transactional // 테스트 후 데이터 롤백
class CustomParkingElasticSearchRepositoryImplTest{
    @Autowired
    lateinit var parkingElasticSearchRepository: ParkingElasticSearchRepository

    @Autowired
    lateinit var customParkingElasticSearchRepositoryImpl: CustomParkingElasticSearchRepositoryImpl


    @BeforeEach
    fun setup() {
        //테스트 Document 생성
        val parking1 = ParkingDocument(
            id = 1,
            name = "Test Parking Lot",
            address = "First address",
            location = GeoPoint(37.5665, 126.9780),
            feePerHour = 1000,
            operatingHours = emptyList()
        )
        val parking2 = ParkingDocument(id = 2,
            name = "Nearby Parking",
            address = "Second address",
            location = GeoPoint(37.5675, 126.9790),
            feePerHour = 1000,
            operatingHours = emptyList()
        )

        //테스트 Document 저장
        parkingElasticSearchRepository.saveAll(listOf(parking1, parking2))
    }

    @Test
    fun `should return result when seraching by name`(){

        val searchWord: String = "Test Parking Lot"

        val pageRequest:Pageable = PageRequest.of(0,10)
        val results = customParkingElasticSearchRepositoryImpl.searchByNameOrAddress(searchWord, pageRequest)



        Assertions.assertEquals("Test Parking Lot", results.first().name) // 이름 검증


    }

    @Test
    fun `should return result when searching by address`(){

        val searchWord: String = "Second address"

        val pageRequest:Pageable = PageRequest.of(0,10)
        val results = customParkingElasticSearchRepositoryImpl.searchByNameOrAddress(searchWord, pageRequest)



        Assertions.assertEquals("Second address", results.first().address) // 주소 검증


    }



}
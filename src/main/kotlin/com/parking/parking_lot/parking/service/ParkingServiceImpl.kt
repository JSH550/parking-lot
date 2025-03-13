package com.parking.parking_lot.parking.service

import com.parking.parking_lot.parking.dto.ParkingSearchDto
import com.parking.parking_lot.parking.repository.ParkingElasticSearchRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ParkingServiceImpl(
    val parkingElasticSearchRepository: ParkingElasticSearchRepository,
) : ParkingService {

    private val logger: Logger = LoggerFactory.getLogger(ParkingServiceImpl::class.java)



    @Transactional(readOnly = true)
    override fun searchBySearchWord(searchWord: String): List<ParkingSearchDto> {

        //Elasticsearch 에서 검색어로 검색, DTO로 변환하여 반환
        return parkingElasticSearchRepository
            .findByNameContaining(searchWord)
            .map { ParkingSearchDto.fromParkingDocument(it) }//DTO 변환


    }

    @Transactional(readOnly = true)
    override fun searchByLocation(distance: String, latitude: Double, longitude: Double): List<ParkingSearchDto> {

        val geoPoint = GeoPoint(latitude, longitude)//파라미터로 GeoPoint 객체 생성

        //Elasticsearch 에서  geoPoint 검색, DTO로 변환하여 반환
        return parkingElasticSearchRepository.findByLocationNear(geoPoint, distance)
            .map { ParkingSearchDto.fromParkingDocument(it) }


    }

}
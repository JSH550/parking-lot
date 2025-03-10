package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.parking.ParkingDocument
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository


interface ParkingElasticSearchRepository : ElasticsearchRepository<ParkingDocument,String>{


    /**
     * 주차장 이름에 특정 문자열을 포함하는 [ParkingDocument] 객체들을 Elasticsearch 에서 검색합니다.
     *
     * @param name 검색할 부분 문자열. 예를 들어, "보라매"이 입력되면 "보라매 공원 주차장" 등이 검색됩니다.
     * @return 이름 필드에 [name]을 포함하는 [ParkingDocument] 리스트.
     */
    fun findByNameContaining(name:String): List<ParkingDocument>//name 필드에 대해 부분검색

    /**
     * 지정한 좌표 [location] 근처에 위치한 [ParkingDocument] 객체들을 Elasticsearch에서 검색합니다.
     *
     * @param location 검색의 중심이 되는 좌표 정보.
     * @param distance [location]을 중심으로 검색할 반경 거리 [String] 타입 (예: "5km").
     * @return 지정된 좌표 근처의 주차장 문서들의 [ParkingDocument] 리스트.
     */
    fun findByLocationNear(location: GeoPoint, distance: String): List<ParkingDocument> // 좌표 기반 검색

}
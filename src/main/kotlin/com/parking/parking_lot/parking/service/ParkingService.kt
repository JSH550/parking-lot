package com.parking.parking_lot.parking.service

import com.parking.parking_lot.parking.dto.ParkingSearchDto

interface ParkingService {


    /**
     * 주어진 검색어를 포함하는 주차장을 검색합니다.
     *
     * 1. Elasticsearch에서 검색어를 포함하는 주차장을 조회합니다.
     * 2. 조회된 결과를 `ParkingSearchDto`로 변환하여 반환합니다.
     *
     * @param searchWord 검색할 주차장 이름의 일부 또는 전체 문자열
     * @return 검색된 주차장의 정보를 담은 [ParkingSearchDto] 리스트. 검색 결과가 없으면 빈 리스트 반환.
     */
    fun searchBySearchWord(searchWord:String): List<ParkingSearchDto>


    /**
     * 주어진 탐색 범위(`distance`) 및 좌표(`latitude`, `longitude`)를 기준으로 주차장을 검색합니다.
     *
     * 1. Elasticsearch에서 특정 위치(`latitude`, `longitude`)를 기준으로 `distance` 범위 내의 주차장을 검색합니다.
     * 2. 조회된 결과를 `ParkingSearchDto`로 변환하여 반환합니다.
     *
     * @param distance 검색 반경 (예: "5km", "500m" 등).
     * @param latitude 검색할 위치의 위도 (예: 37.5665).
     * @param longitude 검색할 위치의 경도 (예: 126.9780).
     *
     * @return 검색된 주차장의 정보를 담은 [ParkingSearchDto] 리스트. 검색 결과가 없으면 빈 리스트 반환.
     */
    fun searchByLocation(distance:String, latitude:Double, longitude:Double): List<ParkingSearchDto>
}
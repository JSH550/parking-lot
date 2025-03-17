package com.parking.parking_lot.parking

import com.parking.parking_lot.common.batch.ParkingItemProcessor
import com.parking.parking_lot.parking.dto.LocationSearchDto
import com.parking.parking_lot.parking.dto.ParkingSearchDto
import com.parking.parking_lot.parking.service.ParkingService
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parkings")
class ParkingController(
    private val parkingService: ParkingService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingController::class.java)
    }


    //단어검색
    @GetMapping
    fun searchByWord(
        @RequestParam searchWord: String
    ): ResponseEntity<List<ParkingSearchDto>> {

        if(searchWord.isBlank()){
            throw IllegalArgumentException("검색어가 비어있습니다.")

        }
        if (!searchWord.matches(Regex("^[가-힣a-zA-Z]{1,30}$"))){
            throw IllegalArgumentException("검색어는 한글,영어 포함 30자이내로 작성해주세요.")

        }
        return ResponseEntity.ok(parkingService.searchBySearchWord(searchWord));

    }


    @PostMapping("/locations")
    fun searchByLocation(
        @Valid @RequestBody data: LocationSearchDto,
            ):ResponseEntity<List<ParkingSearchDto>>{

        return ResponseEntity.ok(parkingService.searchByLocation(data.distance,data.latitude,data.longitude))

    }


}
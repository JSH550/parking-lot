package com.parking.parking_lot.common.batch

import com.parking.parking_lot.operationHours.OperatingHoursRepository
import com.parking.parking_lot.operationHours.dto.OperationHoursDto
import com.parking.parking_lot.operationHours.service.OperatingHoursService
import com.parking.parking_lot.parking.dto.ParkingConvertDto
import com.parking.parking_lot.parking.repository.ParkingRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class ParkingItemReader(
    private val parkingRepository: ParkingRepository,
) : ItemReader<ParkingConvertDto> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingItemReader::class.java)
    }

    private var currentPage = 0//페이지 번호
    private val pageSize = 500//한번에 가져올 레코드의 수
    private var currentItems: List<ParkingConvertDto> = emptyList()
    private var index = AtomicInteger(0)

    override fun read(): ParkingConvertDto? { // 반환 타입 수정
        // 현재 리스트에서 모두 읽었으면 다음 페이지를 로드
        if (index.get() >= currentItems.size) {

            val page = parkingRepository.findAll(PageRequest.of(currentPage, pageSize))//페이지네이션정보로 레코드 조회
            if (page.hasContent()) {
                currentItems = page.content.map { parking -> //  변환된 DTO를 currentItems에 저장

                    val operatingHours = parking.operatingHours.map { OperationHoursDto.fromOperating(it) }
                    //Parking ID 로 운영시간 레코드를 조회하여 DTO 로 변환
//                    val operatingHours = parking.id?.let { operatingHoursService.getByParkingId(it) } ?: emptyList()
                    //Parking 레코드를 DTO로 변환
                    ParkingConvertDto(
                        id = parking.id ?: throw IllegalStateException("Parking ID가 null입니다."),
                        name = parking.name,
                        address = parking.address,
                        latitude = parking.latitude,
                        longitude = parking.longitude,
                        feePerHour = parking.feePerHour,
                        ownership = parking.ownership,
                        operatingHours = operatingHours // 운영시간 포함
                    )
                }

                index.set(0)
                currentPage++ // 페이지 번호 증가
                logger.info("새로운 레코드 로드 페이지네이션 정보 (페이지: $currentPage, 항목 수: ${currentItems.size})")
            } else {
                logger.info("DBMS 모든 Parking 레코드 읽기 완료.") // DB의 레코드를 모두 읽었을 때
                return null
            }
        }

        // 리스트에서 개별 항목 반환
        return currentItems.getOrNull(index.getAndIncrement())
    }
}
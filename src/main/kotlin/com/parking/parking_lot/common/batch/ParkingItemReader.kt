package com.parking.parking_lot.common.batch

import com.parking.parking_lot.parking.Parking
import com.parking.parking_lot.parking.repository.ParkingRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemReader
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class ParkingItemReader(
    private val parkingRepository: ParkingRepository
) : ItemReader<Parking> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingItemReader::class.java)
    }

    private var currentPage = 0//페이지 번호
    private val pageSize = 500//한번에 가져올 레코드의 수
    private var currentItems: List<Parking> = emptyList()
    private var index = AtomicInteger(0)

    override fun read(): Parking? {
        // 현재 리스트에서 모두 읽었으면 다음 페이지를 로드
        if (index.get() >= currentItems.size) {
            val page = parkingRepository.findAll(PageRequest.of(currentPage, pageSize))
            if (page.hasContent()) {
                currentItems = page.content//페이지 사이즈 지정
                index.set(0)
                currentPage++//페이지 번호 증가
                logger.info("새로운 페이지 로드 (페이지: $currentPage, 항목 수: ${currentItems.size})")
            } else {
                logger.info("모든 Parking 레코드 읽기 완료.")//DB의 레코드를 모두 읽었을때
                return null
            }
        }

        // 리스트에서 개별 항목 반환
        return currentItems.getOrNull(index.getAndIncrement())
    }
}
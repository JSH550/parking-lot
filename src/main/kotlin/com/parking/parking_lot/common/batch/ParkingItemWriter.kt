package com.parking.parking_lot.common.batch

import com.parking.parking_lot.parking.ParkingDocument
import com.parking.parking_lot.parking.repository.ParkingElasticSearchRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class ParkingItemWriter(
    private val parkingElasticSearchRepository: ParkingElasticSearchRepository
) : ItemWriter<ParkingDocument> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingItemWriter::class.java)
    }

    override fun write(items: Chunk<out ParkingDocument>) {
        logger.info("💾 ParkingItemWriter - 총 ${items.size()}개 저장 시도")

        try {
            parkingElasticSearchRepository.saveAll(items)
            logger.info("✅ 저장 완료: ${items.size()}개")
        } catch (e: Exception) {
            logger.error("🚨 저장 실패: ${e.message}")
            items.forEach { item ->
                logger.error("❌ 저장 실패한 문서 - ID: ${item.id}, Name: ${item.name} ${item.toString()}" )
            }
        }
    }
}
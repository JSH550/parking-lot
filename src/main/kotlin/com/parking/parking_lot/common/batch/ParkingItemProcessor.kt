package com.parking.parking_lot.common.batch

import com.parking.parking_lot.parking.Ownership
import com.parking.parking_lot.parking.Parking
import com.parking.parking_lot.parking.ParkingDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.data.elasticsearch.core.geo.GeoPoint
import org.springframework.stereotype.Component


@Component
class ParkingItemProcessor : ItemProcessor<Parking, ParkingDocument> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingItemProcessor::class.java)
    }

    override fun process(item: Parking): ParkingDocument? {
        return try {
            ParkingDocument(
                id = item.id,
                name = item.name,
                address = item.address ?: "주소 없음",
                location = GeoPoint(item.latitude, item.longitude),
                feePerHour = item.feePerHour ?: 0,
                ownership = item.ownership?: Ownership.PUBLIC,
            ).also {
//                logger.info("🚗 변환 성공 - ID: ${it.id}, Name: ${it.name}")
            }
        } catch (e: Exception) {
            logger.error("❌ 변환 실패 - ID: ${item.id}, 원인: ${e.message}")
            null
        }
    }
}
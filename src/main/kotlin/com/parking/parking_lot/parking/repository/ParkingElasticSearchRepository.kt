package com.parking.parking_lot.parking.repository

import com.parking.parking_lot.parking.ParkingDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository


interface ParkingElasticSearchRepository : ElasticsearchRepository<ParkingDocument,String>{

}
package com.parking.parking_lot.parking

import org.springframework.beans.factory.parsing.Location
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.GeoPointField
import org.springframework.data.elasticsearch.core.geo.GeoPoint

//Elastic-Search 전용

@Document(indexName = "parking")
data class ParkingDocument (


    @Id
    val id: Long,//Main DB와 일치시킬것!!

    @Field(type = FieldType.Text, analyzer = "korean_nori_analyzer")//nori 사용
    val name: String,//이름
    @Field(type = FieldType.Text, analyzer = "korean_nori_analyzer")//nori 사용
    val address: String,//도로명 주소

    val feePerHour : Int,//시간당요금

    //위도,경도 정보 elasticsearch 에서 geo_point로 저장됨
    @GeoPointField
    val location: GeoPoint//예 "37.5666,126.9780"



)
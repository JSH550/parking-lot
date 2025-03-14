package com.parking.parking_lot.parking

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.annotations.GeoPointField
import org.springframework.data.elasticsearch.core.geo.GeoPoint

//Elastic-Search 전용

@Document(indexName = "parking")
@JsonIgnoreProperties(ignoreUnknown = true) //_class 등 라이브러리에서 생성하는 컬럼들 무시
data class ParkingDocument @JsonCreator constructor(

    @Id
    @JsonProperty("id")//Jackson이 필드명 찾을수 있게 매핑@JsonCreator 와 같이사용
    val id: Long,  // Main DB와 일치시킬 것!!

    @Field(type = FieldType.Text, analyzer = "korean_nori_analyzer")
    @JsonProperty("name")
    val name: String, // 이름

    @Field(type = FieldType.Text, analyzer = "korean_nori_analyzer")
    @JsonProperty("address")
    val address: String, // 도로명 주소

    @JsonProperty("feePerHour")
    val feePerHour: Int, // 시간당 요금

    @JsonProperty("ownership")
    val ownership: Ownership?=Ownership.PUBLIC ,

    @GeoPointField
    @JsonProperty("location")
    val location: GeoPoint // 예 "37.5666,126.9780"
)
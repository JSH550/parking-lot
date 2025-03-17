package com.parking.parking_lot.parking.repository


import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch.core.SearchRequest
import co.elastic.clients.elasticsearch.core.SearchResponse
import com.parking.parking_lot.parking.ParkingDocument
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomParkingElasticSearchRepositoryImpl(
    private val elasticsearchClient: ElasticsearchClient
) : CustomParkingElasticSearchRepository {


    override fun searchByNameOrAddress(searchWord: String, pageable: Pageable): List<ParkingDocument> {

        //native query 생성
        val searchRequest = SearchRequest.Builder()
            .index("parking") // Elasticsearch 인덱스 이름
            .query { q ->
                q.multiMatch { mm ->
                    mm.query(searchWord)
                        .fields("name", "address") // 검색 필드 지정
                }
            }
            .from(pageable.offset.toInt()) // 옵셋 계산, 시작위치
            .size(pageable.pageSize) // 페이지 크기 지정
            .build()

        //Query 요청, 결과를 ParkingDocument 로 매핑, 직렬화,역직렬화 설정 필수(Document class)
        val response: SearchResponse<ParkingDocument> =
            elasticsearchClient.search(searchRequest, ParkingDocument::class.java)

        //결과 반환
        return response.hits().hits().mapNotNull { it.source() }//_source 안의 필드값들만 반환

    }

}


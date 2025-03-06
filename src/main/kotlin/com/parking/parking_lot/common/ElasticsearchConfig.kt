package com.parking.parking_lot.common

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.JsonData
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Configuration
@EnableElasticsearchRepositories
class ElasticsearchConfig {

    @Value("\${spring.elasticsearch.uris}")
    lateinit var url: String

    @Value("\${spring.elasticsearch.password}")
    lateinit var password: String

    @Bean
    fun elasticsearchRestClient(): RestClient {

        // TrustManager 설정 (모든 인증서 신뢰)
        val trustAllCertificates: TrustManager = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(certificates: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certificates: Array<X509Certificate>, authType: String) {}
        }

        // SSL 컨텍스트 생성
        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustAllCertificates), java.security.SecureRandom())

        // RestClientBuilder 생성
        val builder: RestClientBuilder = RestClient.builder(HttpHost("localhost", 9200, "https"))

        builder.setHttpClientConfigCallback { httpClientBuilder ->
            httpClientBuilder.setSSLContext(sslContext)  // SSL 검증 비활성화
            httpClientBuilder.setSSLHostnameVerifier { _, _ -> true }  // 호스트 이름 검증 비활성화
        }

        return builder.build()  // RestClient 반환
    }

}

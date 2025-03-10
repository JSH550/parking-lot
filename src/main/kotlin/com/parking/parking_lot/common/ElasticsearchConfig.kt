package com.parking.parking_lot.common

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Configuration
class ElasticsearchConfig(
    @Value("\${spring.elasticsearch.password}") private val password: String,//생성자주입

    @Value("\${spring.elasticsearch.username}") private val username: String,

    @Value("\${spring.elasticsearch.host}") private  val host : String,

    @Value("\${spring.elasticsearch.port}") private val port : Int,

) {

    /**
     * SSL 인증 검사를 비활성화하는 메서드입니다.
     *
     * - 개발환경에서만 사용합니다.
     *
     * @return SSLContext 객체 (모든 인증서를 신뢰하는 설정 적용)
     */
    fun disableSSLVerification(): SSLContext {
        val trustAllCertificates = arrayOf<TrustManager>(
            object : X509TrustManager {
                //클라이언트 인증 비활성화
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
                //서버 인증 비활성화
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>?, authType: String?) {}
                //CA 목록 null 반환, 모든기관 신뢰
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate>? = null
            }
        )

        //TSL 프로토콜 사용하는 SSLContext 객체 생성
        val sslContext = SSLContext.getInstance("TLS")
        // TrustManager를 사용하여 SSLContext 초기화 (모든 인증서 신뢰)
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        //SSL 인증 비활성화된 Context 반환
        return sslContext
    }


    /**
     * Elasticsearch의 `RestClient`를 생성하는 Bean입니다.
     *
     * - Elasticsearch 서버와의 통신시 사용되는  REST 클라이언트입니다.
     * - 인증 정보를 설정하여 Elasticsearch 서버와의 보안 연결을 유지합니다.
     * - SSL 인증을 비활성화하여 HTTPS 연결 시 인증서 검사를 수행하지 않습니다.(개발환경에서만 사용)
     *
     * @return `RestClient` 객체
     */
    @Bean
    fun restClient(): RestClient {
        val credentialsProvider = BasicCredentialsProvider().apply {
            setCredentials(AuthScope.ANY, UsernamePasswordCredentials(username ,password))//엘라스틱서치 인증 정보
        }

        return RestClient.builder(
            HttpHost(host, port, "https") // HTTPS 사용
        )
            .setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)//기본인증서사용
                    .setSSLContext(disableSSLVerification()) // SSL 검증 비활성화
            }
            .build()
    }

    /**
     * Elasticsearch 클라이언트를 생성하는 Bean입니다.
     *
     * - [RestClient]를 이용하여 [ElasticsearchClient]를 생성합니다.
     * - JSON 직렬화 및 역직렬화를 위해 [JacksonJsonpMapper]를 사용합니다.
     *
     * @param restClient `RestClient` 객체 (Spring Bean으로 주입됨).
     * @return [ElasticsearchClient] 객체
     */
    @Bean
    fun elasticsearchClient(restClient: RestClient): ElasticsearchClient {
        //Elasticsearch 요청/응답 직렬화,역직렬화
        val transport = RestClientTransport(
            restClient,
            JacksonJsonpMapper()//JSON 변환 설정
        )
        return ElasticsearchClient(transport)
    }
}

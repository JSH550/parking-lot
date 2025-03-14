package com.parking.parking_lot.common.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class BatchStartupRunner (

    private val jobLauncher: JobLauncher,
    private val parkingSyncJob : Job
){

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BatchStartupRunner::class.java)
    }

    @Bean
    fun runBatchOnStartup(): ApplicationRunner {
        return ApplicationRunner {
            try {
                logger.info("애플리케이션 시작, Parking 배치 실행...")

                val jobParameters = JobParametersBuilder()
                    .addString("runTime", LocalDateTime.now().toString()) // 실행 시간 추가
                    .addLong("runId", System.currentTimeMillis()) // 중복 실행 방지
                    .toJobParameters()

                val jobExecution = jobLauncher.run(parkingSyncJob, jobParameters)

                logger.info("배치 실행 완료: ${jobExecution.status}")
            } catch (e: Exception) {
                logger.error("배치 실행 중 오류 발생: ${e.message}", e)
            }
        }
    }

}
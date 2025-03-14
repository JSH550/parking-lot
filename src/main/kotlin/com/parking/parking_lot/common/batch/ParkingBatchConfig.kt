package com.parking.parking_lot.common.batch

import com.parking.parking_lot.parking.Parking
import com.parking.parking_lot.parking.ParkingDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@EnableBatchProcessing
class ParkingBatchConfig(

    private val jobRepository: JobRepository,
    private val reader: ParkingItemReader,
    private val writer: ParkingItemWriter,
    private val processor: ParkingItemProcessor,
    private val transactionManager: PlatformTransactionManager,


) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParkingBatchConfig::class.java)
    }

    @Bean
    fun syncParkingStep(): Step {
        return StepBuilder("syncParkingStep", jobRepository)
            .chunk<Parking, ParkingDocument>(500, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build()
    }

    @Bean
    fun parkingSyncJob(): Job {
        return JobBuilder("parkingSyncJob", jobRepository)
            .start(syncParkingStep())
            .build()
    }

}








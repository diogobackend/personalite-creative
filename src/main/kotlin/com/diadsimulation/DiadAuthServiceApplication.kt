package com.diadsimulation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.diadsimulation"])
@EnableJpaRepositories(basePackages = ["com.diadsimulation"])
@EntityScan(basePackages = ["com.diadsimulation"])
class DiadAuthServiceApplication

fun main(args: Array<String>) {
    runApplication<DiadAuthServiceApplication>(*args)
}

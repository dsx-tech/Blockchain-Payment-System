package dsx.bps.DBservices

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class Datasource {
    fun getHicari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;"
        config.maximumPoolSize = 5
        config.validate()
        return HikariDataSource(config)
    }
}
package dsx.bps.DBservices

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class Datasource() {
    fun getHicari(connectionURL: String, driver: String): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = driver
        config.jdbcUrl = connectionURL
        config.maximumPoolSize = 5
        config.validate()
        return HikariDataSource(config)
    }
}
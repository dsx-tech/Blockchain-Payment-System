package dsx.bps.DBservices

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class Datasource {
    fun getHicari(): HikariDataSource {
        val config = HikariConfig()
        config.driverClassName = "org.h2.Driver"
        config.jdbcUrl = "jdbc:h2:~/db"
        config.maximumPoolSize = 5
        config.validate()
        return HikariDataSource(config)
    }
}
package dsx.bps.DBservices

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class Datasource() {
    companion object{
        var conn: Database? = null
        fun getHicari(connectionURL: String, driver: String): Database {
            if (conn == null) {
                val config = HikariConfig()
                config.driverClassName = driver
                config.jdbcUrl = connectionURL
                config.maximumPoolSize = 5
                config.validate()
                conn = Database.Companion.connect(HikariDataSource(config))
                return conn!!
            }
            else
                return conn!!
        }
    }
}
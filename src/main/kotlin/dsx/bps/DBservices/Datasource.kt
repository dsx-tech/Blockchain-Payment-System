package dsx.bps.DBservices

import com.uchuhimo.konf.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dsx.bps.config.DatabaseConfig
import dsx.bps.exception.DBservices.BpsDatabaseException
import org.jetbrains.exposed.sql.Database

class Datasource() {
    companion object{
        var conn: Database? = null

        fun initConnection(conf: Config) {
            val config = HikariConfig()
            config.driverClassName = conf[DatabaseConfig.driver]
            config.jdbcUrl = conf[DatabaseConfig.connectionURL]
            config.maximumPoolSize = conf[DatabaseConfig.maximumPoolSize].toInt()
            config.validate()
            conn = Database.Companion.connect(HikariDataSource(config))
        }

        fun getConnection(): Database {
            return conn
                ?: throw BpsDatabaseException("connection was not initialized")
        }
    }
}
package io.disc99.hogan

import io.disc99.hogan.parser.Table
import groovy.sql.Sql
import io.disc99.hogan.parser.TableParser

import javax.sql.DataSource
import java.sql.Connection

/**
 * Database utility class.
 */
class Database {

    Sql sql

    /**
     * Create Database instance.
     *
     * @param sql
     */
    Database(Sql sql) {
        this.sql = sql
    }
    /**
     * Create Database instance.
     *
     * @param connection
     * @see groovy.sql.Sql
     */
    Database(Connection connection) {
        this.sql = new Sql(connection)
    }
    /**
     * Create Database instance.
     *
     * @param dataSource
     * @see groovy.sql.Sql
     */
    Database(DataSource dataSource) {
        this.sql = new Sql(dataSource)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @see groovy.sql.Sql
     */
    Database(String url) {
        this.sql = Sql.newInstance(url)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @param properties
     * @see groovy.sql.Sql
     */
    Database(String url, Properties properties) {
        this.sql = Sql.newInstance(url, properties)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @param properties
     * @param driverClassName
     * @see groovy.sql.Sql
     */
    Database(String url, Properties properties, String driverClassName) {
        this.sql = Sql.newInstance(url, properties, driverClassName)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @param user
     * @param password
     * @see groovy.sql.Sql
     */
    Database(String url, String user, String password) {
        this.sql = Sql.newInstance(url, user, password)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @param user
     * @param password
     * @param driverClassName
     * @see groovy.sql.Sql
     */
    Database(String url, String user, String password, String driverClassName) {
        this.sql = Sql.newInstance(url, user, password, driverClassName)
    }
    /**
     * Create Database instance.
     *
     * @param url
     * @param driverClassName
     * @see groovy.sql.Sql
     */
    Database(String url, String driverClassName)  {
        this.sql = Sql.newInstance(url, driverClassName)
    }
    /**
     * Create Database instance.
     *
     * @param args
     * @see groovy.sql.Sql
     */
    Database(Map<String, Object> args)  {
        this.sql = Sql.newInstance(args)
    }

    /**
     * Execute insert from Data table DSL.
     *
     * @param tables
     */
    void insert(tables) {
        Map<String, Closure> tableMap = (Map<String, Closure>)tables
        tableMap.each { name, table ->
            TableParser.asTable(table).toMapList().each {
                String[] names = name.split(":")
                sql.dataSet(names[0]).add(it)
            }
        }
    }

    /**
     * Expect from Data table DSL.
     *
     * @param tables
     */
    @Beta
    void 'assert'(tableDefs) {
        Map<String, Closure> tableMap = (Map<String, Closure>)tableDefs

        tableMap.each { name, tableDef ->
            String[] names = name.split(":")
            Table table = TableParser.asTable(tableDef)
            List<Map<String, Object>> tables = table.toMapList()
            String expected = tables.toString()

            List<String> upperCols = table.columns.collect { it.name.toUpperCase() }
            String query =  sql.dataSet(names[0]).getSql()
            if (names.length == 2) {
                query += " where " + names[1]
            }
            List<Map<String, Object>> rows = sql.rows(query).collect {
                it.subMap(upperCols).collectEntries({k, v -> [findByUpperCol(table, k), v]})
            }
            String actual = rows.toString()

            assert actual == expected
        }
    }

    String findByUpperCol(Table table, String col) {
        table.columns.find({it.name.toUpperCase() == col}).name
    }

    /**
     * Commit the connection.
     *
     * @see groovy.sql.Sql#commit
     */
    void commit() {
        sql.commit()
    }
    /**
     * Rollback the connection.
     *
     * @see groovy.sql.Sql#rollback
     */
    void rollback() {
        sql.rollback()
    }
    /**
     * Close the connection.
     *
     * @see groovy.sql.Sql#close
     */
    void close() {
        sql.close()
    }
    /**
     * Get {@link groovy.sql.Sql} object
     * @return
     */
    Sql getSql() {
        sql
    }
}

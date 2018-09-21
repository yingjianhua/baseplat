package com.irille.core.repository.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager {
	private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	private static final ThreadLocal<Connection> current_thread_connection = new ThreadLocal<>();

    static ComboPooledDataSource dataSource = new ComboPooledDataSource();

	public static void main(String[] args) {
		
	}
	public static Connection getConnection() throws SQLException {
		if(current_thread_connection.get()==null)
			current_thread_connection.set(dataSource.getConnection());
		Connection connection = current_thread_connection.get();
		if(connection.getAutoCommit())
			connection.setAutoCommit(false);
		return current_thread_connection.get();
	}
	public static void releaseConnection() throws SQLException {
		if(current_thread_connection.get()!=null)
			current_thread_connection.remove();
		closeConnection();
	}
	public static void closeConnection() throws SQLException {
		Connection connection = current_thread_connection.get();
		if(connection!=null&&!connection.isClosed())
			connection.close();
	}
	public static void commitConnection() throws SQLException {
		Connection connection = current_thread_connection.get();
		if(connection!=null)
			connection.commit();
	}
	public static void rollbackConnection() throws SQLException {
		Connection connection = current_thread_connection.get();
		if(connection!=null)
			connection.rollback();
	}
}

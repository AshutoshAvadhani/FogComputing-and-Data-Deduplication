package com.project.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {


	private String dbUsername = "root";
	private String dbPassword = "root";
	private String dbDriver = "com.mysql.jdbc.Driver";
	private String dbConnectionString = "jdbc:mysql://localhost:3306/fog_homecloud_db";
	private Connection connection = null;

	public Connection connect() {
		{
			try {
				Class.forName(dbDriver);  //to load jdbc driver
				connection = DriverManager.getConnection(dbConnectionString,
						dbUsername, dbPassword);
				if (connection == null) {
					System.out.println("connection is not established");
				}
				return connection;
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return null;
	}
}

package com.example;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The properties used for configuring the taskmadness tasks.
 *
 * @author Glenn Renfro
 */
@ConfigurationProperties("taskmadness")
public class TaskMadnessProperties {

	/**
	 * Connection URL to the database
	 */
	private String databaseUrl = "jdbc:mariadb://localhost:3306/practice";

	/**
	 * Database driver class name
	 */
	private String driverClassName = "org.mariadb.jdbc.Driver";

	/**
	 * User name to the database
	 */
	private String userName = "root";

	/**
	 * Password to the database
	 */
	private String password = "password";

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

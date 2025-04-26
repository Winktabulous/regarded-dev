package com.zenyte.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {

	private static final Logger log = LoggerFactory.getLogger(SQLManager.class);
	public static final String DATABASE_IP = "15.204.210.250";
	public static final int DATABASE_PORT = 3306;
	public static final String GAME_DATABASE_NAME = "analytics";

	public static final String DATABASE_URL = DATABASE_IP + ":" + DATABASE_PORT + "/"
			+ GAME_DATABASE_NAME + "?autoReconnect=true&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true";

	public static final String USERNAME = "leanbow";

	public static final String PASSWORD = "kwo1eS4bR&syKW4#3kTrEK90@Nf9*QU3V!Q*mQwuDjWd2jh4I^";

	public static final int CONNECT_TIMEOUT = 5000;

	private static HikariDataSource ds;

	private static volatile boolean initialized;

	public SQLManager() {
		/* empty */
	}

	public static void init() {
		try {
			final HikariConfig config = new HikariConfig();

			config.setJdbcUrl("jdbc:mysql://" + DATABASE_URL);
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			config.setUsername(USERNAME);
			config.setPassword(PASSWORD);

			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "500");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.addDataSourceProperty("useServerPrepStmts", "true");

			config.addDataSourceProperty("useLocalSessionState", "true");
			config.addDataSourceProperty("useLocalTransactionState", "true");
			config.addDataSourceProperty("rewriteBatchedStatements", "true");
			config.addDataSourceProperty("cacheResultSetMetadata", "true");
			config.addDataSourceProperty("cacheServerConfiguration", "true");
			config.addDataSourceProperty("elideSetAutoCommits", "true");
			config.addDataSourceProperty("maintainTimeStats", "false");

			config.addDataSourceProperty("max_allowed_packet", "1073741824");
			config.addDataSourceProperty("maxAllowedPacket", "1073741824");

			config.setConnectionTimeout(CONNECT_TIMEOUT); // never "freeze" for a long time no matter what happens
			config.setLeakDetectionThreshold(10_000); // detect leaks as fast as possible
			config.setMaximumPoolSize((Runtime.getRuntime().availableProcessors() * 2) + 1); // ensure deadlock never occurs

			ds = new HikariDataSource(config);
		} catch (Exception e) {
			e.printStackTrace();
			initialized = false;
			return;
		}
		initialized = true;
	}

	public static Connection getConnection() {
		if (ds == null) {
			return null;
		}

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			log.error("Error: Mysql error message=" + e.getMessage() + ".");
		}

		return null;
	}

	public static boolean close(PreparedStatement statement) {
		try {
			return close(statement == null ? null : statement.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static boolean use(UseSQLConnection useSQLConnection) {
		final Connection connection = getConnection();
		if (connection != null) {
			boolean closed;
			try {
				useSQLConnection.useConnection(connection);
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				closed = close(connection);
			}
			return closed;
		}
		return false;
	}

}

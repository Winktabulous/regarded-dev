package com.zenyte.utils;

import java.sql.Connection;
import java.sql.SQLException;

public interface UseSQLConnection {

	void useConnection(Connection connection) throws SQLException;

}

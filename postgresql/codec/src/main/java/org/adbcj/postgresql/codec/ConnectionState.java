package org.adbcj.postgresql.codec;

import java.nio.charset.Charset;

/**
 * @author Mike Heath
 */
public interface ConnectionState {
	Charset getBackendCharset();

	PgField[] getCurrentResultSetFields();

	void setCurrentResultSetFields(PgField[] fields);

	String getDatabaseName();
}
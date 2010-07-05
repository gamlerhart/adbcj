/*
 *   Copyright (c) 2007 Mike Heath.  All rights reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.adbcj.postgresql.codec;

import org.adbcj.ConnectionManager;

public abstract class AbstractConnectionManager implements ConnectionManager {

	private final String username;
	private final String password;
	private final String database;

	public AbstractConnectionManager(String username, String password, String database) {
		this.username = username;
		this.password = password;
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDatabase() {
		return database;
	}

	@Override
	public String toString() {
		return getClass().getName() + "{" +
				"database='" + database + '\'' +
				'}';
	}
}

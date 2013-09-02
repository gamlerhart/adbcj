package org.adbcj.dbcj;

import java.sql.*;

import org.adbcj.ConnectionManagerProvider;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author foooling@gmail.com
 */
public class AdaptedDriver implements java.sql.Driver {
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        org.adbcj.Connection realConnection;
        try{
            realConnection=ConnectionManagerProvider.createConnectionManager(url,info.getProperty("user"),info.getProperty("password")).connect().get();
        }catch (Exception e){
            throw new SQLException("connection establish fail");
        }

        return new ConnectionImpl(realConnection);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMajorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getMinorVersion() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean jdbcCompliant() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

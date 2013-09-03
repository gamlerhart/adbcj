package org.adbcj.dbcj;

import java.sql.*;

import org.adbcj.ConnectionManagerProvider;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.adbcj.ConnectionManager;

/**
 * @author foooling@gmail.com
 */
public class AdaptedDriver implements java.sql.Driver {
    protected ConcurrentHashMap<String,ConnectionManager>managerConcurrentHashMap=new ConcurrentHashMap<String, ConnectionManager>();
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        org.adbcj.Connection realConnection;
        StringBuilder stringBuilder=new StringBuilder();

        String user=info.getProperty("user");
        String password=info.getProperty("password");

        // [+-+] is used to split strings , decrease rate of collision
        // TODO: not work in special situations , multi-key is better
        stringBuilder.append(url).append("+-+").append(user).append("+-+").append(password);
        String key= stringBuilder.toString();
        ConnectionManager connectionManager=managerConcurrentHashMap.get(key);

        if (connectionManager==null){
            connectionManager=ConnectionManagerProvider.createConnectionManager(url,user,password);
            managerConcurrentHashMap.putIfAbsent(key,connectionManager);
        }else if (connectionManager.isClosed()){
            connectionManager=ConnectionManagerProvider.createConnectionManager(url,user,password);
            managerConcurrentHashMap.put(key,connectionManager);
        }
        try{
            realConnection=connectionManager.connect().get();
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

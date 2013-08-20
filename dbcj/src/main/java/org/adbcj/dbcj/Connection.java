package org.adbcj.dbcj;

import org.adbcj.DbFuture;
import org.adbcj.PreparedQuery;
import org.adbcj.PreparedUpdate;

/**
 * Created with IntelliJ IDEA.
 * User: fooling
 * Date: 13-8-15
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
public interface Connection extends java.sql.Connection,ConnectionProperties{
    public DbFuture<PreparedQuery> prepareQuery(String sql);
    public DbFuture<PreparedUpdate> prepareUpdate(String sql);


}

package org.adbcj.tck;

/**
 * @author roman.stoffel@gamlor.info
 */
public class InitMysql extends InitDatabase {
    @Override
    protected void loadDriver() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //Class.forName("org.adbcj.dbcj.Driver");
    }

    @Override
    protected String setupScript() {
        return "sql/setupMySQL.sql";
    }

    @Override
    protected String tearDownScript() {
        return "sql/cleanUpMySQL.sql";
    }
}

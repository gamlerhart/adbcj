package info.adbcj.demo;

import org.adbcj.*;


/**
 * @author roman.stoffel@gamlor.info
 */
public class MainDemo {

    public static void main(String[] args) {

        // We assume we have a MySQL server running on localhost
        // Database name: adbcj-demo
        // User: adbcj
        // Password: adbc-pwd

        // A connection manager creates new connections for you.
        // Usually you have one instance in your system.
        // when you close the connection-manager, all associated connections are closed to.
        final ConnectionManager connectionManager = ConnectionManagerProvider.createConnectionManager(
                "adbcj:pooled:mysql://localhost/adbcjtck",
                "adbcjtck",
                "adbcjtck"
        );

        // Connect to your database. It's asynchronous.
        // This means we react on it when done
        final DbFuture<Connection> connect = connectionManager.connect();
        connect.addListener(new DbListener<Connection>() {
            @Override
            public void onCompletion(DbFuture<Connection> connectionDbFuture) {
                switch (connectionDbFuture.getState()){
                    case SUCCESS:
                        final Connection connection = connectionDbFuture.getResult();
                        continueAndCreateSchema(connection);
                        break;
                    case FAILURE:
                        connectionDbFuture.getException().printStackTrace();
                        break;
                    case CANCELLED:
                        System.out.println("Cancelled");
                        break;
                }
            }
        });
    }

    private static void continueAndCreateSchema(final Connection connection) {
        // Again, we send the query and add a listener to react to it
        connection.executeUpdate("CREATE TABLE IF NOT EXISTS posts(\n" +
                "  id int NOT NULL AUTO_INCREMENT,\n" +
                "  title varchar(255) NOT NULL,\n" +
                "  content TEXT NOT NULL,\n" +
                "  PRIMARY KEY (id)\n" +
                ") ENGINE = INNODB;").addListener(new DbListener<Result>() {
            @Override
            public void onCompletion(DbFuture<Result> resultDbFuture) {
                switch (resultDbFuture.getState()) {
                    case SUCCESS:
                        System.out.println("Created Schema, Inserting");
                        continueWithInserting(connection);
                        break;
                    case FAILURE:
                        resultDbFuture.getException().printStackTrace();
                        break;
                    case CANCELLED:
                        System.out.println("Cancelled");
                        break;
                }
            }
        });
    }

    private static void continueWithInserting(final Connection connection) {
        // We can directly send multiple queries
        // And then wait until everyone is done.
        final DbFuture<Result> firstPost = connection.executeUpdate("INSERT INTO posts(title,content) VALUES('The Title','TheContent')");
        final DbFuture<Result> secondPost = connection.executeUpdate("INSERT INTO posts(title,content) VALUES('Second Title','More Content')");
        final DbFuture<Result> thirdPost = connection.executeUpdate("INSERT INTO posts(title,content) VALUES('Third Title','Even More Content')");
        final DbListener<Result> allDone = new DbListener<Result>() {
            @Override
            public void onCompletion(DbFuture<Result> resultSetDbFuture) {
                switch (resultSetDbFuture.getState()) {
                    case SUCCESS:
                        // Check if everyone is done
                        if(firstPost.isDone()&&secondPost.isDone()&&thirdPost.isDone()){
                            continueWithSelect(connection);
                        }
                        break;
                    case FAILURE:
                        resultSetDbFuture.getException().printStackTrace();
                        break;
                    case CANCELLED:
                        System.out.println("Cancelled");
                        break;
                }

            }
        };
        // Register the listener to all instances
        firstPost.addListener(allDone);
        secondPost.addListener(allDone);
        thirdPost.addListener(allDone);
    }

    private static void continueWithSelect(final Connection connection) {
        connection.executeQuery("SELECT * FROM posts").addListener(new DbListener<ResultSet>() {
            @Override
            public void onCompletion(DbFuture<ResultSet> resultSetDbFuture) {
                switch (resultSetDbFuture.getState()) {
                    case SUCCESS:
                        listResultSet(resultSetDbFuture.getResult());

                        // result sets are immutable. You can close the connection
                        connection.close();
                        break;
                    case FAILURE:
                        resultSetDbFuture.getException().printStackTrace();
                        break;
                    case CANCELLED:
                        System.out.println("Cancelled");
                        break;
                }
            }
        });
    }

    private static void listResultSet(ResultSet result) {
        for (Row row : result) {
            System.out.println("ID: "+row.get("ID").getLong()+" with title "+row.get("title").getString());
        }
    }
}

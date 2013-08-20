package info.adbcj.demo;

import org.adbcj.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author roman.stoffel@gamlor.info
 */
public class Failure {
    public static void main(String[] args) {

        Map<String,String> settings = new HashMap<String,String>();
        settings.put(StandardProperties.CAPTURE_CALL_STACK,"true");

        final ConnectionManager connectionManager = ConnectionManagerProvider.createConnectionManager(
                "adbcj:pooled:mysql://localhost/adbcj-demo",
                "adbcj",
                "adbc-pwd",
                settings
        );

        // Connect to your database. It's asynchronous.
        // This means we react on it when done
        final DbFuture<Connection> connect = connectionManager.connect();
        connect.addListener(new DbListener<Connection>() {
            @Override
            public void onCompletion(DbFuture<Connection> connectionDbFuture) {
                switch (connectionDbFuture.getState()) {
                    case SUCCESS:
                        final Connection connection = connectionDbFuture.getResult();
                        try {
                            continueInvalidQuery(connection);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
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

    private static void continueInvalidQuery(Connection connection) throws InterruptedException {
        final DbFuture<ResultSet> result = connection.executeQuery("SELECT * FROM nonExistingTable");
        result.addListener(new DbListener<ResultSet>() {
            @Override
            public void onCompletion(DbFuture<ResultSet> future) {
                switch (future.getState()) {
                    case SUCCESS:
                        throw new Error("This should be unreachable code, since the query is invalid");
                    case FAILURE:
                        future.getException().printStackTrace();
                        break;
                    case CANCELLED:
                        System.out.println("Cancelled");
                        break;
                }
            }
        });
    }
}

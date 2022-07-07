package ind.liuer.rabbitmq.support;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * @author Mingの
 */
public class RabbitMqUtil {

    public static final Logger log = LoggerFactory.getLogger(RabbitMqUtil.class);

    private static final Properties PROPERTIES = new Properties();
    private static final String HOST;
    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        InputStream resourceAsStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("rabbitmq-db.properties");
        try {
            PROPERTIES.load(resourceAsStream);
            HOST = PROPERTIES.getProperty("rabbitmq.host");
            USERNAME = PROPERTIES.getProperty("rabbitmq.username");
            PASSWORD = PROPERTIES.getProperty("rabbitmq.password");
        } catch (IOException e) {
            log.error("Initialize RabbitMQ connection information failed. Cause: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Optional<Connection> getConnection() {
        Optional<Connection> connOpt = Optional.empty();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        try {
            connOpt = Optional.ofNullable(factory.newConnection());
        } catch (IOException | TimeoutException e) {
            log.error("Connect to RabbitMQ failed. Cause: {}", e.getMessage(), e);
        }
        return connOpt;
    }

    public static Optional<Channel> getChannel() {
        Optional<Channel> channelOpt = Optional.empty();
        try {
            Optional<Connection> connOpt = getConnection();
            if (connOpt.isPresent()) {
                Connection connection = connOpt.get();
                channelOpt = Optional.ofNullable(connection.createChannel());
            }
        } catch (IOException e) {
            log.error("Create a channel failed. Cause: {}", e.getMessage(), e);
        }
        return channelOpt;
    }

    public static void close(Channel channel) {
        if (channel != null) {
            Connection connection = channel.getConnection();
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                // do nothing
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}

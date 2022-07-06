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
public class RabbitMQUtil {

    public static final Logger log = LoggerFactory.getLogger(RabbitMQUtil.class);

    private static final Properties properties = new Properties();
    private static final String host;
    private static final String username;
    private static final String password;

    static {
        InputStream resourceAsStream = Thread.currentThread()
            .getContextClassLoader().getResourceAsStream("rabbitmq-db.properties");
        try {
            properties.load(resourceAsStream);
            host = properties.getProperty("rabbitmq.host");
            username = properties.getProperty("rabbitmq.username");
            password = properties.getProperty("rabbitmq.password");
        } catch (IOException e) {
            log.error("Initialize RabbitMQ connection information failed. Cause: {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Optional<Connection> getConnection() {
        Optional<Connection> connOpt = Optional.empty();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
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

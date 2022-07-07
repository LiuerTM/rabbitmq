package ind.liuer.rabbitmq.base.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Mingの
 */
public class ReceiveLog {

    public static final Logger log = LoggerFactory.getLogger(ReceiveLog.class);

    public static final String EXCHANGE_NAME = "base.log";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            // Declare a temporary queue
            String queueName = channel.queueDeclare().getQueue();
            // Bind a queue to an exchange with routing key ""
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            log.info("Waiting for log.....");

            channel.basicConsume(
                    queueName,
                    true,
                    (consumerTag, message) -> {
                        String logMsg = new String(message.getBody(), StandardCharsets.UTF_8);
                        log.info("Received a log: {}", logMsg);
                    },
                    consumerTag -> {
                    }
            );
        }
    }
}

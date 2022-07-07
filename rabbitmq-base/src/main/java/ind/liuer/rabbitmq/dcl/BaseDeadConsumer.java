package ind.liuer.rabbitmq.dcl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Mingの
 */
public class BaseDeadConsumer {

    public static final Logger log = LoggerFactory.getLogger(BaseDeadConsumer.class);

    public static final String DEAD_QUEUE = "base.dead";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            log.info("Waiting for message.....");

            DeliverCallback deliverCallback = (consumerTag, message) -> {
                String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                log.info("Received a message: {}", msg);

                // Had to ack
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(DEAD_QUEUE, deliverCallback, consumerTag -> {
            });
        }
    }
}

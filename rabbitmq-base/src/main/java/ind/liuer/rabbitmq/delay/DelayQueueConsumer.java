package ind.liuer.rabbitmq.delay;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Mingの
 */
public class DelayQueueConsumer {

    public static final Logger log = LoggerFactory.getLogger(DelayQueueConsumer.class);

    public static final String DELAY_QUEUE = "delay.queue";

    public static final String DEAD_EXCHANGE = "base.dead_exchange";
    public static final String DEAD_QUEUE = "base.dead";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            Map<String, Object> arguments = new HashMap<>(16);
            arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            arguments.put("x-dead-letter-routing-key", DEAD_QUEUE);
            arguments.put("x-message-ttl", 10000);
            channel.queueDeclare(DELAY_QUEUE, false, false, false, arguments);

            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_QUEUE);

            log.info("Waiting for message....");

            channel.basicConsume(
                    DELAY_QUEUE,
                    true,
                    (consumerTag, message) -> {
                        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                        log.info("Received a message: {}", msg);
                    },
                    consumerTag -> {
                    }
            );
        }
    }
}

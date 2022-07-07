package ind.liuer.rabbitmq.dcl;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
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
public class MessageTtlConsumer {

    public static final Logger log = LoggerFactory.getLogger(MessageTtlConsumer.class);

    public static final String NORMAL_EXCHANGE = "base.ttl_normal_exchange";
    public static final String NORMAL_QUEUE = "base.ttl.normal";

    public static final String DEAD_EXCHANGE = "base.dead_exchange";
    public static final String DEAD_QUEUE = "base.dead";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

            Map<String, Object> arguments = new HashMap<>(16);
            arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            arguments.put("x-dead-letter-routing-key", DEAD_QUEUE);
            channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_QUEUE);
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_QUEUE);

            log.info("Waiting for message.....");

            DeliverCallback delverCallback = (consumerTag, message) -> {
                String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                log.info("Received a message: {}", msg);
            };
            channel.basicConsume(NORMAL_QUEUE, true, delverCallback, consumerTag -> {
            });
        }
    }
}

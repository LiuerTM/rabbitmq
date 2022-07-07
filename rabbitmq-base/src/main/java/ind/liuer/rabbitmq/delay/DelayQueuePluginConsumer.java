package ind.liuer.rabbitmq.delay;

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
public class DelayQueuePluginConsumer {

    public static final Logger log = LoggerFactory.getLogger(DelayQueuePluginConsumer.class);

    public static final String DELAY_PLUGIN_EXCHANGE = "delay.plugin.exchange";

    public static final String DELAY_PLUGIN_QUEUE = "delay.plugin.queue";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            Map<String, Object> arguments = new HashMap<>(16);
            arguments.put("x-delayed-type", "direct");
            channel.exchangeDeclare(DELAY_PLUGIN_EXCHANGE, "x-delayed-message", false, false, arguments);
            channel.queueDeclare(DELAY_PLUGIN_QUEUE, false, false, false, null);
            channel.queueBind(DELAY_PLUGIN_QUEUE, DELAY_PLUGIN_EXCHANGE, DELAY_PLUGIN_QUEUE);

            log.info("Waiting for message.....");

            channel.basicConsume(
                    DELAY_PLUGIN_QUEUE,
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

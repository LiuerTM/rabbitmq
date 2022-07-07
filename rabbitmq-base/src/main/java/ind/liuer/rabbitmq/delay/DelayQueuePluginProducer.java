package ind.liuer.rabbitmq.delay;

import com.rabbitmq.client.AMQP;
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
public class DelayQueuePluginProducer {

    public static final Logger log = LoggerFactory.getLogger(DelayQueuePluginProducer.class);

    public static final String DELAY_PLUGIN_EXCHANGE = "delay.plugin.exchange";

    public static final String DELAY_PLUGIN_QUEUE = "delay.plugin.queue";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            int messageCount = 10;
            for (int i = 0; i < messageCount; i++) {
                long delay = (long) (Math.random() * 10 + 1) * 1000;
                Map<String, Object> arguments = new HashMap<>(16);
                arguments.put("x-delay", delay);
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(arguments).build();
                channel.basicPublish(
                        DELAY_PLUGIN_EXCHANGE,
                        DELAY_PLUGIN_QUEUE,
                        properties,
                        ("Delay Message - " + i + " delay: " + delay).getBytes(StandardCharsets.UTF_8)
                );

                log.info("Sent a message successfully");
            }

            RabbitMqUtil.close(channel);
        }
    }
}

package ind.liuer.rabbitmq.base.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Mingの
 */
public class EmitZooTopic {

    public static final Logger log = LoggerFactory.getLogger(EmitZooTopic.class);

    public static final String EXCHANGE_NAME = "base.topic_zoo";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Declare a topic form exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String[] strings = new String[]{"lazy.monkey", "lazy.black.panda", "cute.white.rabbit", "ugly.orange.bear"};
            for (String str : strings) {
                channel.basicPublish(EXCHANGE_NAME, str, null, str.getBytes(StandardCharsets.UTF_8));

                log.info("Sent a message successfully");
            }

            RabbitMQUtil.close(channel);
        }
    }
}

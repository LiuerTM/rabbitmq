package ind.liuer.rabbitmq.dcl;

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
public class MessageRefuseProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageRefuseProducer.class);

    public static final String NORMAL_EXCHANGE = "base.refuse_normal_exchange";
    public static final String NORMAL_QUEUE = "base.refuse.normal";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            int messageCount = 10;
            for (int i = 0; i < messageCount; i++) {
                String message = "info - " + i;
                channel.basicPublish(NORMAL_EXCHANGE, NORMAL_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
                log.info("Sent a message successfully");
            }

            RabbitMqUtil.close(channel);
        }
    }
}

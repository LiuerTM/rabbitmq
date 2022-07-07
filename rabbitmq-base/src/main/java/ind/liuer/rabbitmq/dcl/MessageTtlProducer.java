package ind.liuer.rabbitmq.dcl;

import com.rabbitmq.client.AMQP;
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
public class MessageTtlProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageTtlProducer.class);

    public static final String NORMAL_EXCHANGE = "base.ttl_normal_exchange";
    public static final String NORMAL_QUEUE = "base.ttl.normal";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
            for (int i = 0; i < 10; i++) {
                String message = "info - " + i;
                channel.basicPublish(NORMAL_EXCHANGE, NORMAL_QUEUE, properties, message.getBytes(StandardCharsets.UTF_8));
                log.info("Sent a message successfully");
            }

            RabbitMQUtil.close(channel);
        }
    }
}

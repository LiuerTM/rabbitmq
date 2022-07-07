package ind.liuer.rabbitmq.base.pubconfirm;

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
public class PubConfirmConsumer {

    public static final Logger log = LoggerFactory.getLogger(PubConfirmConsumer.class);

    public static final String QUEUE_NAME = "base.confirm";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            log.info("Waiting for message.....");
            channel.basicConsume(
                    QUEUE_NAME,
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

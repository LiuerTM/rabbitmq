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
public class ReceiveZooTopic {

    public static final Logger log = LoggerFactory.getLogger(ReceiveZooTopic.class);

    public static final String EXCHANGE_NAME = "base.topic_zoo";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String queueName = channel.queueDeclare().getQueue();

//            String[] strings = new String[]{"*.orange.*"};
//            String[] strings = new String[]{"*.*.rabbit", "lazy.#"};
            String[] strings = new String[]{"#"};
            for (String string : strings) {
                channel.queueBind(queueName, EXCHANGE_NAME, string);
            }

            log.info("Waiting for message.....");

            channel.basicConsume(
                queueName,
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

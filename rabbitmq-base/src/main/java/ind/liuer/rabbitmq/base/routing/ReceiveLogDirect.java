package ind.liuer.rabbitmq.base.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Mingの
 */
public class ReceiveLogDirect {

    public static final Logger log = LoggerFactory.getLogger(ReceiveLogDirect.class);

    public static final String EXCHANGE_NAME = "base.direct_log";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String queueName = channel.queueDeclare().getQueue();

            String[] strings = new String[]{"error"};
//            String[] strings = new String[]{"info", "warning", "error"};
            for (String str : strings) {
                channel.queueBind(queueName, EXCHANGE_NAME, str);
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

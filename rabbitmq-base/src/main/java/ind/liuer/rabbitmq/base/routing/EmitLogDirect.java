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
public class EmitLogDirect {

    public static final Logger log = LoggerFactory.getLogger(EmitLogDirect.class);

    public static final String EXCHANGE_NAME = "base.direct_log";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Declare a direct form exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            String[] strings = new String[]{"info", "info:log", "warning", "warning:log", "error", "error:log"};
            List<String> list = Arrays.asList(strings);
            for (int i = 0; i < list.size(); i = i + 2) {
                channel.basicPublish(
                    EXCHANGE_NAME,
                    list.get(i),
                    null,
                    (list.get(i + 1)).getBytes(StandardCharsets.UTF_8)
                );

                log.info("Sent a message successfully");
            }

            RabbitMQUtil.close(channel);
        }
    }
}

package ind.liuer.rabbitmq.base.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class EmitLog {

    public static final Logger log = LoggerFactory.getLogger(EmitLog.class);

    public static final String EXCHANGE_NAME = "base.log";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Declare a fanout form exchange
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            int messageCount = 5;
            for (int i = 0; i < messageCount; i++) {
                channel.basicPublish(
                        EXCHANGE_NAME,
                        "",
                        null,
                        ("info: log - " + i).getBytes(StandardCharsets.UTF_8)
                );

                log.info("Sent a log successfully");
            }

            RabbitMqUtil.close(channel);
        }
    }

}

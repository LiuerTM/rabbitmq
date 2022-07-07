package ind.liuer.rabbitmq.base.helloworld;

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
public class Receive {

    public static final Logger log = LoggerFactory.getLogger(Receive.class);

    public static final String QUEUE_NAME = "base.hello";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            log.info("Waiting for message.....");

            /*
             * receive a message
             * - queue name       队列名称
             * - autoAck          是否自动签收
             * - deliverCallback  处理回调
             * - cancelCallback   取消回调
             */
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

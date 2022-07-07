package ind.liuer.rabbitmq.base.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Mingの
 */
public class Leader {

    public static final Logger log = LoggerFactory.getLogger(Leader.class);

    public static final String TASK_NAME = "base.task";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Declare a durable queue named base.task
            channel.queueDeclare(TASK_NAME, true, false, false, null);

            // Publish a persistent message
            int messageCount = 10;
            for (int i = 0; i < messageCount; i++) {
                channel.basicPublish(
                        "",
                        TASK_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        ("Task" + " - " + i).getBytes(StandardCharsets.UTF_8)
                );

                log.info("Sent a task successfully");
            }

            RabbitMqUtil.close(channel);
        }
    }
}

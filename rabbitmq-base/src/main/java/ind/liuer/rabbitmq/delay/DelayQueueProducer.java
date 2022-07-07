package ind.liuer.rabbitmq.delay;

import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import ind.liuer.rabbitmq.support.SleepUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author Mingの
 */
public class DelayQueueProducer {

    public static final Logger log = LoggerFactory.getLogger(DelayQueueProducer.class);

    public static final String DELAY_QUEUE = "delay.queue";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            int messageCount = 10;
            for (int i = 0; i < messageCount; i++) {
                SleepUtil.secondSleep((long) ((Math.random() * 10) + 1));
                channel.basicPublish("", DELAY_QUEUE, null, ("Delay Message - " + i).getBytes(StandardCharsets.UTF_8));
                log.info("Sent a message successfully");
            }

            RabbitMqUtil.close(channel);
        }
    }
}

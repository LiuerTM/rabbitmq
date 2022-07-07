package ind.liuer.rabbitmq.base.pubconfirm;

import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMqUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * @author Mingの
 */
public class PubConfirmSingle {

    public static final Logger log = LoggerFactory.getLogger(PubConfirmSingle.class);

    public static final String QUEUE_NAME = "base.confirm";

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Enabling Publisher Confirms on a Channel
            channel.confirmSelect();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            long start = System.currentTimeMillis();
            int messageCount = 5000;
            for (int i = 0; i < messageCount; i++) {
                channel.basicPublish("", QUEUE_NAME, null, ("Confirm Message - " + i).getBytes(StandardCharsets.UTF_8));

                // Confirm, uses a five seconds timeout
                channel.waitForConfirmsOrDie(5000);
            }
            long stop = System.currentTimeMillis();

            log.info("Published 5000 messages individually in {} ms", (stop - start));
            log.info("Sent 5000 messages successfully");

            RabbitMqUtil.close(channel);
        }
    }
}

package ind.liuer.rabbitmq.base.workqueue;

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
public class Worker {

    public static final Logger log = LoggerFactory.getLogger(Worker.class);

    public static final String QUEUE_NAME = "base.task";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Fair dispatch
            int prefetchCount = 2;
            channel.basicQos(prefetchCount);

            log.info("Waiting for task.....");

            // Message not acknowledgment
            channel.basicConsume(
                    QUEUE_NAME,
                    false,
                    (consumerTag, message) -> {
                        String task = new String(message.getBody(), StandardCharsets.UTF_8);
                        log.info("Received a task: {}", task);
                        try {
                            doWork(task);
                        } finally {
                            log.info("Done");
                            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                        }
                    },
                    consumerTag -> {
                    }
            );
        }
    }

    private static void doWork(String task) {
        int len = task.length();
        String numStr = task.substring(len - 1, len);
        int num = Integer.parseInt(numStr);
        int modNum = 2;
        if (num > 0 && num % modNum == 0) {
            SleepUtil.secondSleep(4L);
        }
    }
}

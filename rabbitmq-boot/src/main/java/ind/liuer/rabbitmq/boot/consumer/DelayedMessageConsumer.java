package ind.liuer.rabbitmq.boot.consumer;

import ind.liuer.rabbitmq.boot.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author Mingの
 */
@Component
public class DelayedMessageConsumer {

    public static final Logger log = LoggerFactory.getLogger(DelayedMessageConsumer.class);

    @RabbitListener(queues = RabbitMqConstant.DELAYED_QUEUE_NAME)
    public void receiveDelayedMessage(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Received a message: {}", msg);
    }
}

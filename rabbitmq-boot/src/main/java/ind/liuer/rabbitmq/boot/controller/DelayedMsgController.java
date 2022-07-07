package ind.liuer.rabbitmq.boot.controller;

import ind.liuer.rabbitmq.boot.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mingの
 */
@RestController
@RequestMapping("/msg/delayed")
public class DelayedMsgController {

    public static final Logger log = LoggerFactory.getLogger(DelayedMsgController.class);

    private final RabbitTemplate rabbitTemplate;

    public DelayedMsgController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/send/{message}/{delayedTime}")
    public String sendDelayedMsg(@PathVariable String message, @PathVariable Integer delayedTime) {
        rabbitTemplate.convertAndSend(
                RabbitMqConstant.DELAYED_EXCHANGE_NAME,
                RabbitMqConstant.DELAYED_ROUTING_KEY,
                message,
                msg -> {
                    msg.getMessageProperties().setDelay(delayedTime);
                    return msg;
                }
        );
        log.info("Sent a message successfully");
        return "Sent a message[msg: '" + message + "', delayed: " + delayedTime + "ms] successfully";
    }
}

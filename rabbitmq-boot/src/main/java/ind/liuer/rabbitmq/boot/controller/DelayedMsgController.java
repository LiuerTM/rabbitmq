package ind.liuer.rabbitmq.boot.controller;

import ind.liuer.rabbitmq.boot.constant.RabbitConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mingの
 */
@RestController("/msg")
public class DelayedMsgController {

    private final RabbitTemplate rabbitTemplate;

    public DelayedMsgController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/send/delayed/{message}/{delayedTime}")
    public String sendDelayedMsg(@PathVariable String message, @PathVariable Integer delayedTime) {
        rabbitTemplate.convertAndSend(
            RabbitConstant.DELAYED_EXCHANGE_NAME,
            RabbitConstant.DELAYED_ROUTING_KEY,
            message,
            msg -> {
                msg.getMessageProperties().setDelay(delayedTime);
                return msg;
            }
        );
        return "Sent a message[msg: '" + message + "', delayed: " + delayedTime + "ms] successfully";
    }
}

package ind.liuer.rabbitmq.boot.config;

import ind.liuer.rabbitmq.boot.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mingの
 */
@Configuration
public class RabbitConfig {

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");

        return new CustomExchange(
            RabbitConstant.DELAYED_EXCHANGE_NAME,
            RabbitConstant.DELAYED_EXCHANGE_TYPE,
            false,
            false,
            arguments);
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue(
            RabbitConstant.DELAYED_QUEUE_NAME,
            false,
            false,
            false,
            null);
    }

    @Bean
    public Binding delayedQueueBindingDelayedExchange(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder
            .bind(delayedQueue)
            .to(delayedExchange)
            .with(RabbitConstant.DELAYED_ROUTING_KEY)
            .noargs();
    }

}

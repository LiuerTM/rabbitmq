package ind.liuer.rabbitmq.boot.config;

import ind.liuer.rabbitmq.boot.constant.RabbitMqConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Mingの
 */
@Configuration
public class RabbitMqConfig implements EnvironmentAware {

    public static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>(16);
        arguments.put("x-delayed-type", "direct");

        return new CustomExchange(
                RabbitMqConstant.DELAYED_EXCHANGE_NAME,
                RabbitMqConstant.DELAYED_EXCHANGE_TYPE,
                false,
                false,
                arguments);
    }

    @Bean
    public Queue delayedQueue() {
        return new Queue(
                RabbitMqConstant.DELAYED_QUEUE_NAME,
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
                .with(RabbitMqConstant.DELAYED_ROUTING_KEY)
                .noargs();
    }

    @Override
    public void setEnvironment(Environment environment) {
        try {
            MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
            InputStream inputStream = new ClassPathResource("rabbitmq-db.properties").getInputStream();
            Properties properties = new Properties();
            properties.load(inputStream);
            PropertiesPropertySource propertySource = new PropertiesPropertySource("rabbitmq-config", properties);
            sources.addLast(propertySource);
        } catch (IOException e) {
            log.error("Loaded RabbitMQ client properties failed");
            e.printStackTrace();
        }
    }
}

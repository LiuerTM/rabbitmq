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
public class Send {

    public static final Logger log = LoggerFactory.getLogger(Send.class);

    public static final String QUEUE_NAME = "base.hello";

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMqUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            /*
             * Declare a queue
             * - queue name  队列名称
             * - durable     队列是否持久化
             * - exclusive   队列是否共享
             * - autoDelete  队列是否自动删除
             * - other args  其他参数
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            String message = "Hello World";

            /*
             * Publish a message
             * - exchange    交换机，默认是：""(空串，AMQP default)、direct、D(durable)
             * - queue name  队列名称
             * - props       其他属性
             * - the binary form of the message 二进制形式的消息
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));

            log.info("Sent a message successfully");

            RabbitMqUtil.close(channel);
        }
    }
}

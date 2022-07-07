package ind.liuer.rabbitmq.base.pubconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import ind.liuer.rabbitmq.support.RabbitMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Mingの
 */
public class PubConfirmAsync {

    public static final Logger log = LoggerFactory.getLogger(PubConfirmAsync.class);

    public static final String QUEUE_NAME = "base.confirm";

    private static final ConcurrentNavigableMap<Long, String> requireConfirm = new ConcurrentSkipListMap<>();

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();
            channel.confirmSelect();

            ConfirmCallback cleanConfirmed = (deliveryTag, multiple) -> {
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = requireConfirm.headMap(deliveryTag, true);
                    confirmed.clear();
                } else {
                    requireConfirm.remove(deliveryTag);
                }
            };
            ConfirmCallback handleNack = (deliveryTag, multiple) -> {
                String msg = requireConfirm.get(deliveryTag);
                log.error("Message with body {} has been nack-ed. Sequence number: {}, multiple: {}", msg, deliveryTag, msg);
                cleanConfirmed.handle(deliveryTag, multiple);
            };
            channel.addConfirmListener(cleanConfirmed, handleNack);

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            long start = System.currentTimeMillis();
            for (int i = 0; i < 50000; i++) {
                long sequenceNumber = channel.getNextPublishSeqNo();
                String msg = "Confirm Message - " + sequenceNumber;
                requireConfirm.put(sequenceNumber, msg);
                channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));
            }
            long stop = System.currentTimeMillis();

            log.info("Published 50000 messages individually in {} ms", (stop - start));
            log.info("Sent 50000 messages successfully");

            RabbitMQUtil.close(channel);
        }
    }
}

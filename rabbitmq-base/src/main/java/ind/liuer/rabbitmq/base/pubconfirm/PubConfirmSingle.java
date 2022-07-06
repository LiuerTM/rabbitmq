package ind.liuer.rabbitmq.base.pubconfirm;

import com.rabbitmq.client.Channel;
import ind.liuer.rabbitmq.support.RabbitMQUtil;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Mingの
 */
public class PubConfirmSingle {

    public static void main(String[] args) throws IOException {
        Optional<Channel> channelOpt = RabbitMQUtil.getChannel();
        if (channelOpt.isPresent()) {
            Channel channel = channelOpt.get();

            // Enabling Publisher Confirms on a Channel
            channel.confirmSelect();


        }
    }
}

package ind.liuer.rabbitmq.support;

import java.util.concurrent.TimeUnit;

/**
 * @author Mingの
 */
public class SleepUtil {

    public static void secondSleep(long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    public static void milliSleep(long milli) {
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            // do nothing
        }
    }
}

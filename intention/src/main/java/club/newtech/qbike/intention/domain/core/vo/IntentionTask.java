package club.newtech.qbike.intention.domain.core.vo;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@ToString
/**
 * 用延时队列来实现定时的轮询任务，如果找不到匹配intention的司机，将重新压回队列
 * 分布式下可以使用redis来实现延时任务队列
 */
public class IntentionTask implements Delayed {
    /**
     * Base of millisecond timings, to avoid wrapping
     */
    private static final long ORIGIN = System.currentTimeMillis();
    private final long time;
    private int intenionId;

    public IntentionTask(int intenionId, long time) {
        this.intenionId = intenionId;

        this.time = time;
    }

    final static long now() {
        return System.currentTimeMillis() - ORIGIN;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(time - now(), TimeUnit.MILLISECONDS);
        return d;
    }

    @Override
    public int compareTo(Delayed other) {
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof IntentionTask) {
            IntentionTask x = (IntentionTask) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
        }
        long d = (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}


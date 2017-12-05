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
     * Base of nanosecond timings, to avoid wrapping
     */
    private final long NANO_ORIGIN = System.nanoTime();
    private final long executionTime;
    private int intenionId;
    private int repeatTimes;

    public IntentionTask(int intenionId, long time, TimeUnit unit, int repeatTimes) {
        this.intenionId = intenionId;
        this.repeatTimes = repeatTimes;
        this.executionTime = TimeUnit.NANOSECONDS.convert(time, unit);
    }


    final long now() {
        return System.nanoTime() - NANO_ORIGIN;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(executionTime - now(), TimeUnit.NANOSECONDS);
        return d;
    }

    @Override
    public int compareTo(Delayed other) {
        if (other == this) // compare zero ONLY if same object
            return 0;
        if (other instanceof IntentionTask) {
            IntentionTask x = (IntentionTask) other;
            long diff = executionTime - x.executionTime;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
}


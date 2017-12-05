package club.newtech.qbike.intention.domain.core.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * A value object representing a named lock, with a globally unique value and an expiry.
 * @author Joe
 *
 */
@Data
@ToString
public class Lock implements Comparable<Lock> {
    /**
     * The customerName of the lock.
     */
    private final String name;
    /**
     * The value of the lock (globally unique, or at least different for locks with the
     * same customerName and different expiry).
     */
    private final String value;
    /**
     * The expiry of the lock expressed as a point in time.
     */
    private final Date expires;

    public boolean isExpired() {
        return expires.before(new Date());
    }

    @Override
    public int compareTo(Lock other) {
        return expires.compareTo(other.expires);
    }
}

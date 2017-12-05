package club.newtech.qbike.intention.domain.service;


import club.newtech.qbike.intention.domain.core.vo.Lock;
import club.newtech.qbike.intention.domain.exception.LockExistsException;
import club.newtech.qbike.intention.domain.exception.LockNotHeldException;

import java.util.Set;

public interface LockService {
    /**
     * Iterate the existing locks.
     *
     * @return an iterable of all locks
     */
    Iterable<Lock> findAll();

    /**
     * Acquire a lock by customerName. Only one process (globally) should be able to obtain and
     * hold the lock with this customerName at any given time. Locks expire and can also be
     * released by the owner, so after either of those events the lock can be acquired by
     * the same or a different process.
     *
     * @param name the customerName identifying the lock
     * @return a Lock containing a value that can be used to release or refresh the lock
     * @throws LockExistsException
     */
    Lock create(String name) throws LockExistsException;

    Set<Lock> createMultiLock(Set<String> names) throws LockExistsException;

    /**
     * Release a lock before it expires. Only the holder of a lock can release it, and the
     * holder must have the correct unique value to prove that he holds it.
     *
     * @param name  the customerName of the lock
     * @param value the value of the lock (which has to match the value when it was
     *              acquired)
     * @return true if successful
     * @throws LockNotHeldException
     */
    boolean release(String name, String value) throws LockNotHeldException;


    /**
     * The holder of a lock can refresh it, extending its expiry. If the caller does not
     * hold the lock there will be an exception, but the implementation may not be able to
     * tell if it was because he formerly held the lock and it expired, or if it simply
     * was never held.
     *
     * @param name  the customerName of the lock
     * @param value the value of the lock (which has to match the value when it was
     *              acquired)
     * @return a new lock with a new value and a new expiry
     * @throws LockNotHeldException if the value does not match the current value or if
     *                              the lock doesn't exist (e.g. if it expired)
     */
    Lock refresh(String name, String value) throws LockNotHeldException;
}

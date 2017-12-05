package club.newtech.qbike.intention.domain.service;


import club.newtech.qbike.intention.domain.core.vo.Lock;
import club.newtech.qbike.intention.domain.exception.LockExistsException;
import club.newtech.qbike.intention.domain.exception.LockNotHeldException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toMap;

@Service
public class RedisLockService implements LockService {
    private static final String DEFAULT_LOCK_PREFIX = "qbike.lock.";
    @Autowired
    StringRedisTemplate redisOperations;
    private String prefix = DEFAULT_LOCK_PREFIX;
    @Setter
    private long expiry = 30000; // 30 seconds

    /**
     * The prefix for all lock keys.
     *
     * @param prefix the prefix to set for all lock keys
     */
    public void setPrefix(String prefix) {
        if (!prefix.endsWith(".")) {
            prefix = prefix + ".";
        }
        this.prefix = prefix;
    }

    @Override
    public Iterable<Lock> findAll() {
        Set<String> keys = redisOperations.keys(prefix + "*");
        Set<Lock> locks = new LinkedHashSet<Lock>();
        for (String key : keys) {
            Date expires = new Date(System.currentTimeMillis() + redisOperations.getExpire(key, TimeUnit.MILLISECONDS));
            locks.add(new Lock(nameForKey(key), redisOperations.opsForValue().get(key), expires));
        }
        return locks;
    }

    @Override
    public Lock create(String name) {
        String stored = getValue(name);
        if (stored != null) {
            throw new LockExistsException();
        }
        String value = UUID.randomUUID().toString();
        String key = keyForName(name);
        if (!redisOperations.opsForValue().setIfAbsent(key, value)) {
            throw new LockExistsException();
        }
        redisOperations.expire(key, expiry, TimeUnit.MILLISECONDS);
        Date expires = new Date(System.currentTimeMillis() + expiry);
        return new Lock(name, value, expires);
    }

    @Override
    public Set<Lock> createMultiLock(Set<String> names) {
        Set<Lock> locks = redisOperations.execute(new SessionCallback<Set<Lock>>() {
            @Override
            public Set<Lock> execute(RedisOperations operations) throws DataAccessException {
                Set<Lock> locks = new HashSet<>();
                Map<String, String> mset =
                        names.stream()
                                .collect(toMap(name -> keyForName(name), key -> UUID.randomUUID().toString()));
                //一次性获取所有的锁，如果失败直接退出，说明已经有被占用了
                boolean b = operations.opsForValue()
                        .multiSetIfAbsent(mset);
                //占领锁成功了，再给锁添加过期时间 TODO 这里有可能失败，如果用Watch也许会更有效果一些
                if (b) {
                    operations.multi();
                    for (String name : names) {
                        String key = keyForName(name);
                        operations.expire(key, expiry, TimeUnit.MILLISECONDS);
                        Date expires = new Date(System.currentTimeMillis() + expiry);
                        Lock lock = new Lock(name, mset.get(key), expires);
                        locks.add(lock);
                    }
                    operations.exec();
                    return locks;
                } else {
                    throw new LockExistsException();
                }
            }

        });
        return locks;
    }

    @Override
    public boolean release(String name, String value) {
        String stored = getValue(name);
        if (stored != null && value.equals(stored)) {
            String key = keyForName(name);
            redisOperations.delete(key);
            return true;
        }
        if (stored != null) {
            throw new LockNotHeldException();
        }
        return false;
    }

    @Override
    public Lock refresh(String name, String value) {
        String key = keyForName(name);
        String stored = getValue(name);
        if (stored != null && value.equals(stored)) {
            Date expires = new Date(System.currentTimeMillis() + expiry);
            redisOperations.expire(key, expiry, TimeUnit.MILLISECONDS);
            return new Lock(name, value, expires);
        }
        throw new LockNotHeldException();
    }

    private String getValue(String name) {
        String key = keyForName(name);
        String stored = redisOperations.opsForValue().get(key);
        return stored;
    }

    private String nameForKey(String key) {
        if (!key.startsWith(prefix)) {
            throw new IllegalStateException("Key (" + key + ") does not start with prefix (" + prefix + ")");
        }
        return key.substring(prefix.length());
    }

    private String keyForName(String name) {
        return prefix + name;
    }
}

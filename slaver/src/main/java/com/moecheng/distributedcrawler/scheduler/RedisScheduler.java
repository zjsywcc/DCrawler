package com.moecheng.distributedcrawler.scheduler;


import com.alibaba.fastjson.JSON;
import com.moecheng.distributedcrawler.Task;
import com.moecheng.distributedcrawler.model.URLRequest;
import com.moecheng.distributedcrawler.scheduler.component.DuplicateRemover;
import com.moecheng.distributedcrawler.scheduler.component.MonitorableScheduler;
import org.apache.commons.codec.digest.DigestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Use Redis as url scheduler for distributed crawlers.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class RedisScheduler implements MonitorableScheduler, DuplicateRemover {

    protected JedisPool pool;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    public RedisScheduler(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }

    public RedisScheduler(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        Jedis jedis = pool.getResource();
        try {
            jedis.del(getSetKey(task));
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public boolean isDuplicate(URLRequest request, Task task) {
        Jedis jedis = pool.getResource();
        try {
            boolean isDuplicate = jedis.sismember(getSetKey(task), request.getUrl());
            if (!isDuplicate) {
                jedis.sadd(getSetKey(task), request.getUrl());
            }
            return isDuplicate;
        } finally {
            pool.returnResource(jedis);
        }

    }

    @Override
    public void push(URLRequest request, Task task) {
        if (!isDuplicate(request, task)) {
            Jedis jedis = pool.getResource();
            try {
                jedis.rpush(getQueueKey(task), request.getUrl());
                System.out.println(String.format("push: QueueKey: %s Url: %s", getQueueKey(task), request.getUrl()));
                if (request.getExtras() != null) {
                    String field = DigestUtils.shaHex(request.getUrl());
                    String value = JSON.toJSONString(request);
                    jedis.hset((ITEM_PREFIX + task.getUUID()), field, value);
                }
            } finally {
                pool.returnResource(jedis);
            }
        }
    }

    @Override
    public synchronized URLRequest poll(Task task) {
        Jedis jedis = pool.getResource();
        try {
            String url = jedis.lpop(getQueueKey(task));
            if (url == null) {
                return null;
            }
            String key = ITEM_PREFIX + task.getUUID();
            String field = DigestUtils.shaHex(url);
            byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            if (bytes != null) {
                URLRequest o = JSON.parseObject(new String(bytes), URLRequest.class);
                return o;
            }
            URLRequest request = new URLRequest(url);
            return request;
        } finally {
            pool.returnResource(jedis);
        }
    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getUUID();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    protected String getItemKey(Task task)
    {
        return ITEM_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        Jedis jedis = pool.getResource();
        try {
            Long size = jedis.llen(getQueueKey(task));
            return size.intValue();
        } finally {
            pool.returnResource(jedis);
        }
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        Jedis jedis = pool.getResource();
        try {
            Long size = jedis.scard(getSetKey(task));
            return size.intValue();
        } finally {
            pool.returnResource(jedis);
        }
    }
}

package de.polipol.analytics.cache;

import static de.polipol.analytics.commons.Constants.LOCALHOST;

import java.time.Duration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
//import redis.clients.jedis.exceptions.JedisException;

public final class RedisCache implements Cache {

//	private static Jedis jedis;
//	protected String host;
//	protected int port;

	final JedisPoolConfig poolConfig = buildPoolConfig();
	protected JedisPool jedisPool; // = new JedisPool(poolConfig, "localhost");

	public RedisCache() {
//		this.host = EMPTY;
//		this.port = 0;
		this.jedisPool = new JedisPool(poolConfig, LOCALHOST);
		buildPoolConfig();
	}

	public RedisCache(int port) {
//		this.port = port;
		this.jedisPool = new JedisPool(poolConfig, LOCALHOST, port);
		buildPoolConfig();
	}

	public RedisCache(String host, int port) {
//		this.host = host;
//		this.port = port;
		this.jedisPool = new JedisPool(poolConfig, host, port);
		buildPoolConfig();
	}

	private JedisPoolConfig buildPoolConfig() {
		final JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(128);
		poolConfig.setMaxIdle(128);
		poolConfig.setMinIdle(16);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
		poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
		poolConfig.setNumTestsPerEvictionRun(3);
		poolConfig.setBlockWhenExhausted(true);
//	    redisTemplate = new RedisTemplate<String, Object>();
//	    redisTemplate.setConnectionFactory(jedisConnectionFactory);
//	    redisTemplate.setEnableTransactionSupport(true);
//	    StringRedisSerializer serializer = new StringRedisSerializer();
//	    redisTemplate.setKeySerializer(serializer);
//	    redisTemplate.setValueSerializer(serializer);
//	    redisTemplate.setHashKeySerializer(serializer);
//	    redisTemplate.setHashValueSerializer(serializer);
//	    redisTemplate.afterPropertiesSet();
//	    redisTemplate.setEnableTransactionSupport(true);
		return poolConfig;
	}

//	private Jedis createInstance() {
//    	if (host.equals(EMPTY) & port == 0) {
//    		return new Jedis();
//    	} else if (host.equals(EMPTY) & port > 0) {
//    		return new Jedis(LOCALHOST, port);
//    	} 
//    	return new Jedis(host, port);
//	}

//	public Jedis getInstance() {
//	    try{
//	    	if (RedisCache.jedis == null) {
//	    		throw new JedisException(EMPTY);
//			}
//	    	RedisCache.jedis.ping();
//	    } catch (JedisException exception) {
//	    	logger.info("Try to connect REDIS...");
//	    	RedisCache.jedis = this.createInstance();
//	    	RedisCache.jedis.connect();
//	    	logger.info("Connection to REDIS established.");
//	    }
//	    return RedisCache.jedis;
//	}

	@Override
	public boolean exists(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.exists(key);
		}
	}

	@Override
	public void flushAll() {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.flushAll();
		}
	}

	@Override
	public byte[] get(byte[] key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.get(key);
		}
	}

	@Override
	public String get(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			return jedis.get(key);
		}
	}

//	@Override
//	public boolean isConnected() {
//		return getInstance().isConnected();
//	}

	@Override
	public void remove(String key) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.del(key);
		}
	}

	@Override
	public void sadd(String key, String member) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.sadd(key, member);
		}
	}

	@Override
	public void set(String key, byte[] value) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(key.getBytes(), value);
		}
	}

	@Override
	public void set(String key, String value) {
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.set(key, value);
		}
	}
}

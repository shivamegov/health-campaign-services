package org.egov.common.data.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.data.query.builder.SelectQueryBuilder;
import org.egov.common.data.query.exception.QueryBuilderException;
import org.egov.common.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.egov.common.utils.CommonUtils.getMethod;
import static org.egov.common.utils.CommonUtils.getObjClass;

@Slf4j
public abstract class GenericRepository<T> {

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected final Producer producer;

    protected final RedisTemplate<String, Object> redisTemplate;

    protected final SelectQueryBuilder selectQueryBuilder;

    protected final RowMapper<T> rowMapper;

    protected String hashKey;

    @Value("${spring.cache.redis.time-to-live:60}")
    private String timeToLive;

    protected GenericRepository(Producer producer, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                RedisTemplate<String, Object> redisTemplate,
                                SelectQueryBuilder selectQueryBuilder, RowMapper<T> rowMapper,
                                Optional<String> hashKey) {
        this.producer = producer;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.redisTemplate = redisTemplate;
        this.selectQueryBuilder = selectQueryBuilder;
        this.rowMapper = rowMapper;
        hashKey.ifPresent(h -> this.hashKey = hashKey.get());
    }

    public List<T> findById(List<String> ids) {
        return findById(ids, false);
    }

    public List<T> findById(List<String> ids, boolean includeDeleted) {
        ArrayList<T> objFound = new ArrayList<>();
        Collection<Object> collection = new ArrayList<>(ids);
        List<Object> objFromCache = redisTemplate.opsForHash()
                .multiGet(hashKey, collection);
        if (!objFromCache.isEmpty() && !objFromCache.contains(null)) {
            log.info("Cache hit");
            objFound = (ArrayList<T>) objFromCache.stream().map(Object.class::cast)
                    .collect(Collectors.toList());
            // return only if all the objFromCache are found in cache
            Method getIdMethod = getMethod("getId", getObjClass(objFromCache));
            ids.removeAll(objFound.stream()
                    .map(obj -> (String) ReflectionUtils.invokeMethod(getIdMethod, obj))
                    .collect(Collectors.toList()));
            if (ids.isEmpty()) {
                return objFound;
            }
        }

        String query = "SELECT * FROM PRODUCT WHERE id IN (:ids) AND isDeleted = false";
        if (includeDeleted) {
            query = "SELECT * FROM PRODUCT WHERE id IN (:ids)";
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ids", ids);

        objFound.addAll(namedParameterJdbcTemplate.query(query, paramMap, rowMapper));
        putInCache(objFound);
        return objFound;
    }

    public List<T> save(List<T> objects, String topic) {
        producer.push(topic, objects);
        log.info("Pushed to kafka");
        putInCache(objects);
        return objects;
    }

    private void putInCache(List<T> objects) {
        Method getIdMethod = getMethod("getId", getObjClass(objects));
        Map<String, T> objMap = objects.stream()
                .collect(Collectors
                        .toMap(obj -> (String) ReflectionUtils.invokeMethod(getIdMethod, obj),
                                obj -> obj));
        redisTemplate.opsForHash().putAll(hashKey, objMap);
        redisTemplate.expire(hashKey, Long.parseLong(timeToLive), TimeUnit.SECONDS);
    }

    public List<T> find(Object searchObject,
                              Integer limit,
                              Integer offset,
                              String tenantId,
                              Long lastChangedSince,
                              Boolean includeDeleted) throws QueryBuilderException {
        String query = selectQueryBuilder.build(searchObject);
        query += " and tenantId=:tenantId ";
        if (Boolean.FALSE.equals(includeDeleted)) {
            query += "and isDeleted=:isDeleted ";
        }
        if (lastChangedSince != null) {
            query += "and lastModifiedTime>=:lastModifiedTime ";
        }
        query += "ORDER BY id ASC LIMIT :limit OFFSET :offset";
        Map<String, Object> paramsMap = selectQueryBuilder.getParamsMap();
        paramsMap.put("tenantId", tenantId);
        paramsMap.put("isDeleted", includeDeleted);
        paramsMap.put("lastModifiedTime", lastChangedSince);
        paramsMap.put("limit", limit);
        paramsMap.put("offset", offset);
        return namedParameterJdbcTemplate.query(query, paramsMap, rowMapper);
    }
}

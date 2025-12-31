package com.ben;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;

public class CaffeineDemo {

    public static void main(String[] args) {

        Cache<Object, Object> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofMinutes(1))
                .removalListener((key, value, cause) ->
                        System.out.printf("Key %s was removed (%s)%n", key, cause))
                .build();

        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        Object key1Result = cache.get("key", (key) -> "not_exist");
        System.out.println(key1Result);


//        ConcurrentMap<Object, CompletableFuture<Object>> map = Caffeine.newBuilder()
//                .maximumSize(10_000)
//                .expireAfterWrite(Duration.ofMinutes(1))
//                .buildAsync().asMap();


//        long sizeL = cache.estimatedSize();
//        System.out.println(sizeL);


//        LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
//                .maximumSize(10_000)
//                .expireAfterWrite(Duration.ofMinutes(10))
//                .removalListener((Key key, Graph graph, RemovalCause cause) ->
//                        System.out.printf("Key %s was removed (%s)%n", key, cause))
//                .build(key -> createExpensiveGraph(key));


    }

}
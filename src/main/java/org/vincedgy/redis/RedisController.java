package org.vincedgy.redis;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Log4j2
@RestController
public class RedisController {

    private static final String KEY_PATTERN = "ACC:*:*:Limit";
    private static final String SEP = ":";

    private final ReactiveRedisOperations<String, String> redisOperations;

    @Autowired
    private RedisTemplate<String, String> template;

    public RedisController(final ReactiveRedisConnectionFactory factory, final RedisOperations<String, String> redis) {
        this.redisOperations = new ReactiveRedisTemplate<>(factory, RedisSerializationContext.string());

    }

    private static String getAccountWithinKey(String k) {
        return k.split(SEP)[1];
    }

    private static String getDateWithinKey(String k) {
        return k.split(SEP)[2];
    }

    private static String getKey(String key) {
        return "ACC" + SEP + key.split(SEP)[1] + SEP + key.split(SEP)[2];
    }

    @GetMapping("/accounts/{key}")
    Mono<String> getAccountLimit(@PathVariable("key") String key) {
        return redisOperations.opsForValue()
                .get(key);
    }

    @GetMapping("/accounts")
    Flux<AccountLimit> getAllAccounts() {
        return redisOperations.keys(KEY_PATTERN)
                .map(k -> {

                    //String s = template.opsForValue().get(getKey(k));
                    String s = null;

                    AccountLimit acc = new AccountLimit();
                    acc.setAccount(getAccountWithinKey(k));
                    acc.setDate(getDateWithinKey(k));
                    acc.setKey(getKey(k));
                    acc.setLimit(s);
                    return acc;
                });
    }


}

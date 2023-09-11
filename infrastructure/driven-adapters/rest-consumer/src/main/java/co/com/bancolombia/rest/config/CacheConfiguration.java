package co.com.bancolombia.rest.config;

import co.com.bancolombia.binstash.adapter.memory.MemoryStash;
import co.com.bancolombia.binstash.model.api.ObjectCache;
import co.com.bancolombia.d2b.cache.CacheFactory;
import co.com.bancolombia.d2b.cache.FunctionalCacheOpsImpl;
import co.com.bancolombia.d2b.model.cache.FunctionalCacheOps;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
public class CacheConfiguration {
    ObjectMapper objectMapper = new ObjectMapper();

    @Bean(name = "productTypeCache")
    public FunctionalCacheOps<Map<String, String>> productTypeCache(@Value("${d2b.cache.productTypesCacheTtl}")
                                                                            Integer productTypesCacheTtl) {
        var cacheFactory = new CacheFactory(buildMemoryStash(productTypesCacheTtl), null, objectMapper);
        ObjectCache<Map<String, String>> objectCache = cacheFactory.createLocalObjectCache();
        return new FunctionalCacheOpsImpl<>(objectCache, (Class<Map<String, String>>) (Object) Map.class);
    }

    @Bean(name = "bankEntitiesCache")
    public FunctionalCacheOps<HashSet<String>> bankEntitiesCache(@Value("${d2b.cache.bankEntitiesCacheTtl}")
                                                                    Integer bankEntitiesCacheTtl) {
        var cacheFactory = new CacheFactory(buildMemoryStash(bankEntitiesCacheTtl), null, objectMapper);
        ObjectCache<HashSet<String>> objectCache = cacheFactory.createLocalObjectCache();
        return new FunctionalCacheOpsImpl<>(objectCache, (Class<HashSet<String>>) (Object) Set.class);
    }

    private MemoryStash buildMemoryStash(Integer expirationTime) {
        return new MemoryStash.Builder()
                .expireAfter(expirationTime)
                .build();
    }
}
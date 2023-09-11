package co.com.bancolombia.rest.entities;

import co.com.bancolombia.d2b.model.cache.FunctionalCacheOps;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.entity.EntityAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Component
public class EntityAdapterImpl implements EntityAdapter {

    private String nequiBankCode;
    private String bankCode;
    private final RestEntityAdapter restEntityAdapter;
    private final FunctionalCacheOps<HashSet<String>> cache;
    private final String channel;
    private static final Integer PARAMETER_PAGE_NUMBER_VALUE = 1;
    public static final String BANK_ENTITIES_CACHE_KEY = "bankEntities";

    public EntityAdapterImpl(RestEntityAdapter restEntityAdapter,
                             FunctionalCacheOps<HashSet<String>> bankEntitiesCache,
                             @Value("${adapter.channel}") String channel,
                             @Value("${adapter.entityCodes.Nequi}") String nequiBankCode,
                             @Value("${adapter.entityCodes.Bancolombia}") String bankCode) {
        this.restEntityAdapter = restEntityAdapter;
        this.cache = bankEntitiesCache;
        this.channel = channel;
        this.nequiBankCode = nequiBankCode;
        this.bankCode = bankCode;
    }

    @Override
    public String getBankCode() {
        return bankCode;
    }

    @Override
    public String getNequiBankCode() {
        return nequiBankCode;
    }

    @Override
    public Mono<HashSet<String>> getAffiliateCodes() {
        return cache.getFromCache(BANK_ENTITIES_CACHE_KEY, () ->
            restEntityAdapter.getTotalPages(channel)
                    .flatMap(totalPages ->
                            Flux.range(PARAMETER_PAGE_NUMBER_VALUE, totalPages)
                                    .flatMap(pageNumber -> restEntityAdapter.getBankEntities(channel, pageNumber))
                                    .flatMap(Flux::fromIterable)
                                    .collect(HashSet<String>::new, Set::add)))
                .onErrorMap(exception ->
                        new TechnicalException(exception, TechnicalErrorMessage.ERROR_GETTING_BANK_ENTITIES));
    }
}
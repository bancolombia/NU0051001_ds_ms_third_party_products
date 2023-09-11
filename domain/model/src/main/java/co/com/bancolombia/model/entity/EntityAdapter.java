package co.com.bancolombia.model.entity;

import reactor.core.publisher.Mono;

import java.util.HashSet;

public interface EntityAdapter {

    String getBankCode();
    Mono<HashSet<String>> getAffiliateCodes();
    String getNequiBankCode();
}

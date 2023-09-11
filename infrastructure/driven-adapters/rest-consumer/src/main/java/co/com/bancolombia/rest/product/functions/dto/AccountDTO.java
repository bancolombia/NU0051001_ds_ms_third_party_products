package co.com.bancolombia.rest.product.functions.dto;

import lombok.NonNull;

public record AccountDTO(@NonNull String type, @NonNull String number, @NonNull String bankCode) {
}
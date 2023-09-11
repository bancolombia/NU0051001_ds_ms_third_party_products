package co.com.bancolombia.api.product.mapper;

import co.com.bancolombia.api.product.dto.BeneficiarySearchDTO;
import co.com.bancolombia.api.product.dto.DateRangeDTO;
import co.com.bancolombia.api.product.dto.IdentificationSearchDTO;
import co.com.bancolombia.api.product.dto.ProductSearchDTO;
import co.com.bancolombia.api.product.dto.request.FunctionDTO;
import co.com.bancolombia.api.product.dto.request.ProductSearchRequestDTO;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.DateRange;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.Product.ProductBuilder;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.model.user.Identification;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class SearchCriteriaMapper {

    private static final String TIME_FORMAT = "yyyy-MM-dd";

    public Product mapSearchCriteriaToProduct(ProductSearchRequestDTO productSearchRequestDTO, Context context) {
        ProductBuilder product = Product.builder();

        product.beneficiary(beneficiarySearchDTOToBeneficiary(productSearchRequestDTO.beneficiary()));
        product.name(productSearchRequestDTO.customName());
        product.number(getDTOProductNumber(productSearchRequestDTO));
        product.type(getDTOProductType(productSearchRequestDTO));
        product.entity(productSearchRequestDTO.bankingEntityCode());
        product.customer(new Customer(new Identification(
                context.getCustomer().getIdentification().type(), context.getCustomer().getIdentification().number()),
                context.getCustomer().getMdmCode()));
        Set<FunctionDTO> set = productSearchRequestDTO.functions();
        if (set != null) {
            product.functionList(set.stream()
                    .map(result -> Function.findFunction(result.code()))
                    .collect(Collectors.toSet()));
        }

        return product.build();
    }

    public DateRange mapSearchCriteriaToDateRange(ProductSearchRequestDTO productSearchRequestDTO) {
        var dateRangeDTO = productSearchRequestDTO.dateRange();
        if (dateRangeDTO == null) {
            return new DateRange(null, null);
        }

        return new DateRange(getInitialDate(dateRangeDTO), getEndDate(dateRangeDTO));
    }

    private Identification identificationSearchDTOToIdentification(IdentificationSearchDTO identificationSearchDTO) {
        if (identificationSearchDTO == null) {
            return new Identification(null, null);
        }

        String type = null;
        String number = null;

        type = identificationSearchDTO.type();
        number = identificationSearchDTO.number();

        return new Identification(type, number);
    }

    private Beneficiary beneficiarySearchDTOToBeneficiary(BeneficiarySearchDTO beneficiarySearchDTO) {
        if (beneficiarySearchDTO == null) {
            return new Beneficiary(new Identification(null, null), null);
        }

        Identification identification = null;
        String name = null;

        identification = identificationSearchDTOToIdentification(beneficiarySearchDTO.identification());
        name = beneficiarySearchDTO.name();

        return new Beneficiary(identification, name);
    }

    private String getDTOProductNumber(ProductSearchRequestDTO productSearchRequestDTO) {
        ProductSearchDTO product = productSearchRequestDTO.product();
        if (product == null) {
            return null;
        }
        return product.number();
    }

    private String getDTOProductType(ProductSearchRequestDTO productSearchRequestDTO) {
        ProductSearchDTO product = productSearchRequestDTO.product();
        if (product == null) {
            return null;
        }
        return product.type();
    }

    private LocalDateTime getInitialDate(DateRangeDTO dateRangeDTO) {
        if (dateRangeDTO.start() == null) {
            return null;
        }
        var formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        try {
            return LocalDate.parse(dateRangeDTO.start(), formatter).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new TechnicalException(e, TechnicalErrorMessage.DATE_FORMAT_IS_NOT_VALID);
        }
    }

    private LocalDateTime getEndDate(DateRangeDTO dateRangeDTO) {
        if (dateRangeDTO.end() == null) {
            return null;
        }
        var formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        try {
            return LocalDate.parse(dateRangeDTO.end(), formatter).atStartOfDay().plusDays(1).minusSeconds(1);
        } catch (DateTimeParseException e) {
            throw new TechnicalException(e, TechnicalErrorMessage.DATE_FORMAT_IS_NOT_VALID);
        }
    }
}
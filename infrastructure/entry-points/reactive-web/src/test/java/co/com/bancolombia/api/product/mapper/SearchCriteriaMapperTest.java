package co.com.bancolombia.api.product.mapper;

import co.com.bancolombia.api.builders.ProductTestBuilder;
import co.com.bancolombia.api.product.dto.BeneficiarySearchDTO;
import co.com.bancolombia.api.product.dto.DateRangeDTO;
import co.com.bancolombia.api.product.dto.request.FunctionDTO;
import co.com.bancolombia.api.product.dto.request.ProductSearchRequestDTO;
import co.com.bancolombia.builders.ContextCreator;
import co.com.bancolombia.exception.technical.TechnicalException;
import co.com.bancolombia.exception.technical.message.TechnicalErrorMessage;
import co.com.bancolombia.model.commons.Function;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchCriteriaMapperTest {


    @Test
    void shouldMapProductWithAllValues() {
        var productDTO = ProductTestBuilder.buildProductSearchRequestDTO();
        var context = ContextCreator.buildNewContext();

        var product = SearchCriteriaMapper.mapSearchCriteriaToProduct(productDTO, context);
        var dateRange = SearchCriteriaMapper.mapSearchCriteriaToDateRange(productDTO);

        assertEquals(productDTO.customName(), product.getName());
        assertEquals(productDTO.bankingEntityCode(), product.getEntity());
        assertEquals(productDTO.product().number(), product.getNumber());
        assertEquals(productDTO.product().type(), product.getType());
        assertEquals(productDTO.beneficiary().name(), product.getBeneficiary().getName());
        assertEquals(productDTO.beneficiary().identification().number(), product.getBeneficiary().getIdentification().number());
        assertEquals(productDTO.beneficiary().identification().type(), product.getBeneficiary().getIdentification().type());
        assertEquals(((FunctionDTO)productDTO.functions().toArray()[0]).code(), ((Function) product.getFunctionList().toArray()[0]).getProductFunction());

        assertEquals(productDTO.dateRange().start()+"T00:00", dateRange.initialDate().toString());
        assertEquals(productDTO.dateRange().end()+"T23:59:59", dateRange.endDate().toString());
    }

    @Test
    void shouldMapWithFieldsInNull() {
        var productDTO = new ProductSearchRequestDTO(null, null, null, null, null, null);
        var context = ContextCreator.buildNewContext();

        var product = SearchCriteriaMapper.mapSearchCriteriaToProduct(productDTO, context);
        var dateRange = SearchCriteriaMapper.mapSearchCriteriaToDateRange(productDTO);

        assertNotNull(product.getBeneficiary());
        assertNull(product.getBeneficiary().getName());
        assertNotNull(product.getBeneficiary().getIdentification());
        assertNull(product.getBeneficiary().getIdentification().type());
        assertNull(product.getBeneficiary().getIdentification().number());

        assertNotNull(dateRange);
        assertNull(dateRange.initialDate());
        assertNull(dateRange.endDate());
    }

    @Test
    void shouldMapWithSubFieldsInNull() {
        var productDTO = new ProductSearchRequestDTO(null, null, null,
                new BeneficiarySearchDTO(null, null), null, new DateRangeDTO(null, null));
        var context = ContextCreator.buildNewContext();

        var product = SearchCriteriaMapper.mapSearchCriteriaToProduct(productDTO, context);
        var dateRange = SearchCriteriaMapper.mapSearchCriteriaToDateRange(productDTO);

        assertNotNull(product.getBeneficiary());
        assertNull(product.getBeneficiary().getName());
        assertNotNull(product.getBeneficiary().getIdentification());
        assertNull(product.getBeneficiary().getIdentification().type());
        assertNull(product.getBeneficiary().getIdentification().number());

        assertNotNull(dateRange);
        assertNull(dateRange.initialDate());
        assertNull(dateRange.endDate());
    }

    @Test
    void shouldThrowExceptionWithBadDateFormats() {
        var productDTOInitial = new ProductSearchRequestDTO(null, null, null,
                new BeneficiarySearchDTO(null, null), null, new DateRangeDTO("null", null));
        var productDTOFinal = new ProductSearchRequestDTO(null, null, null,
                new BeneficiarySearchDTO(null, null), null, new DateRangeDTO(null, "null"));

        TechnicalException technicalException = assertThrows(TechnicalException.class, () ->
                SearchCriteriaMapper.mapSearchCriteriaToDateRange(productDTOInitial));
        assertEquals(TechnicalErrorMessage.DATE_FORMAT_IS_NOT_VALID, technicalException.getTechnicalErrorMessage());
        technicalException = assertThrows(TechnicalException.class, () ->
                SearchCriteriaMapper.mapSearchCriteriaToDateRange(productDTOFinal));
        assertEquals(TechnicalErrorMessage.DATE_FORMAT_IS_NOT_VALID, technicalException.getTechnicalErrorMessage());
    }

}
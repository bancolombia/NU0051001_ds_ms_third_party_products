package co.com.bancolombia.api.product.mapper;

import co.com.bancolombia.api.product.dto.request.FunctionDTO;
import co.com.bancolombia.api.product.dto.request.ProductEnrollmentRequestDTO;
import co.com.bancolombia.api.product.dto.request.ProductModifyRequestDTO;
import co.com.bancolombia.api.product.dto.response.ProductEnrollmentResponseDTO;
import co.com.bancolombia.api.product.dto.response.ProductModifyResponseDTO;
import co.com.bancolombia.api.product.dto.response.ProductSearchResponseDTO;
import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.commons.Function;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.exception.message.BusinessErrorMessage;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.user.Beneficiary;
import co.com.bancolombia.model.user.Customer;
import co.com.bancolombia.model.user.Identification;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class ProductMapper {

    public Product mapToProduct(ProductEnrollmentRequestDTO request, Context context) {
        return Product.builder()
                .name(request.customName())
                .number(request.product().number())
                .type(request.product().type())
                .entity(request.bankingEntityCode())
                .functionList(mapToFunction(request.functions()))
                .customer(new Customer(new Identification(context.getCustomer().getIdentification().type(),
                        context.getCustomer().getIdentification().number()), context.getCustomer().getMdmCode()))
                .beneficiary(new Beneficiary(new Identification(request.beneficiary().identification().type()
                        , request.beneficiary().identification().number()), request.beneficiary().name()))
                .build();
    }

    public DataModify mapToData(ProductModifyRequestDTO request) {
        var dataModify = new DataModify();

        if (Boolean.TRUE.equals( isDataValidToModify(request.customName(), request.functions()))) {
            if (request.customName() != null) {
                dataModify.setCustomName(request.customName());
            }
            if (request.functions() != null) {
                dataModify.setFunctions(mapToFunction(request.functions()));
            }
        }
        return dataModify;
    }

    public Boolean  isDataValidToModify(String name, Set<FunctionDTO> functionList) {
        if ((name == null && functionList == null) || ((name != null && name.isBlank()) ||
                (functionList != null && functionList.isEmpty()))) {
            throw new BusinessException(BusinessErrorMessage.INVALID_DATA_TO_MODIFY);
        }
        return Boolean.TRUE;
    }

    public Set<Function> mapToFunction(Set<FunctionDTO> request) {
        return request.stream()
                .map(result -> Function.findFunction(result.code()))
                .collect(Collectors.toSet());
    }


    public ProductEnrollmentResponseDTO mapToProductEnrollmentResponse(Product product) {
        return new ProductEnrollmentResponseDTO(
                new ProductEnrollmentResponseDTO.ProductEnrollmentDataDTO(product.getState()));
    }

    public ProductModifyResponseDTO mapToProductModifyResponse(Product product,
                                                               String name, Set<Function> functionList) {
        Set<FunctionDTO> functions = new HashSet<>();
        if (functionList != null) {
            functions = mapToFunctionDTO(product.getFunctionList());
        }
        if (name != null) {
            name = product.getName();
        }
        return new ProductModifyResponseDTO(
                new ProductModifyResponseDTO.ProductModifyDataDTO(name, functions));
    }

    public Set<FunctionDTO> mapToFunctionDTO(Set<Function> functionList) {
        return functionList.stream()
                .map(Function::getProductFunction)
                .map(FunctionDTO::new)
                .collect(Collectors.toSet());
    }


    public ProductSearchResponseDTO mapToProductSearchResponse(Product product) {
        Set<ProductSearchResponseDTO.SearchFunctionDTO> functionList = new HashSet<>();
        for (Function function : product.getFunctionList()) {
            functionList.add(new ProductSearchResponseDTO.SearchFunctionDTO(function.getProductFunction()));
        }

        return ProductSearchResponseDTO.builder()
                .id(product.getId())
                .inscriptionDate(product.getInscriptionDate().toString())
                .customName(product.getName())
                .bankingEntity(product.getEntity())
                .product(new ProductSearchResponseDTO.SearchProductDTO(product.getType(), product.getNumber()))
                .beneficiary(new ProductSearchResponseDTO.SearchBeneficiaryDTO(
                        product.getBeneficiary().getName(),
                        new ProductSearchResponseDTO.SearchIdentificationDTO(
                                product.getBeneficiary().getIdentification().number(),
                                product.getBeneficiary().getIdentification().type())))
                .functions(functionList)
                .build();
    }
}

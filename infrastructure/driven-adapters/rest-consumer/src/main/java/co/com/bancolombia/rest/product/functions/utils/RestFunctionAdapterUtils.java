package co.com.bancolombia.rest.product.functions.utils;

import co.com.bancolombia.model.commons.Context;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.rest.commons.dto.IdentificationDTO;
import co.com.bancolombia.rest.product.functions.dto.AccountDTO;
import co.com.bancolombia.rest.product.functions.dto.BeneficiaryDTO;
import co.com.bancolombia.rest.product.functions.dto.RequestEnrollNequiTransferDTO;
import co.com.bancolombia.rest.product.functions.dto.RequestEnrollOtherBanksTransferDTO;
import co.com.bancolombia.rest.product.functions.dto.RequestEnrollOtherBanksTransferDTO.RequestEnrollOtherBanksTransfer;
import co.com.bancolombia.rest.product.functions.dto.RequestEnrollPayrollAndSupplierDTO;
import co.com.bancolombia.rest.product.functions.dto.RequestEnrollPayrollAndSupplierDTO.RequestEnrollPayrollAndSupplierPayment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestFunctionAdapterUtils {

    public RequestEnrollOtherBanksTransferDTO buildBodyRequestEnrollOtherBanksTransfer(Product product,
                                                                                       Context context,
                                                                                       String homologatedProductType) {

        return new RequestEnrollOtherBanksTransferDTO(
                new RequestEnrollOtherBanksTransfer(
                        new RequestEnrollOtherBanksTransferDTO.PayerDTO(
                                new IdentificationDTO(context.getCustomer().getIdentification().type(),
                                        context.getCustomer().getIdentification().number()),
                                new BeneficiaryDTO(
                                        new IdentificationDTO(product.getBeneficiary().getIdentification().type(),
                                                product.getBeneficiary().getIdentification().number()),
                                        new AccountDTO(
                                                homologatedProductType, product.getNumber(), product.getEntity()
                                        )
                                )
                        )
                )
        );
    }

    public RequestEnrollPayrollAndSupplierDTO buildBodyRequestEnrollPayrollAndSupplier(Product product,
                                                                                       Context context,
                                                                                       String paymentType,
                                                                                       String homologatedProductType) {

        return new RequestEnrollPayrollAndSupplierDTO(
                new RequestEnrollPayrollAndSupplierPayment(
                        new RequestEnrollPayrollAndSupplierDTO.PayerDTO(
                                new IdentificationDTO(
                                        context.getCustomer().getIdentification().type(),
                                        context.getCustomer().getIdentification().number()
                                )),
                        new RequestEnrollPayrollAndSupplierDTO.BeneficiaryDTO(
                                product.getBeneficiary().getName(), paymentType,
                                new IdentificationDTO(
                                        product.getBeneficiary().getIdentification().type(),
                                        product.getBeneficiary().getIdentification().number()),
                                new RequestEnrollPayrollAndSupplierDTO.AccountDTO(
                                        homologatedProductType, product.getNumber(), product.getEntity()
                                )
                        )
                )
        );
    }

    public RequestEnrollNequiTransferDTO buildBodyRequestEnrollNequiTransferDTO(Product product, Context context,
                                                                                String homologatedProductType) {

        return new RequestEnrollNequiTransferDTO(
                new RequestEnrollNequiTransferDTO.RequestEnrollNequiTransfer(
                        new RequestEnrollNequiTransferDTO.EnrollAccountRequestDTO(
                                new RequestEnrollNequiTransferDTO.PayerDTO(
                                        new IdentificationDTO(
                                                context.getCustomer().getIdentification().type(),
                                                context.getCustomer().getIdentification().number()
                                        )
                                ),
                                new BeneficiaryDTO(
                                        new IdentificationDTO(
                                                product.getBeneficiary().getIdentification().type(),
                                                product.getBeneficiary().getIdentification().number()
                                        ),
                                        new AccountDTO(
                                                homologatedProductType, product.getNumber(), product.getEntity()
                                        )
                                ),
                                new RequestEnrollNequiTransferDTO.TransactionDTO(context.getChannel())
                        )
                )
        );
    }
}

package co.com.bancolombia.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    INVALID_PAGE_NUMBER("MPB0001", "The page number must be greater than 0"),
    INVALID_PAGE_SIZE("MPB0002", "The page size must be greater than 0"),
    ERROR_IN_ENROLLMENT_OF_PRODUCT_FUNCTIONS("MPB0003", "There was an error enrolling the products functions"),
    INCORRECT_ENTITY_CODE("MPB0004", "The entity code is not correct"),
    UNAVAILABLE_PRODUCT_TYPE("MPB0005", "The product type is unavailable"),
    PRODUCT_FUNCTION_NOT_ALLOWED("MPB0006", "The function is not allowed for the product"),
    ENTITY_CODES_ARE_REQUIRED("MPB0007", "The entity codes are required. They shouldn't be null"),
    PRODUCT_ALREADY_CUSTOM_NAME("MPB0008", "The custom product name is available"),
    PRODUCT_INVALID_INFORMATION("MPB0009", "There is no change in the product information to modify"),
    ERROR_IN_MODIFY_OF_PRODUCT_FUNCTION_MANAGER("MPB0010", "Error in function manager"),
    INVALID_OWNERSHIP("MPB0011", "There is not relationship between the product and the client"),
    REQUIRED_PRODUCT_TYPE("MPB0012", "The product type is required"),
    REQUIRED_IDENTIFICATION_TYPE("MPB0013", "The identification type is required"),
    REQUIRED_IDENTIFICATION_NUMBER("MPB0014", "The identification number is required"),
    REQUIRED_PRODUCT_ID("MPB0015", "The product id is required"),
    REQUIRED_NAME("MPB0016", "The product name is required"),
    REQUIRED_PRODUCT_NUMBER("MPB0017", "The product number is required"),
    REQUIRED_ENTITY("MPB0018", "The product entity is required"),
    REQUIRED_FUNCTIONS("MPB0019", "The product functions are required"),
    REQUIRED_CUSTOMER("MPB0020", "The customer is required"),
    REQUIRED_BENEFICIARY("MPB0021", "The beneficiary is required"),
    EMPTY_FUNCTIONS("MPB0022", "The functions can't be empty"),
    PRODUCT_NOT_EXIST("MPB0023", "The product doesn't exist"),
    REQUIRED_SEARCH("MPB0024", "The search criteria is required"),
    CUSTOM_NAME_UNAVAILABLE("MPB0025", "The product custom name is unavailable"),
    PRODUCT_EXIST("MPB0026", "The product already exist"),
    NON_PRODUCTS_FOUND("MPB0027", "Could not find any products with the criteria"),
    MESSAGE_ID_IS_REQUIRED("MPB0028", "The message-id field cannot be null or empty"),
    SESSION_TRACKER_IS_REQUIRED("MPB0029", "The session-tracker field cannot be null or empty"),
    REQUEST_TIMESTAMP_IS_REQUIRED("MPB0030", "The request-timestamp field cannot be null or empty"),
    CHANNEL_IS_REQUIRED("MPB0031", "The channel field cannot be null or empty"),
    USER_AGENT_IS_REQUIRED("MPB0032", "The User-Agent field cannot be null or empty"),
    APP_VERSION_IS_REQUIRED("MPB0033", "The app-version field cannot be null or empty"),
    DEVICE_ID_IS_REQUIRED("MPB0034", "The device-id field cannot be null or empty"),
    IP_IS_REQUIRED("MPB0035", "The ip field cannot be null or empty"),
    PLATFORM_TYPE_IS_REQUIRED("MPB0036", "The platform-type field cannot be null or empty"),
    CUSTOMER_IDENTIFICATION_TYPE_IS_REQUIRED
            ("MPB0037", "The customer-identification-type field cannot be null or empty"),
    CUSTOMER_IDENTIFICATION_NUMBER_IS_REQUIRED
            ("MPB0038", "The customer-identification-number field cannot be null or empty"),
    MDM_CODE_IS_REQUIRED("MPB0039", "The mdm-code field cannot be null or empty"),
    CONTENT_TYPE_IS_REQUIRED("MPB0040", "The content-type field cannot be null or empty"),
    AUTHORIZATION_IS_REQUIRED("MPB0041", "The authorization field cannot be null or empty"),
    BENEFICIARY_DIFFERENT_THAN_CUSTOMER("MPB0042", "The beneficiary can't have the same " +
            "identification that the customer"),
    BENEFICIARY_NAME_IS_REQUIRED("MPB0043", "The beneficiary name is required"),
    IDENTIFICATION_IS_REQUIRED("MPB0044", "The identification is required"),
    INVALID_PRODUCT_SIZE("MPB0045", "Invalid product size"),
    INVALID_NAME_SIZE("MPB0046", "Invalid name size"),
    INVALID_ENTITY_SIZE("MPB0047", "Invalid entity size"),
    INVALID_PRODUCT_TYPE_SIZE("MPB0048", "Invalid product type size"),
    INVALID_PRODUCT_NUMBER_SIZE("MPB0049", "Invalid product number size"),
    INVALID_IDENTIFICATION_TYPE_SIZE("MPB0050", "Invalid identification type size"),
    INVALID_IDENTIFICATION_NUMBER_SIZE("MPB0051", "Invalid identification number size"),
    INVALID_BENEFICIARY_NAME_SIZE("MPB0052", "Invalid beneficiary name size"),
    INCORRECT_PRODUCT_FUNCTION("MPB0053", "The product functions is not correct"),
    INVALID_DATA_TO_MODIFY("MPB0054", "The data is invalid to modify"),
    CUSTOMER_DOES_NOT_EXIST("MPB0055", "The customer does not exist"),
    INVALID_PRODUCT_NUMBER_CONTENT("MPB0056", "Invalid product number"),
    INVALID_IDENTIFICATION_TYPE("MPB0057", "Invalid identification type"),
    REQUIRED_PRODUCT_OBJECT("MPB0058", "The product is required"),
    REQUIRED_IDENTIFICATION_OBJECT("MPB0059", "The beneficiary identification is required"),
    INVALID_PRODUCT_NUMBER("MPB0060", "Invalid product number"),
    MDM_CODE_IS_INCORRECT("MPB0061", "Client not found for the mdm-code"),
    REQUIRED_FORMAT("MPB0062", "The format is required");

    private final String code;
    private final String message;
}
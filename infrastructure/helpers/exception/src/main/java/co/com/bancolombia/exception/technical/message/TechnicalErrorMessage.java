package co.com.bancolombia.exception.technical.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalErrorMessage {

    UNEXPECTED_EXCEPTION("MPT0001", "Unexpected error"),
    DATABASE_CONNECTION("MPT0002", "Error connecting to database"),
    ERROR_EMITTING_EVENT("MPT0003", "Error emitting event"),
    WEB_CLIENT_EXCEPTION("MPT0005", "Error on web client request"),
    REST_CLIENT_EXCEPTION("MPT0006", "Unexpected rest client error"),
    TIMEOUT_EXCEPTION("MPT0007", "Time out for the consumption of the rest service has been exceeded"),
    ERROR_PROCESSING_PARAMETERS_JSON("MPT0008", "There is a error in the format of the parameter"),
    DB_SECRET_EXCEPTION("MPT0009", "Error getting database connection secret"),
    ERROR_REMOVING_PRODUCT_DATABASE("MPT0010", "Error removing product from database"),
    ERROR_GETTING_PRODUCT("MPT0011", "Error getting product from database"),
    ERROR_GETTING_CUSTOMNAME("MPT0012", "Error getting customName from database"),
    ERROR_VALIDATE_PRODUCT("MPT0013", "Error product validation from database"),
    DATE_FORMAT_IS_NOT_VALID("MPT0014", "The start and end date fields must contain the date in the format yyyy-MM-dd"),
    ERROR_GETTING_CUSTOMER_DATABASE("MPT0015", "There is an error getting the customer"),
    ERROR_GETTING_BENEFICIARY_DATABASE("MPT0016", "There is an error getting the beneficiary"),
    ERROR_SAVING_PRODUCT_DATABASE("MPT0017", "There is an error saving the product"),
    ERROR_SEARCHING_PRODUCTS("MT0018", "Error searching products from database"),
    ERROR_IN_MODIFY_PRODUCT_DATABASE("MT0019", "Error when modifying product in the database"),
    GENERATE_REPORT("MPT0020", "Error generating report"),
    INVALID_REPORT_FIELD_FORMAT("MPT0021", "invalid report field format. Must be of type JSON"),
    INVALID_REPORT_FORMAT("MPT0022", "invalid report format"),
    SAVE_REPORT("MPT0023", "Error saving report"),
    PARAMETER_NOT_FOUND("MPT0024", "The parameter does not exist"),
    ERROR_GETTING_PRODUCT_TYPES("MPT0025", "There is an error getting the product types"),
    ERROR_GETTING_BANK_ENTITIES("MPT0026", "There is an error getting the bank entities");

    private final String code;
    private final String message;
}
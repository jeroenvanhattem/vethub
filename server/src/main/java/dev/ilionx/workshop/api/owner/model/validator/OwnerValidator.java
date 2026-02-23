package dev.ilionx.workshop.api.owner.model.validator;

import dev.ilionx.workshop.api.owner.model.request.CreateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.request.UpdateOwnerRequest;
import io.github.jframe.exception.core.ValidationException;
import io.github.jframe.validation.ValidationResult;
import io.github.jframe.validation.Validator;

import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * Validator for owner requests.
 */
@Component
public class OwnerValidator implements Validator<Object> {

    // Error messages
    public static final String BODY_IS_MISSING = "Request body is missing";
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_TOO_LONG = "First name must not exceed 255 characters";
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_TOO_LONG = "Last name must not exceed 255 characters";
    public static final String ADDRESS_TOO_LONG = "Address must not exceed 255 characters";
    public static final String CITY_TOO_LONG = "City must not exceed 255 characters";
    public static final String TELEPHONE_TOO_LONG = "Telephone must not exceed 255 characters";
    public static final String TELEPHONE_INVALID_FORMAT = "Telephone must contain only digits";

    // Fields
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String ADDRESS = "address";
    public static final String CITY = "city";
    public static final String TELEPHONE = "telephone";

    // Telephone pattern: digits only
    private static final String DIGITS_ONLY_PATTERN = "\\d+";

    /**
     * Validates a CreateOwnerRequest.
     *
     * @param request the create request
     * @param result  the validation result
     */
    public void validate(final CreateOwnerRequest request, final ValidationResult result) {
        if (isNull(request)) {
            result.reject(BODY_IS_MISSING);
            throw new ValidationException(result);
        }

        result.rejectField(FIRST_NAME, request.getFirstName())
            .whenNull(FIRST_NAME_REQUIRED)
            .orWhen(String::isEmpty, FIRST_NAME_REQUIRED)
            .orWhen(String::isBlank, FIRST_NAME_REQUIRED)
            .orWhen(name -> name.length() > 255, FIRST_NAME_TOO_LONG);

        result.rejectField(LAST_NAME, request.getLastName())
            .whenNull(LAST_NAME_REQUIRED)
            .orWhen(String::isEmpty, LAST_NAME_REQUIRED)
            .orWhen(String::isBlank, LAST_NAME_REQUIRED)
            .orWhen(name -> name.length() > 255, LAST_NAME_TOO_LONG);

        result.rejectField(ADDRESS, request.getAddress())
            .when(val -> val != null && val.length() > 255, ADDRESS_TOO_LONG);

        result.rejectField(CITY, request.getCity())
            .when(val -> val != null && val.length() > 255, CITY_TOO_LONG);

        result.rejectField(TELEPHONE, request.getTelephone())
            .when(val -> val != null && val.length() > 255, TELEPHONE_TOO_LONG)
            .orWhen(val -> val != null && !val.isEmpty() && !val.matches(DIGITS_ONLY_PATTERN), TELEPHONE_INVALID_FORMAT);

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }

    /**
     * Validates an UpdateOwnerRequest.
     *
     * @param request the update request
     * @param result  the validation result
     */
    public void validate(final UpdateOwnerRequest request, final ValidationResult result) {
        if (isNull(request)) {
            result.reject(BODY_IS_MISSING);
            throw new ValidationException(result);
        }

        result.rejectField(FIRST_NAME, request.getFirstName())
            .whenNull(FIRST_NAME_REQUIRED)
            .orWhen(String::isEmpty, FIRST_NAME_REQUIRED)
            .orWhen(String::isBlank, FIRST_NAME_REQUIRED)
            .orWhen(name -> name.length() > 255, FIRST_NAME_TOO_LONG);

        result.rejectField(LAST_NAME, request.getLastName())
            .whenNull(LAST_NAME_REQUIRED)
            .orWhen(String::isEmpty, LAST_NAME_REQUIRED)
            .orWhen(String::isBlank, LAST_NAME_REQUIRED)
            .orWhen(name -> name.length() > 255, LAST_NAME_TOO_LONG);

        result.rejectField(ADDRESS, request.getAddress())
            .when(val -> val != null && val.length() > 255, ADDRESS_TOO_LONG);

        result.rejectField(CITY, request.getCity())
            .when(val -> val != null && val.length() > 255, CITY_TOO_LONG);

        result.rejectField(TELEPHONE, request.getTelephone())
            .when(val -> val != null && val.length() > 255, TELEPHONE_TOO_LONG)
            .orWhen(val -> val != null && !val.isEmpty() && !val.matches(DIGITS_ONLY_PATTERN), TELEPHONE_INVALID_FORMAT);

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(final Object request, final ValidationResult result) {
        if (request instanceof CreateOwnerRequest) {
            validate((CreateOwnerRequest) request, result);
        } else if (request instanceof UpdateOwnerRequest) {
            validate((UpdateOwnerRequest) request, result);
        } else {
            result.reject("Unsupported request type");
            throw new ValidationException(result);
        }
    }
}

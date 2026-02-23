package dev.ilionx.workshop.api.owner.model.validator;

import dev.ilionx.workshop.api.owner.model.request.CreateOwnerRequest;
import dev.ilionx.workshop.api.owner.model.request.UpdateOwnerRequest;
import dev.ilionx.workshop.support.UnitTest;
import io.github.jframe.exception.core.ValidationException;
import io.github.jframe.validation.ValidationResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.ilionx.workshop.api.owner.model.validator.OwnerValidator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit Test - Owner Validator.
 */
@DisplayName("Unit Test - Owner Validator")
public class OwnerValidatorTest extends UnitTest {

    private static final String VALID_FIRST_NAME = "George";
    private static final String VALID_LAST_NAME = "Franklin";
    private static final String VALID_ADDRESS = "110 W. Liberty St.";
    private static final String VALID_CITY = "Madison";
    private static final String VALID_TELEPHONE = "6085551023";
    private static final String STRING_OF_255 = "A".repeat(255);
    private static final String STRING_OF_256 = "A".repeat(256);

    private OwnerValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new OwnerValidator();
    }

    // ==================== Create Request Tests ====================

    @Test
    @DisplayName("Should accept valid create request")
    public void shouldAcceptValidCreateRequest() {
        // Given: Valid create request
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setAddress(VALID_ADDRESS)
            .setCity(VALID_CITY)
            .setTelephone(VALID_TELEPHONE);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should accept create request without optional fields")
    public void shouldAcceptCreateRequestWithoutOptionalFields() {
        // Given: Valid request without optional fields
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject null create request body")
    public void shouldRejectNullCreateRequestBody() {
        // Given: Null request
        final CreateOwnerRequest request = null;
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().getErrors().size(), is(1));
        assertThat(
            exception.getValidationResult().getErrors().get(0).getCode(),
            is(equalTo(BODY_IS_MISSING))
        );
    }

    @Test
    @DisplayName("Should reject null firstName in create request")
    public void shouldRejectNullFirstNameInCreateRequest() {
        // Given: Request with null firstName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject empty firstName in create request")
    public void shouldRejectEmptyFirstNameInCreateRequest() {
        // Given: Request with empty firstName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName("")
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject blank firstName in create request")
    public void shouldRejectBlankFirstNameInCreateRequest() {
        // Given: Request with blank firstName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName("   ")
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject firstName exceeding 255 characters in create request")
    public void shouldRejectFirstNameExceeding255CharactersInCreateRequest() {
        // Given: Request with firstName exceeding 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(STRING_OF_256)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept firstName with exactly 255 characters in create request")
    public void shouldAcceptFirstNameWithExactly255CharactersInCreateRequest() {
        // Given: Request with firstName of exactly 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(STRING_OF_255)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject null lastName in create request")
    public void shouldRejectNullLastNameInCreateRequest() {
        // Given: Request with null lastName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject empty lastName in create request")
    public void shouldRejectEmptyLastNameInCreateRequest() {
        // Given: Request with empty lastName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName("");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject blank lastName in create request")
    public void shouldRejectBlankLastNameInCreateRequest() {
        // Given: Request with blank lastName
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName("   ");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject lastName exceeding 255 characters in create request")
    public void shouldRejectLastNameExceeding255CharactersInCreateRequest() {
        // Given: Request with lastName exceeding 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(STRING_OF_256);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept lastName with exactly 255 characters in create request")
    public void shouldAcceptLastNameWithExactly255CharactersInCreateRequest() {
        // Given: Request with lastName of exactly 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(STRING_OF_255);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject address exceeding 255 characters in create request")
    public void shouldRejectAddressExceeding255CharactersInCreateRequest() {
        // Given: Request with address exceeding 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setAddress(STRING_OF_256);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(ADDRESS) &&
                        error.getCode().equals(ADDRESS_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept address with exactly 255 characters in create request")
    public void shouldAcceptAddressWithExactly255CharactersInCreateRequest() {
        // Given: Request with address of exactly 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setAddress(STRING_OF_255);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject city exceeding 255 characters in create request")
    public void shouldRejectCityExceeding255CharactersInCreateRequest() {
        // Given: Request with city exceeding 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setCity(STRING_OF_256);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(CITY) &&
                        error.getCode().equals(CITY_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept city with exactly 255 characters in create request")
    public void shouldAcceptCityWithExactly255CharactersInCreateRequest() {
        // Given: Request with city of exactly 255 characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setCity(STRING_OF_255);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject telephone exceeding 255 characters in create request")
    public void shouldRejectTelephoneExceeding255CharactersInCreateRequest() {
        // Given: Request with telephone exceeding 255 digits
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setTelephone("1".repeat(256));
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(TELEPHONE) &&
                        error.getCode().equals(TELEPHONE_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject telephone with non-digit characters in create request")
    public void shouldRejectTelephoneWithNonDigitCharactersInCreateRequest() {
        // Given: Request with telephone containing non-digit characters
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setTelephone("608-555-1023");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(TELEPHONE) &&
                        error.getCode().equals(TELEPHONE_INVALID_FORMAT)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept telephone with only digits in create request")
    public void shouldAcceptTelephoneWithOnlyDigitsInCreateRequest() {
        // Given: Request with valid telephone
        final CreateOwnerRequest request = new CreateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setTelephone(VALID_TELEPHONE);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    // ==================== Update Request Tests ====================

    @Test
    @DisplayName("Should accept valid update request")
    public void shouldAcceptValidUpdateRequest() {
        // Given: Valid update request
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setAddress(VALID_ADDRESS)
            .setCity(VALID_CITY)
            .setTelephone(VALID_TELEPHONE);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should accept update request without optional fields")
    public void shouldAcceptUpdateRequestWithoutOptionalFields() {
        // Given: Valid update request without optional fields
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject null update request body")
    public void shouldRejectNullUpdateRequestBody() {
        // Given: Null update request
        final UpdateOwnerRequest request = null;
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().getErrors().size(), is(1));
        assertThat(
            exception.getValidationResult().getErrors().get(0).getCode(),
            is(equalTo(BODY_IS_MISSING))
        );
    }

    @Test
    @DisplayName("Should reject null firstName in update request")
    public void shouldRejectNullFirstNameInUpdateRequest() {
        // Given: Update request with null firstName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject empty firstName in update request")
    public void shouldRejectEmptyFirstNameInUpdateRequest() {
        // Given: Update request with empty firstName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName("")
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject blank firstName in update request")
    public void shouldRejectBlankFirstNameInUpdateRequest() {
        // Given: Update request with blank firstName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName("   ")
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject firstName exceeding 255 characters in update request")
    public void shouldRejectFirstNameExceeding255CharactersInUpdateRequest() {
        // Given: Update request with firstName exceeding 255 characters
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(STRING_OF_256)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(FIRST_NAME) &&
                        error.getCode().equals(FIRST_NAME_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept firstName with exactly 255 characters in update request")
    public void shouldAcceptFirstNameWithExactly255CharactersInUpdateRequest() {
        // Given: Update request with firstName of exactly 255 characters
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(STRING_OF_255)
            .setLastName(VALID_LAST_NAME);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject null lastName in update request")
    public void shouldRejectNullLastNameInUpdateRequest() {
        // Given: Update request with null lastName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject empty lastName in update request")
    public void shouldRejectEmptyLastNameInUpdateRequest() {
        // Given: Update request with empty lastName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName("");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject blank lastName in update request")
    public void shouldRejectBlankLastNameInUpdateRequest() {
        // Given: Update request with blank lastName
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName("   ");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_REQUIRED)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should reject lastName exceeding 255 characters in update request")
    public void shouldRejectLastNameExceeding255CharactersInUpdateRequest() {
        // Given: Update request with lastName exceeding 255 characters
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(STRING_OF_256);
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(LAST_NAME) &&
                        error.getCode().equals(LAST_NAME_TOO_LONG)
                ),
            is(true)
        );
    }

    @Test
    @DisplayName("Should accept lastName with exactly 255 characters in update request")
    public void shouldAcceptLastNameWithExactly255CharactersInUpdateRequest() {
        // Given: Update request with lastName of exactly 255 characters
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(STRING_OF_255);
        final ValidationResult result = new ValidationResult();

        // When: Validating request
        validator.validate(request, result);

        // Then: Validation should pass
        assertThat(result.hasErrors(), is(false));
    }

    @Test
    @DisplayName("Should reject telephone with non-digit characters in update request")
    public void shouldRejectTelephoneWithNonDigitCharactersInUpdateRequest() {
        // Given: Update request with telephone containing non-digit characters
        final UpdateOwnerRequest request = new UpdateOwnerRequest()
            .setFirstName(VALID_FIRST_NAME)
            .setLastName(VALID_LAST_NAME)
            .setTelephone("608-555-1023");
        final ValidationResult result = new ValidationResult();

        // When & Then: Should throw ValidationException
        final ValidationException exception = assertThrows(
            ValidationException.class,
            () -> validator.validate(request, result)
        );

        assertThat(exception.getValidationResult().hasErrors(), is(true));
        assertThat(
            exception.getValidationResult().getErrors().stream()
                .anyMatch(
                    error -> error.getField().equals(TELEPHONE) &&
                        error.getCode().equals(TELEPHONE_INVALID_FORMAT)
                ),
            is(true)
        );
    }
}

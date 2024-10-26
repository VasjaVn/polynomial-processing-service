package com.example.polynomial.controller;

import com.example.polynomial.config.app.props.JwtProperties;
import com.example.polynomial.config.security.filter.JwtAuthenticationFilter;
import com.example.polynomial.config.security.filter.JwtAuthenticationFilterErrorHandler;
import com.example.polynomial.controller.advice.GlobalExceptionHandler;
import com.example.polynomial.exception.JwtAuthenticationFilterException;
import com.example.polynomial.exception.MissingJwtTokenException;
import com.example.polynomial.exception.PolynomialNotFoundException;
import com.example.polynomial.model.domain.Polynomial;
import com.example.polynomial.model.dto.response.PolynomialResponseDto;
import com.example.polynomial.service.impl.PolynomialServiceImpl;
import com.example.polynomial.util.validator.JwtTokenValidator;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.example.polynomial.model.domain.CalculationResultSource.EVALUATION;
import static com.example.polynomial.util.validator.JwtTokenValidatorTest.JWT_SECRET_KEY;
import static com.example.polynomial.util.validator.JwtTokenValidatorTest.TOKEN_IS_EXPIRED;
import static com.example.polynomial.util.validator.JwtTokenValidatorTest.TOKEN_IS_MALFORMED;
import static com.example.polynomial.util.validator.JwtTokenValidatorTest.TOKEN_IS_VALID;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PolynomialController.class)
public class PolynomialControllerTest {

    private static final String RAW_POLYNOMIAL = "x^2+2*x-1";

    private static final String SIMPLIFIED_POLYNOMIAL = "x^2+2*x-1";

    private static final int VALUE = 1;

    private static final double RESULT = 2.0;

    private MockMvc mockMvc;

    @MockBean
    private PolynomialServiceImpl polynomialServiceMock;

    @MockBean
    private JwtAuthenticationFilterErrorHandler jwtAuthenticationFilterErrorHandlerMock;

    private PolynomialResponseDto polynomialResponseDto;

    @BeforeEach
    public void setUp() {
        PolynomialController polynomialController =
                new PolynomialController(polynomialServiceMock, jwtAuthenticationFilterErrorHandlerMock);

        mockMvc = MockMvcBuilders
                .standaloneSetup(polynomialController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilters(
                        new JwtAuthenticationFilter(
                                new JwtTokenValidator(
                                        new JwtProperties(JWT_SECRET_KEY))))
                .build();

        Polynomial.CalculationResult calculationResult = new Polynomial.CalculationResult();
        calculationResult.setValue(VALUE);
        calculationResult.setResult(RESULT);
        calculationResult.setCalculationResultSource(EVALUATION);

        polynomialResponseDto = new PolynomialResponseDto(RAW_POLYNOMIAL, SIMPLIFIED_POLYNOMIAL, null, calculationResult);
    }

    @Test
    public void getApiPolynomialsEvaluate_AllQueryParamsAreValid_ReturnsOkWithJsonBody() throws Exception {
        when(polynomialServiceMock.evaluatePolynomial(anyString(), anyInt()))
                .thenReturn(polynomialResponseDto);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2*x-1")
                        .param("value", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rawPolynomial").value(RAW_POLYNOMIAL))
                .andExpect(jsonPath("$.simplifiedPolynomial").value(SIMPLIFIED_POLYNOMIAL))
                .andExpect(jsonPath("$.multipliedPolynomial").isEmpty())
                .andExpect(jsonPath("$.calculationResult").exists())
                .andExpect(jsonPath("$.calculationResult.value").value(VALUE))
                .andExpect(jsonPath("$.calculationResult.result").value(RESULT))
                .andExpect(jsonPath("$.calculationResult.calculationResultSource").value(EVALUATION.toString()));
    }

    @Test
    public void getApiPolynomialsEvaluate_QueryParamValueIsNotNumber_ReturnsBadRequestWithJsonBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .param("value", "sdsd")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_QueryParamValueIsAbsent_ReturnsBadRequestWithJsonBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_QueryParamPolynomialIsAbsent_ReturnsBadRequestWithJsonBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("value","1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_AllQueryParamsAreAbsent_ReturnsBadRequestWithJsonBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_AuthorizationHeaderIsAbsent_ReturnsUnauthorizedWithJsonBody() throws Exception {
        doThrow(new JwtAuthenticationFilterException("Authentication failure", new RuntimeException("Authorization Header is Absent")))
                .when(jwtAuthenticationFilterErrorHandlerMock).handleError();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .param("value", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_TokenIsExpired_ReturnsUnauthorizedWithJsonBody() throws Exception {
        doThrow(new JwtAuthenticationFilterException("Authentication failure", new RuntimeException("Token is Expired")))
                .when(jwtAuthenticationFilterErrorHandlerMock).handleError();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .param("value", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_EXPIRED)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_TokenIsEmpty_ReturnsUnauthorizedWithJsonBody() throws Exception {
        doThrow(new JwtAuthenticationFilterException("Authentication failure", new MissingJwtTokenException("Missing JWT token")))
                .when(jwtAuthenticationFilterErrorHandlerMock).handleError();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .param("value", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void getApiPolynomialsEvaluate_TokenIsMalformed_ReturnsUnauthorizedWithJsonBody() throws Exception {
        doThrow(new JwtAuthenticationFilterException("Authentication failure", new MalformedJwtException("JWT token is malformed")))
                .when(jwtAuthenticationFilterErrorHandlerMock).handleError();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/polynomials/evaluate")
                        .param("polynomial","x^2+2x-1")
                        .param("value", "1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_MALFORMED)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void deleteApiPolynomials_PolynomialIsExist_ReturnsOkWithJsonBody() throws Exception {
        polynomialResponseDto =
                new PolynomialResponseDto(RAW_POLYNOMIAL, SIMPLIFIED_POLYNOMIAL, null, null);

        when(polynomialServiceMock.deletePolynomial(anyString()))
                .thenReturn(polynomialResponseDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/polynomials")
                        .param("polynomial","x^2+2*x-1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.calculationResult").doesNotExist());
    }

    @Test
    public void deleteApiPolynomials_PolynomialIsNotExist_ReturnsNotFoundWithJsonBody() throws Exception {
        polynomialResponseDto =
                new PolynomialResponseDto(RAW_POLYNOMIAL, SIMPLIFIED_POLYNOMIAL, null, null);

        when(polynomialServiceMock.deletePolynomial(anyString()))
                .thenThrow(new PolynomialNotFoundException("Polynomial not found for deleting"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/polynomials")
                        .param("polynomial","x^2+2*x-1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void deleteApiPolynomials_PolynomialIsAbsent_ReturnsBadRequestWithJsonBody() throws Exception {
        polynomialResponseDto =
                new PolynomialResponseDto(RAW_POLYNOMIAL, SIMPLIFIED_POLYNOMIAL, null, null);

        when(polynomialServiceMock.deletePolynomial(anyString()))
                .thenThrow(new PolynomialNotFoundException("Polynomial not found for deleting"));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/polynomials")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN_IS_VALID)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.error").exists());
    }
}

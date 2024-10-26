package com.example.polynomial.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder(builderClassName = "Builder")
public record ErrorResponseDto(String error,
                               @JsonInclude(NON_NULL)
                               Map<String, String> details) {}

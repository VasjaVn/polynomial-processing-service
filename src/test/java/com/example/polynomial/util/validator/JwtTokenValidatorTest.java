package com.example.polynomial.util.validator;

import com.example.polynomial.config.app.props.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtTokenValidatorTest {

    public static final String JWT_SECRET_KEY = "038dd6994b3f8ecda910a3b215a9d09b3ecf8930440d0e72b225d39c1699175b1cbfccf6afe461920b529349c51f6f7aa822eee97e15489f7fea5aefbe84509e3e793903e51f6c87ee981cd92997f85838feef752d1b7d303ac0f5fa630606b2aa03c2bf8184782ff277ebeb025c5645f4c18cb6a1d8593c1d1e03358d51360145a42cdd39fff585f5d766451d4897add9b4ec280685d025bdb0c01da76291640e0225a40897c82023f1457f3563eabda6a77df6b629308a5e3bcbe068165d25eff61788eb9085dac41d8702ec4578e233bd18e4343f91fbef8b11b5652d7405066625303db0100db6d881bda82905721f5496ea3ad946801e327fcd8d5eb0f7";
    public static final String JWT_SECRET_KEY_IS_EMPTY = "";

    public static final String TOKEN_IS_VALID = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3Mjk1OTc4NjAsImV4cCI6ODA0MDk0NTA2MCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.tKB2fi9iA0YfrdBkztdUQkN0TAceQ8L8uP5cvLWQZSc";
    public static final String TOKEN_IS_EXPIRED = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE3Mjk1OTc4NjAsImV4cCI6MTY5Nzk3NTQ2MCwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.seRxbiIoQoluURkBJw15D8ThDAlJ5Xqe_bWusRrVs_Y";
    public static final String TOKEN_IS_MALFORMED = "AAAAAAAA.BBBBBBBB.CCCCCCC";
    public static final String TOKEN_IS_NULL = null;
    public static final String TOKEN_IS_EMPTY = "";

    private final JwtTokenValidator underTest =
            new JwtTokenValidator(new JwtProperties(JWT_SECRET_KEY));

    @Test
    public void givenValidToken_whenValidate_thenReturnTrue() {
        assertTrue(underTest.validate(TOKEN_IS_VALID));
    }

    @Test()
    public void givenCreateJwtTokenValidator_whenJwtPropertiesIsNull_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JwtTokenValidator(null);
        });
    }
    @Test()
    public void givenCreateJwtTokenValidator_whenSecretKeyIsNullInJwtProperties_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JwtTokenValidator(new JwtProperties(null));
        });
    }

    @Test()
    public void givenCreateJwtTokenValidator_whenSecretKeyIsEmptyInJwtProperties_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JwtTokenValidator(new JwtProperties(""));
        });
    }

    @Test
    public void givenExpiredToken_whenValidate_thenThrowsExpiredJwtException() {
        assertThrows(ExpiredJwtException.class, () -> {
            underTest.validate(TOKEN_IS_EXPIRED);
        });
    }

    @Test()
    public void givenMalformedToken_whenValidate_thenThrowsMalformedJwtException() {
        assertThrows(MalformedJwtException.class, () -> {
            underTest.validate(TOKEN_IS_MALFORMED);
        });
    }

    @Test()
    public void givenNullToken_whenValidate_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.validate(TOKEN_IS_NULL);
        });
    }

    @Test()
    public void givenEmptyToken_whenValidate_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.validate(TOKEN_IS_EMPTY);
        });
    }
}

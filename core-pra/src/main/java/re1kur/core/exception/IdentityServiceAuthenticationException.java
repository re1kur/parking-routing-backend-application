package re1kur.core.exception;

import lombok.Getter;

@Getter
public class IdentityServiceAuthenticationException extends RuntimeException {
    private final Integer httpStatusCode;
    private final String body;

    public IdentityServiceAuthenticationException(String body, Integer httpStatusCode) {
        super("Identity service authentication failed:\n%s [%d]".formatted(body, httpStatusCode));
        this.httpStatusCode = httpStatusCode;
        this.body = body;
    }
}

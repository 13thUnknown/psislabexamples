package info.stepanoff.trsis.samples.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class InternalApiException extends RuntimeException {

    private String code;
    private HttpStatus httpStatus;
    private Exception cause;
    private Map<String, Object> params = new HashMap<>();

    public InternalApiException(HttpStatus httpStatus, String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public InternalApiException(Exception cause, HttpStatus httpStatus, String code, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.cause = cause;
    }

    public InternalApiException withParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public InternalApiException withList(Collection collection) {
        params.put("list", collection);
        return this;
    }

    public Mono toMono() {
        return Mono.error(this);
    }

}

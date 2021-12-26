package info.stepanoff.trsis.samples.exception;

import reactor.core.publisher.Mono;

import java.text.MessageFormat;

public interface IApiError {

    default Mono toMono() {
        return Mono.error(getException());
    }

    default Mono toMonoWithFormatParams(Object... params) {
        return Mono.error(getExceptionWithFormatParams(params));
    }

    InternalApiException getException();

    InternalApiException getException(Exception cause);

    InternalApiException getExceptionWithFormatParams(Object... params);

    default String formatMessage(String message, Object... params) {
        if (params != null) {
            return MessageFormat.format(message, params);
        } else {
            return message;
        }
    }

}

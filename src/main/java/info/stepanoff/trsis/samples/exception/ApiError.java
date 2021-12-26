package info.stepanoff.trsis.samples.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiError implements IApiError {

    AUTHORIZATION_HEADER_NOT_FOUND("Заголовок Authorization не найден"),
    NOT_AUTHORIZED("Авторизация не удалась"),
    INVALID_LOGIN_OR_PASSWORD("Неправильный логин или пароль"),
    TOKEN_NOT_VALID(HttpStatus.UNAUTHORIZED, "Невалидный токен"),
    PARAMETER_NOT_CORRECT("Параметр {0} не корректный"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "{0} не найден"),
    NOT_FOUND_MANY(HttpStatus.NOT_FOUND, "{0} не найдены"),
    CANT_BE_NULL("{0} не может быть null"),
    ALREADY_EXISTS("{0} уже существует"),
    ERROR("{0}"),
    VALIDATION_ERROR("Ошибка валидации"),
    DUPLICATE_KEY("Дублирование сущности"),

    PASSWORDS_DONT_MATCH("Пароли не совпадают"),

    VERSION_IS_BUSY_FOR_RATE_NOTIFICATION("Версия МП уже занята другим уведомлением об оценке"),
    VERSION_INTERVALS_OVERLAP("Интервалы версий пересекаются");

    private final HttpStatus httpStatus;
    private final String errorCode = name();
    private final String message;

    ApiError(String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    @Override
    public InternalApiException getException() {
        return new InternalApiException(httpStatus, name(), message);
    }

    @Override
    public InternalApiException getException(Exception cause) {
        return new InternalApiException(cause, httpStatus, name(), message);
    }

    @Override
    public InternalApiException getExceptionWithFormatParams(Object... params) {
        return new InternalApiException(httpStatus, errorCode, formatMessage(message, params));
    }

}

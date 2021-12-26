package info.stepanoff.trsis.samples.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import info.stepanoff.trsis.samples.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@RestControllerAdvice
public class ReactiveGlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final DataBufferWriter bufferWriter;

    public ReactiveGlobalExceptionHandler(ObjectMapper objectMapper) {
        this.bufferWriter = new DataBufferWriter(objectMapper);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        ResponseEntity<ErrorDto> responseEntity = throwable(ex);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, responseEntity.getHeaders().getContentType().toString());
        response.setStatusCode(responseEntity.getStatusCode());
        return bufferWriter.write(exchange.getResponse(), responseEntity.getBody());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorDto> throwable(Throwable ex) {
        log.error(ex.toString(), ex);

        HttpStatus status;
        ErrorDto errorDto;

        if (ex instanceof InternalApiException) {
            InternalApiException e = (InternalApiException) ex;
            status = e.getHttpStatus();
            if (e.getCause() == null) {
                errorDto = new ErrorDto(e.getParams(), e.getCode(), e.getMessage());
            } else {
                errorDto = new ErrorDto(e.getParams(), e.getCode(), e.getMessage(), e.getCause().getMessage());
            }
        } else {
            status = HttpStatus.BAD_REQUEST;
            errorDto = new ErrorDto(ex.toString());
        }

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorDto);
    }

    private class DataBufferWriter {

        private final ObjectMapper objectMapper;

        public DataBufferWriter(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        public <T> Mono<Void> write(ServerHttpResponse httpResponse, T object) {
            return httpResponse
                    .writeWith(Mono.fromSupplier(() -> {
                        DataBufferFactory bufferFactory = httpResponse.bufferFactory();
                        try {
                            return bufferFactory.wrap(objectMapper.writeValueAsBytes(object));
                        } catch (Exception ex) {
                            log.warn("Error writing response", ex);
                            return bufferFactory.wrap(new byte[0]);
                        }
                    }));
        }
    }

}

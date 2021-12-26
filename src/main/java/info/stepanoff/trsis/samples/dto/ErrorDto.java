package info.stepanoff.trsis.samples.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorDto extends HashMap<String, Object> {

    public ErrorDto(String message) {
        put("message", message);
    }

    public ErrorDto(String code, String message) {
        put("code", code);
        put("message", message);
    }

    public ErrorDto(Map<String, Object> params, String code, String message) {
        putAll(params);
        put("code", code);
        put("message", message);
    }

    public ErrorDto(Map<String, Object> params, String code, String message, String cause) {
        putAll(params);
        put("code", code);
        put("message", message);
        put("cause", cause);
    }

}

package info.stepanoff.trsis.samples.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.stepanoff.trsis.samples.db.model.BaseLocalizationEntity;
import info.stepanoff.trsis.samples.db.model.Marker;
import info.stepanoff.trsis.samples.dto.LocalizationGetRequest;
import info.stepanoff.trsis.samples.exception.ApiError;
import info.stepanoff.trsis.samples.service.LocalizationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static info.stepanoff.trsis.samples.exception.ApiError.VALIDATION_ERROR;

@RestController
@RequestMapping("/localization")
@RequiredArgsConstructor
public class LocalizationController {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final ObjectMapper mapper;
    private final LocalizationService service;

    @PostMapping("/{regionCode}/{category}")
    Mono<? extends BaseLocalizationEntity> save(@RequestBody Map<String, Object> body, @PathVariable String regionCode, @PathVariable String category) {
        BaseLocalizationEntity entity = mapper.convertValue(body, entityClass(category));
        validate(entity);
        return service.create(entity, regionCode);
    }

    @GetMapping("/{regionCode}/{category}")
    Flux<? extends BaseLocalizationEntity> getList(@PathVariable String regionCode, @PathVariable String category,
                                                   @ParameterObject LocalizationGetRequest request) {
        return service.findByRegionCode(regionCode, entityClass(category), PageRequest.of(request.getPage(), request.getSize()));
    }

    @GetMapping("/{regionCode}/{category}/{mnemocode}")
    Mono<? extends BaseLocalizationEntity> get(@PathVariable String regionCode, @PathVariable String category,
                                               @PathVariable String mnemocode) {
        return service.findOne(mnemocode, entityClass(category), regionCode);
    }

    @DeleteMapping("/{regionCode}/{category}/{mnemocode}")
    Mono<Void> delete(@PathVariable String regionCode, @PathVariable String category,
                                               @PathVariable String mnemocode) {
        return service.delete(regionCode, mnemocode, entityClass(category));
    }

    private void validate(BaseLocalizationEntity entity) {
        Set<ConstraintViolation<BaseLocalizationEntity>> validate = validator.validate(entity);
        if (!validate.isEmpty()) {
            throw VALIDATION_ERROR.getExceptionWithFormatParams("Ошибка валидации").withList(validate.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.toList()));
        }
    }

    private Class<? extends BaseLocalizationEntity> entityClass(String category) {
        switch (category) {
            case "marker":
                return Marker.class;
            default:
                throw new RuntimeException("Unknown localization category");
        }
    }
}

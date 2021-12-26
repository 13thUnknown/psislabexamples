package info.stepanoff.trsis.samples.service;

import info.stepanoff.trsis.samples.db.dao.LocalizationRepository;
import info.stepanoff.trsis.samples.db.model.BaseLocalizationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface LocalizationService {


    <E extends BaseLocalizationEntity> Flux<E> findByRegionCode(String locale, Class<E> clazz, Pageable pageable);

    <E extends BaseLocalizationEntity> Mono<E> create(BaseLocalizationEntity entity, String locale);

    <E extends BaseLocalizationEntity> Mono<E> findOne(String mnemocode, Class<E> clazz, String locale);

    <E extends BaseLocalizationEntity> Mono<Void> delete(String locale, String mnemocode, Class<E> clazz);
}

package info.stepanoff.trsis.samples.service;

import info.stepanoff.trsis.samples.db.dao.LocalizationRepository;
import info.stepanoff.trsis.samples.db.model.BaseLocalizationEntity;
import info.stepanoff.trsis.samples.exception.ApiError;
import info.stepanoff.trsis.samples.service.db.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static info.stepanoff.trsis.samples.exception.ApiError.NOT_FOUND;
import static liquibase.ext.mongodb.lockservice.MongoChangeLogLock.Fields.id;


@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {

    private final LocalizationRepository repository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final ReactiveMongoTemplate mongoTemplate;
    public <E extends BaseLocalizationEntity> Flux<E> findByRegionCode(String locale, Class<E> clazz, Pageable pageable) {
        Query query = new Query()
                .with(pageable)
                .addCriteria(Criteria.where("locale").is(locale));
        return mongoTemplate.find(query, clazz);
    }

    public <E extends BaseLocalizationEntity> Mono<E> create(BaseLocalizationEntity entity, String locale) {
        return repository
                .findByLocaleAndMnemocode(locale, entity.getMnemocode(), entity.getClass().getName())
                .map(e -> entity.setId(e.getId()))
                .defaultIfEmpty(entity.setLocale(locale))
                .doOnNext(e -> {
                    if (e.getId() == null)
                        e.setId(sequenceGeneratorService.generateSequence(entity.sequenceName()));
                })
                .flatMap(e -> (Mono<? extends E>) repository.save(e));
    }

    @Override
    public <E extends BaseLocalizationEntity> Mono<E> findOne(String mnemocode, Class<E> clazz, String locale) {
        return (Mono<E>)repository.findByLocaleAndMnemocode(locale,mnemocode,clazz.getName())
                .switchIfEmpty(Mono.error(NOT_FOUND.getExceptionWithFormatParams(clazz.getSimpleName())));
    }

    @Override
    public <E extends BaseLocalizationEntity> Mono<Void> delete(String locale,String mnemocode, Class<E> clazz) {
        return repository.findByLocaleAndMnemocode(locale,mnemocode,clazz.getName())
                .switchIfEmpty(Mono.error(NOT_FOUND.getExceptionWithFormatParams(clazz.getSimpleName())))
                .flatMap(repository::delete);
    }
}

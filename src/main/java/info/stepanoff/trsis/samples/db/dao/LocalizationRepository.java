package info.stepanoff.trsis.samples.db.dao;

import info.stepanoff.trsis.samples.db.model.BaseLocalizationEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LocalizationRepository extends ReactiveMongoRepository<BaseLocalizationEntity, Long> {

     @Query("{ locale : ?0, mnemocode : ?1, _class : ?2}")
     <E extends BaseLocalizationEntity> Mono<E> findByLocaleAndMnemocode(String locale, String mnemocode, String eClass);

     @Query("{ locale : ?0, _class : ?1}")
     <E extends BaseLocalizationEntity> Flux<E> findByLocale(String locale, String eClass);

}

package info.stepanoff.trsis.samples.db.dao;

import info.stepanoff.trsis.samples.db.model.School;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Pavel
 */
@Repository
@Transactional(propagation=Propagation.REQUIRED)
public interface SchoolRepository extends CrudRepository<School, Integer> {

    // List<School> findByNumber(Integer number);
}

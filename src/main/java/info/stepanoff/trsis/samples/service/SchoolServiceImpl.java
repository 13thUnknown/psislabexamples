package info.stepanoff.trsis.samples.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import info.stepanoff.trsis.samples.db.dao.SchoolRepository;
import info.stepanoff.trsis.samples.db.dto.SchoolDTO;
import info.stepanoff.trsis.samples.db.model.School;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.StreamUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public Iterable<SchoolDTO> listAll() {

        return StreamUtils.createStreamFromIterator(
                schoolRepository.findAll().iterator())
                .map(x -> new SchoolDTO(x))
                .collect(Collectors.toList());

    }

    @Override
    public void delete(Integer id) {
        try {
            schoolRepository.deleteById(id);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            //for the reason of idempotency leave this blank
        }
    }

    @Override
    public SchoolDTO add(Integer number, String name) {
        return new SchoolDTO(schoolRepository.save(new School(number, name)));
    }

    @Override
    public SchoolDTO getById(Integer id) {
        School school = schoolRepository.findById(id).orElse(null);
        if (school != null)
            return new SchoolDTO(school);
        else return null;
    }

}

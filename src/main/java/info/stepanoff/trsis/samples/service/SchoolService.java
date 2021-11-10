package info.stepanoff.trsis.samples.service;

import info.stepanoff.trsis.samples.db.dto.SchoolDTO;

public interface SchoolService {

    Iterable<SchoolDTO> listAll();

    void delete(Integer id);
    
    SchoolDTO add(Integer number, String name);
    
    SchoolDTO getById(Integer id);

}

package info.stepanoff.trsis.samples.rest;

import info.stepanoff.trsis.samples.db.dto.SchoolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import info.stepanoff.trsis.samples.service.SchoolService;
import java.security.Principal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/public/rest/schools")
public class SchoolRestService {

    @Autowired
    private SchoolService schoolService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Flux<Object> browse() {
        return Flux.defer(() -> Flux.fromIterable(schoolService.listAll()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Mono<Object> delete(@PathVariable("id") Integer id, Principal principal) {

        if (principal == null) {
            Exception ex = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User identity not provided");
            log.error("Unauthorized request to delete method");
            return Mono.error(ex);
            
        } 
        
        return Mono.defer(() -> {
            try {
                schoolService.delete(id);
                return Mono.empty();
            } catch (Exception e) {
                Exception ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "School with id=" + id + " not found and could not be deleted");
                return Mono.error(ex);
            }
        });
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Mono<Object> getOne(@PathVariable("id") Integer id) {

        return Mono.defer(() -> {
            SchoolDTO schoolById = schoolService.getById(id);
            if (schoolById == null) {
                Exception ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "School with id=" + id + " not found");
                return Mono.error(ex);
            } else {
                return Mono.just(schoolById);
            }
        });
    }

    @RequestMapping(value = "/{number}/{name}", method = RequestMethod.POST)
    public Mono<Object> add(@PathVariable("number") Integer number, @PathVariable("name") String name) {
        return Mono.defer(() -> Mono.just(schoolService.add(number, name)));
    }

}

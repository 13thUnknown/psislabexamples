package info.stepanoff.trsis.samples.rest;

import info.stepanoff.trsis.samples.db.dao.LocalizationRepository;
import info.stepanoff.trsis.samples.exception.ApiError;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureDataMongo
@AutoConfigureWebTestClient
class ApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private LocalizationRepository repository;

    @BeforeEach
    void beforeEach(){
        repository.deleteAll();
    }

    @Test
    @DisplayName("Testing save localization")
    void testSave() {
        Map<String, Object> body = new HashMap<>();
        body.put("title","Some value");
        body.put("description", "some desc");
        body.put("mnemocode", "test");
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body), new ParameterizedTypeReference<>(){})
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<>(){})
                .consumeWith(response -> {
                    Map<String,String> responseBody = (Map<String, String>) response.getResponseBody();
                    assertThat(responseBody.get("title"), equalTo(body.get("title")));
                    assertThat(responseBody.get("description"), equalTo(body.get("description")));
                });
    }

    @Test
    @DisplayName("Testing validation with 400")
    void testSaveFailed() {
        Map<String, Object> body = new HashMap<>();
        body.put("title","Some value");
        body.put("description", "some desc");
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body), new ParameterizedTypeReference<>(){})
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(new ParameterizedTypeReference<>(){})
                .consumeWith(response -> {
                    Map<String,String> responseBody = (Map<String, String>) response.getResponseBody();
                    assertThat(responseBody.get("message"), equalTo(ApiError.VALIDATION_ERROR.getMessage()));
                });
    }

    @Test
    @DisplayName("Testing get localization")
    void testGet() {
        Map<String, Object> body = new HashMap<>();
        body.put("title","Some value");
        body.put("description", "some desc");
        body.put("mnemocode", "test");
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body), new ParameterizedTypeReference<>(){})
                .exchange();
        webClient.get()
                .uri("/localization/ru/marker/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<>() {})
                .consumeWith(response ->{
                    Map<String,String> responseBody = (Map<String, String>) response.getResponseBody();
                    assertThat(responseBody.get("title"), equalTo(body.get("title")));
                    assertThat(responseBody.get("description"), equalTo(body.get("description")));
                });
    }

    @Test
    @DisplayName("Testing get localization with 404 error")
    void testGet404() {
        webClient.get()
                .uri("/localization/ru/marker/notfound")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(new ParameterizedTypeReference<>() {})
                .consumeWith(response ->{
                    Map<String,String> responseBody = (Map<String, String>) response.getResponseBody();
                    assert responseBody != null;
                    assertThat(responseBody.get("message"), equalTo("Marker не найден"));
                });
    }

    @Test
    @DisplayName("Testing deleting localization")
    void testDelete() {
        Map<String, Object> body = new HashMap<>();
        body.put("title","Some value");
        body.put("description", "some desc");
        body.put("mnemocode", "test");
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body), new ParameterizedTypeReference<>(){})
                .exchange();
        webClient.delete()
                .uri("/localization/ru/marker/test")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Testing deleting localization with 404 error")
    void testDeleteWith404() {
        Map<String, Object> body = new HashMap<>();
        body.put("title","Some value");
        body.put("description", "some desc");
        body.put("mnemocode", "test");
        webClient.delete()
                .uri("/localization/ru/marker/notfound")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Testing get paginated")
    void testGetPagination() {
        Map<String, Object> body1 = new HashMap<>();
        body1.put("title","Some value");
        body1.put("description", "some desc");
        body1.put("mnemocode", "test");
        Map<String, Object> body2 = new HashMap<>();
        body2.put("title","Some value2");
        body2.put("description", "some desc2");
        body2.put("mnemocode", "test2");
        Map<String, Object> body3 = new HashMap<>();
        body3.put("title","Some value3");
        body3.put("description", "some desc3");
        body3.put("mnemocode", "test3");
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body1), new ParameterizedTypeReference<>(){})
                .exchange();
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body2), new ParameterizedTypeReference<>(){})
                .exchange();
        webClient.post()
                .uri("/localization/ru/marker")
                .body(Mono.just(body3), new ParameterizedTypeReference<>(){})
                .exchange();

        webClient.get()
                .uri("/localization/ru/marker?page=0&size=20")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<>() {})
                .consumeWith(response ->{
                    List<Object> responseBody = (List<Object>) response.getResponseBody();
                    assertThat(responseBody.size(),equalTo(3));
                });
    }


//    @Test
//    @DisplayName("Testing get movie by id")
//    void testGet() {
//        var movie = new CreateMovieDto("name", LocalDate.now(), List.of());
//        var createdMovie = webClient.post()
//                .uri("/api/v1/movie")
//                .body(Mono.just(movie), CreateMovieDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(createdMovie).isNotNull();
//        assertThat(createdMovie.getId()).isNotNull();
//
//        webClient.get()
//                .uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .consumeWith(response -> assertThat(createdMovie).isEqualTo(response.getResponseBody()));
//    }
//
//    @Test
//    @DisplayName("Testing get movie by id with expected 404")
//    void testGetWithNotFound() {
//
//        var errorMessage = "Movie not found!";
//        webClient.get()
//                .uri("/api/v1/movie/not_valid_id")
//                .exchange()
//                .expectStatus()
//                .isNotFound()
//                .expectBody(ErrorCode.class)
//                .consumeWith(result -> {
//                    var errorCode = result.getResponseBody();
//                    assertThat(errorCode).isNotNull();
//                    assertThat(errorMessage).isEqualTo(errorCode.getMessage());
//                });
//    }
//
//    @Test
//    @DisplayName("Testing get all movie")
//    void testGetAll() {
//
//        var moviesBeforeAdd = webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/api/v1/movie")
//                        .queryParam("pageNum", 0)
//                        .queryParam("pageSize", 20)
//                        .build())
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(GetMovieDto.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(moviesBeforeAdd).isNotNull();
//
//        var movie = new CreateMovieDto("name", LocalDate.now(), List.of());
//        var createdMovie = webClient.post()
//                .uri("/api/v1/movie")
//                .body(Mono.just(movie), CreateMovieDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(createdMovie).isNotNull();
//        assertThat(createdMovie.getId()).isNotNull();
//
//        webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/api/v1/movie")
//                        .queryParam("pageNum", 0)
//                        .queryParam("pageSize", 20)
//                        .build())
//                .exchange()
//                .expectStatus().isOk()
//                .expectBodyList(GetMovieDto.class)
//                .consumeWith(result -> {
//                    var list = result.getResponseBody();
//                    assertThat(list).isNotNull();
//                    assertThat(list.size()).isEqualTo(moviesBeforeAdd.size() + 1);
//                });
//
//    }
//
//    @Test
//    @DisplayName("Testing update movie by id")
//    void testUpdate() {
//        var movie = new CreateMovieDto("name", LocalDate.now(), List.of());
//        var createdMovie = webClient.post()
//                .uri("/api/v1/movie")
//                .body(Mono.just(movie), CreateMovieDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(createdMovie).isNotNull();
//        assertThat(createdMovie.getId()).isNotNull();
//
//        webClient.get()
//                .uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .consumeWith(response -> assertThat(createdMovie).isEqualTo(response.getResponseBody()));
//
//        var createMovieForUpdate = new CreateMovieDto("name2", LocalDate.now().plusYears(1L), List.of());
//        var updatedMovie = webClient.put()
//                .uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .body(Mono.just(createMovieForUpdate), CreateMovieDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .returnResult().getResponseBody();
//
//        webClient.get()
//                .uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .consumeWith(response -> assertThat(updatedMovie).isEqualTo(response.getResponseBody()));
//    }
//
//    @Test
//    @DisplayName("Testing delete movie")
//    void testDelete() {
//        var movie = new CreateMovieDto("name", LocalDate.now(), List.of());
//        var createdMovie = webClient.post()
//                .uri("/api/v1/movie")
//                .body(Mono.just(movie), CreateMovieDto.class)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(GetMovieDto.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(createdMovie).isNotNull();
//        assertThat(createdMovie.getId()).isNotNull();
//
//        webClient.delete().uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .exchange()
//                .expectStatus()
//                .isOk();
//
//        webClient.get()
//                .uri(String.format("/api/v1/movie/%s", createdMovie.getId()))
//                .exchange()
//                .expectStatus()
//                .isNotFound();
//    }
//
//    @Test
//    @DisplayName("Testing delete not existing movie")
//    void testDeleteNotExistingMovie() {
//        webClient.get()
//                .uri("/api/v1/movie/not_existing_movie")
//                .exchange()
//                .expectStatus()
//                .isNotFound();
//    }

}

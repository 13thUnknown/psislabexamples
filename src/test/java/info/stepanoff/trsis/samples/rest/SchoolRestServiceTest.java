/*
 * this code is available under GNU GPL v3
 * https://www.gnu.org/licenses/gpl-3.0.en.html
 */
package info.stepanoff.trsis.samples.rest;

import lombok.extern.slf4j.Slf4j;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@Slf4j
public class SchoolRestServiceTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testPublicRestSchools() throws Exception {

        webClient.get().uri("/public/rest/schools")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].number").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("School 1")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].number").isEqualTo(2)
                .jsonPath("$[1].name").isEqualTo("School 2")
                .jsonPath("$[2].id").isEqualTo(3)
                .jsonPath("$[2].number").isEqualTo(3)
                .jsonPath("$[2].name").isEqualTo("School 3")
                .jsonPath("$[3].id").isEqualTo(4)
                .jsonPath("$[3].number").isEqualTo(4)
                .jsonPath("$[3].name").isEqualTo("School 4")
                .jsonPath("$[4].id").isEqualTo(5)
                .jsonPath("$[4].number").isEqualTo(5)
                .jsonPath("$[4].name").isEqualTo("School 5");
    }

    @Test
    @WithMockUser(value = "Test User", username = "admin")
    public void testRestSchoolsAddDelete() throws Exception {

        webClient.post().uri("/public/rest/schools/10/mockschool").exchange().expectStatus().isOk();
                
        webClient.get().uri("/public/rest/schools").exchange().expectStatus().isOk()
                .expectBody()                
                .jsonPath("$[5].id").isEqualTo(6)
                .jsonPath("$[5].number").isEqualTo(10)
                .jsonPath("$[5].name").isEqualTo("mockschool")
                .jsonPath("$", hasSize(6));

        webClient.delete().uri("/public/rest/schools/6").exchange().expectStatus().isOk();        

        webClient.get().uri("/public/rest/schools").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$", hasSize(5));

    }

    @Test
    @WithMockUser(value = "Test User", username = "admin")
    public void testRestSchoolsDeleteNonExisting() throws Exception {
        webClient.delete().uri("/public/rest/schools/10").exchange().expectStatus().isNotFound();        
    }
    
    @Test    
    public void testRestSchoolsDeleteUnauthentificated() throws Exception {
        webClient.delete().uri("/public/rest/schools/10").exchange().expectStatus().isUnauthorized();        
    }
}

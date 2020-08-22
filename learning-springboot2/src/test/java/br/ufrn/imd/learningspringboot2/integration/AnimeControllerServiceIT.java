package br.ufrn.imd.learningspringboot2.integration;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.repository.AnimeRepository;
import br.ufrn.imd.learningspringboot2.util.AnimeCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimeControllerServiceIT {

    @Autowired
    @Qualifier(value = "testRestTemplateUser")
    private TestRestTemplate testRestTemplateUser;


    @Autowired
    @Qualifier(value = "testRestTemplateAdmin")
    private TestRestTemplate testRestTemplateAdmin;


//    @LocalServerPort
//    private Integer port;

    @MockBean
    private AnimeRepository animeRepositoryMock;

    @Lazy
    @TestConfiguration
    static class Config {
        @Bean(name = "testRestTemplateUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devdojo", "academy");

            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("samuel", "academy");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @BeforeEach
    public void setUp() {
        PageImpl<Anime> animePages = new PageImpl<>(List.of(AnimeCreator.creatorValidAnime()));
        when(animeRepositoryMock.findAll(any(PageRequest.class)))
                .thenReturn(animePages);

        when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Optional.of(AnimeCreator.creatorValidAnime()));

        when(animeRepositoryMock.findByName(any()))
                .thenReturn(List.of(AnimeCreator.creatorValidAnime()));

        when(animeRepositoryMock.save(AnimeCreator.creatorAnimeToBeSaved()))
                .thenReturn(AnimeCreator.creatorValidAnime());


        doNothing().when(animeRepositoryMock).delete(any(Anime.class));

        when(animeRepositoryMock.save(AnimeCreator.creatorValidUpdatedAnime()))
                .thenReturn(AnimeCreator.creatorValidUpdatedAnime());
    }

//    @Test
//    @DisplayName("listAll returns a pageable list of animes when successful")
//    public void listAll_ReturnListOfAnimesInsidePageableObject_WhenSuccessful() {
//
//        String expectedName = AnimeCreator.creatorValidAnime().getName();
//
//        // @formatter:off
//        Page<Anime> animePage = testRestTemplate.exchange("/animes ", HttpMethod.GET, null,
//                new ParameterizedTypeReference<PageableResponse<Anime>>() {}).getBody();
//        //
//
//
//        assertThat(animePage).isNotNull();
//        assertThat(animePage.toList()).isNotEmpty();
//        assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
//
//    }

    @Test
    @DisplayName("findById returns a anime when successul")
    public void findById_ReturnAnimeInsidePageableObject_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime resultAnime = testRestTemplateUser.getForObject("/animes/{id}", Anime.class, 1);

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);
    }
//    @Test
//    @DisplayName("findByName returns a list of animes when successful")
//    public void findByName_ReturnListOfAnimes_WhenSuccessful() {
//        String expectedName = AnimeCreator.creatorValidAnime().getName();
//
//        List<Anime> resultAnimeList = testRestTemplate.exchange("/animes/find?name={name}", HttpMethod.GET,
//                new HttpEntity<>("Dragon Ball Z"),
//                new ParameterizedTypeReference<List<Anime>>() {
//        }).getBody();
//
//
//        assertThat(resultAnimeList).isNotNull();
//        assertThat(resultAnimeList).isNotEmpty();
//        assertThat(resultAnimeList.get(0).getName()).isEqualTo(expectedName);
//
//    }

    @Test
    @DisplayName("save creates anime when successful")
    public void save_CreatesAnime_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.creatorAnimeToBeSaved();

        Anime resultAnime = testRestTemplateUser.exchange("/animes", HttpMethod.POST, new HttpEntity<>(animeToBeSaved),
                new ParameterizedTypeReference<Anime>() {
            }).getBody();

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete Remove the anime when successful")
    public void delete_removeAnime_WhenSuccessful() {

        ResponseEntity<Void> responseEntity = testRestTemplateAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE,
                null, Void.class, 1);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }
    @Test
    @DisplayName("delete return erro 403 when user is not admin")
    public void delete_returnForbidden_WhenUserIsNotAdmin() {

        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/animes/admin/{id}", HttpMethod.DELETE,
                null, Void.class, 1);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("save Updates anime when successful")
    public void save_UpdatesAnime_WhenSuccessful() {

        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(AnimeCreator.creatorValidUpdatedAnime()), Void.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }
}
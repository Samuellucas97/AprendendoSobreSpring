package br.ufrn.imd.learningspringboot2.controller;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.service.AnimeService;
import br.ufrn.imd.learningspringboot2.util.AnimeCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

//@SpringBootTest
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    public void setUp() {

        PageImpl<Anime> animePages = new PageImpl<>(List.of(AnimeCreator.creatorValidAnime()));
        when(animeServiceMock.listAll(any()))
                .thenReturn(animePages);
        when(animeServiceMock.findByName(any()))
                .thenReturn(List.of(AnimeCreator.creatorValidAnime()));

        when(animeServiceMock.findById(any()))
                .thenReturn(AnimeCreator.creatorValidAnime());

        when(animeServiceMock.save(AnimeCreator.creatorAnimeToBeSaved()))
                .thenReturn(AnimeCreator.creatorValidAnime());

        doNothing().when(animeServiceMock).delete(any());

        when(animeServiceMock.save(AnimeCreator.creatorValidAnime()))
                .thenReturn(AnimeCreator.creatorValidUpdatedAnime());

    }

    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageableObject_WhenSuccessful() {

        String expectedName = AnimeCreator.creatorValidAnime().getName();

        Page<Anime> animePage = animeController.listAll(null).getBody();

        assertThat(animePage).isNotNull();
        assertThat(animePage.toList()).isNotEmpty();
        assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @Disabled
    @DisplayName("findById returns a anime when successul")
    public void findById_ReturnAnimeInsidePageableObject_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime resultAnime = animeController.findById(1, null).getBody();

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);

    }
    @Test
    @DisplayName("findByName returns a list of animes when successful")
    public void findByName_ReturnListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.creatorValidAnime().getName();

        List<Anime> animeList = animeController.findByName("Dragon Ball Z").getBody();

        assertThat(animeList).isNotNull();
        assertThat(animeList).isNotEmpty();
        assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save creates anime when successful")
    public void save_CreatesAnime_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.creatorAnimeToBeSaved();

        Anime resultAnime = animeController.save(animeToBeSaved).getBody();

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete Remove the anime when successful")
    public void delete_removeAnime_WhenSuccessful() {

        ResponseEntity<Void> responseEntity = animeController.delete(1);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    @DisplayName("save Updates anime when successful")
    public void save_UpdatesAnime_WhenSuccessful() {

        ResponseEntity<Void> responseEntity = animeController.update(AnimeCreator.creatorValidUpdatedAnime());

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }
}
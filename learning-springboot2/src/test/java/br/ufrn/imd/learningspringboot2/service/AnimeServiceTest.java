package br.ufrn.imd.learningspringboot2.service;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.exception.ResourceNotFoundException;
import br.ufrn.imd.learningspringboot2.repository.AnimeRepository;
import br.ufrn.imd.learningspringboot2.util.AnimeCreator;
import br.ufrn.imd.learningspringboot2.util.Utils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @Mock
    private Utils utilsMock;

    @BeforeEach
    public void setUp() {
        PageImpl<Anime> animePages = new PageImpl<>(List.of(AnimeCreator.creatorValidAnime()));
        when(animeRepositoryMock.findAll(any(PageRequest.class)))
                .thenReturn(animePages);


        when(animeRepositoryMock.findById(anyInt()))
                .thenReturn(Optional.of(AnimeCreator.creatorValidAnime()));

        when(utilsMock.findAnimeOrThrowNotFound(anyInt(), any(AnimeRepository.class) ))
                .thenReturn(AnimeCreator.creatorValidAnime());

        when(animeRepositoryMock.findByName(any()))
                .thenReturn(List.of(AnimeCreator.creatorValidAnime()));

        when(animeRepositoryMock.save(AnimeCreator.creatorAnimeToBeSaved()))
                .thenReturn(AnimeCreator.creatorValidAnime());


        doNothing().when(animeRepositoryMock).delete(any(Anime.class));

        when(animeRepositoryMock.save(AnimeCreator.creatorValidUpdatedAnime()))
                .thenReturn(AnimeCreator.creatorValidUpdatedAnime());
    }
    @Test
    @DisplayName("listAll returns a pageable list of animes when successful")
    public void listAll_ReturnListOfAnimesInsidePageableObject_WhenSuccessful() {

        String expectedName = AnimeCreator.creatorValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1,1));

        assertThat(animePage).isNotNull();
        assertThat(animePage.toList()).isNotEmpty();
        assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById returns a anime when successul")
    public void findById_ReturnAnimeInsidePageableObject_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime resultAnime = animeService.findById(1);

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);

    }
    @Test
    @DisplayName("findByName returns a list of animes when successful")
    public void findByName_ReturnListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.creatorValidAnime().getName();

        List<Anime> animeList = animeService.findByName("Dragon Ball Z");

        assertThat(animeList).isNotNull();
        assertThat(animeList).isNotEmpty();
        assertThat(animeList.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save creates anime when successful")
    public void save_CreatesAnime_WhenSuccessful() {
        Integer expectedId = AnimeCreator.creatorValidAnime().getId();

        Anime animeToBeSaved = AnimeCreator.creatorAnimeToBeSaved();

        Anime resultAnime = animeService.save(animeToBeSaved);

        assertThat(resultAnime).isNotNull();
        assertThat(resultAnime.getId()).isNotNull();
        assertThat(resultAnime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("delete Remove the anime when successful")
    public void delete_removeAnime_WhenSuccessful() {


        Assertions.assertThatCode(() -> animeService.delete(1))
        .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete Throws ResourceNotFoundException when the anime doesn't exist")
    public void delete_ThrowsResourceNotFoundException_WhenAnimeDoesNotExist() {

        when(utilsMock.findAnimeOrThrowNotFound(anyInt(), any(AnimeRepository.class)))
            .thenThrow( new ResourceNotFoundException("Anime not found"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> animeService.delete(2));

    }

    @Test
    @DisplayName("save Updates anime when successful")
    public void save_UpdatesAnime_WhenSuccessful() {

        Anime validUpdatedAnime = AnimeCreator.creatorValidUpdatedAnime();
        String expectedName = validUpdatedAnime.getName();

        Anime anime = animeService.save(AnimeCreator.creatorValidUpdatedAnime());

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull();
        Assertions.assertThat(anime.getName()).isEqualTo(expectedName);
    }
}
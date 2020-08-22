package br.ufrn.imd.learningspringboot2.repository;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Anime Repository Tests")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Saved creates anime when successful")
    public void save_PersistAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.creatorAnimeToBeSaved();

        Anime savedAnime = this.animeRepository.save(anime);

        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
    }


    @Test
    @DisplayName("Saved creates anime when successful")
    public void save_UpdatedAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.creatorAnimeToBeSaved();;
        Anime savedAnime = this.animeRepository.save(anime);
        savedAnime.setName("That time i got reincarneted as a slime");

        Anime updatedAnime = this.animeRepository.save(savedAnime);

        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("Deleted removes anime when successful")
    public void delete_RemoveAnime_WhenSuccessful(){
        Anime anime = AnimeCreator.creatorAnimeToBeSaved();;
        Anime savedAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(anime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());
        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find by name returns anime when successful")
    public void findByName_ReturnAnime_WhenSuccesfull() {
        Anime anime = AnimeCreator.creatorAnimeToBeSaved();
        Anime savedAnime = this.animeRepository.save(anime);
        String nameAnime = savedAnime.getName();

        List<Anime> animeList = this.animeRepository.findByName(nameAnime);

        Assertions.assertThat(animeList).isNotEmpty();
        Assertions.assertThat(animeList).contains(savedAnime);
    }

    @Test
    @DisplayName("Find by name returns empty list when no anime is found")
    public void findByName_ReturnEmptyList_WhenAnimeIsNotFound() {
        String nameAnime = "fake-name";

        List<Anime> animeList = this.animeRepository.findByName(nameAnime);

        Assertions.assertThat(animeList).isEmpty();
    }



    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    public void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();

//        Assertions.assertThatThrownBy( () -> animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        // Another alternative

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> animeRepository.save(anime))
                .withMessageContaining("The name anime cannot be empty");

    }

}
package br.ufrn.imd.learningspringboot2.util;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.exception.ResourceNotFoundException;
import br.ufrn.imd.learningspringboot2.repository.AnimeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {
    public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public Anime findAnimeOrThrowNotFound(Integer id, AnimeRepository animeRepository) {
        return animeRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Anime not found"));
    }
}

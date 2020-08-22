package br.ufrn.imd.learningspringboot2.repository;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Integer> {
    List<Anime> findByName(String name);
}

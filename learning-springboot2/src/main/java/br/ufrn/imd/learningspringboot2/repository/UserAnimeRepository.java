package br.ufrn.imd.learningspringboot2.repository;

import br.ufrn.imd.learningspringboot2.domain.UserAnime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnimeRepository extends JpaRepository<UserAnime, Integer> {
    UserAnime findByUsername(String username);

}
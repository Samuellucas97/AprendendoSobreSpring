package br.ufrn.imd.learningspringboot2.service;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.repository.AnimeRepository;
import br.ufrn.imd.learningspringboot2.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final Utils utils;
    private final AnimeRepository animeRepository;

    public Page<Anime> listAll(Pageable pageable ) {
        return this.animeRepository.findAll(pageable);
    }

//    public List<Anime> listAll() {
//        return this.animeRepository.findAll();
//    }

    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findById(Integer id) {
        return utils.findAnimeOrThrowNotFound(id, this.animeRepository);
    }


    @Transactional /// Transional define o método como uma operação atômica
    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public void delete(Integer id) {
        animeRepository.delete(utils.findAnimeOrThrowNotFound(id, this.animeRepository));
    }

    public void update(Anime anime) {
        animeRepository.save(anime);
    }


}
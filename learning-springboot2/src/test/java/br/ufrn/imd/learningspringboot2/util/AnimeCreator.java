package br.ufrn.imd.learningspringboot2.util;

import br.ufrn.imd.learningspringboot2.domain.Anime;

public class AnimeCreator {
    public static Anime creatorAnimeToBeSaved() {
        return Anime.builder().
                name("Tensei Shitara Slime Datta Ken")
                .build();
    }
    public static Anime creatorValidAnime() {
        return Anime.builder()
                .name("Tensei Shitara Slime Datta Ken")
                .id(1)
                .build();
    }
    public static Anime creatorValidUpdatedAnime() {
        return Anime.builder()
                .name("Tensei Shitara Slime Datta Ken")
                .id(1)
                .build();
    }
}
package br.ufrn.imd.learningspringboot2.controller;

import br.ufrn.imd.learningspringboot2.domain.Anime;
import br.ufrn.imd.learningspringboot2.service.AnimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

//    @GetMapping
//    public ResponseEntity<List<Anime>> listAll() {
//        return ResponseEntity.ok(animeService.listAll());
//    }
    @GetMapping
    @Operation(summary = "List all animes paginated and sorted", description = "to use pagination add sort parameters",
    tags = {"anime"})
     public ResponseEntity<Page<Anime>> listAll( Pageable pageable) {
        return ResponseEntity.ok(animeService.listAll(pageable));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(value = "name", required = true) String name) {
        return ResponseEntity.ok(animeService.findByName(name));
    }

    @GetMapping(path = "/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> findById(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
            log.info("User details {}", userDetails);
        return ResponseEntity.ok(animeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid Anime anime) {
        return ResponseEntity.ok(animeService.save(anime));
     }

     @DeleteMapping(path = "/admin/{id}")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "204", description = "Successful operation"),
             @ApiResponse(responseCode = "404", description = "Anime not found")
     })
//     @PreAuthorize("hasRole('ADMIN')")
     public ResponseEntity<Void> delete(@PathVariable Integer id) {
         animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }

     @PutMapping
     public ResponseEntity<Void> update(@RequestBody Anime anime) {
        animeService.update(anime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
     }
}
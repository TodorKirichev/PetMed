package com.petMed.web;

import com.petMed.pet.service.BreedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/breeds")
public class BreedControllerREST {

    private final BreedService breedService;

    public BreedControllerREST(BreedService breedService) {
        this.breedService = breedService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getBreeds(@RequestParam String species) {
        List<String> breeds = breedService.getBreedsBySpecies(species);
        return ResponseEntity.ok(breeds);
    }
}
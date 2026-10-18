package com.noSQL.claseJava.controllers;

import com.noSQL.claseJava.dto.FavoriteRequest;
import com.noSQL.claseJava.entity.Favorite;
import com.noSQL.claseJava.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.getAllFavorites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable String id) {
        Optional<Favorite> favorite = favoriteService.getFavoriteById(id);
        return favorite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<List<Favorite>> getFavoriteByUserId(@PathVariable String userId) {
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping
    public ResponseEntity<Favorite> createFavorite(@RequestBody FavoriteRequest request) {
        Favorite favoriteCreated = favoriteService.createFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Favorite> updateFavorite(@PathVariable String id, @RequestBody FavoriteRequest request) {
        Favorite updatedFavorite = favoriteService.updateFavorite(id, request);
        if (updatedFavorite != null) {
            return ResponseEntity.ok(updatedFavorite);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable String id) {
        boolean deleted = favoriteService.deleteFavorite(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

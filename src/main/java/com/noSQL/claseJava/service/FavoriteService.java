package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.FavoriteRequest;
import com.noSQL.claseJava.entity.Favorite;
import com.noSQL.claseJava.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public Favorite createFavorite(FavoriteRequest request) {
        Favorite favorite = Favorite.builder()
                .user(request.getUser())
                .favorite(request.getFavorite())
                .build();
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    public Optional<Favorite> getFavoriteById(String id) {
        return favoriteRepository.findById(id);
    }

    public List<Favorite> getFavoritesByUserId(String userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public Favorite updateFavorite(String id, FavoriteRequest request) {
        Optional<Favorite> existingFavorite = favoriteRepository.findById(id);
        if (existingFavorite.isPresent()) {
            Favorite favorite = existingFavorite.get();
            favorite.setUser(request.getUser());
            favorite.setFavorite(request.getFavorite());
            return favoriteRepository.save(favorite);
        }
        return null;
    }

    public boolean deleteFavorite(String id) {
        if (favoriteRepository.existsById(id)) {
            favoriteRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

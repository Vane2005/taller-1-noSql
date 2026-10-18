package com.noSQL.claseJava.dto;

import com.noSQL.claseJava.entity.Favorite.favoriteList;
import com.noSQL.claseJava.entity.Favorite.userReference;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FavoriteRequest {

    private userReference user;
    private favoriteList favorite;

}

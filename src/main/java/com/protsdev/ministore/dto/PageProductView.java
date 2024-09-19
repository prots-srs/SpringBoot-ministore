package com.protsdev.ministore.dto;

public record PageProductView(
        Long id,
        String name,
        String price,
        String picture,
        String pictureAlt) {

}

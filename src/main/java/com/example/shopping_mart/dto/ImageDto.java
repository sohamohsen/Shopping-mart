package com.example.shopping_mart.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String downLoadUrl;

    public ImageDto(Long imageId, String imageName, String downLoadUrl) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.downLoadUrl = downLoadUrl;
    }
}

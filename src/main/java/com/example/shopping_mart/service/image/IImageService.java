package com.example.shopping_mart.service.image;

import com.example.shopping_mart.dto.ImageDto;
import com.example.shopping_mart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {

    Image getImageById(long id);
    void deleteImageById(long id);
    List <ImageDto> saveImage(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId) throws IOException;

}

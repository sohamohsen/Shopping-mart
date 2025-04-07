package com.example.shopping_mart.service.image;

import com.example.shopping_mart.dto.ImageDto;
import com.example.shopping_mart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IImageService {

    Image getImageById(long id);
    void deleteImageById(long id);
    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

    //    @Override
    //    public void updateImage(MultipartFile file, Long imageId) {
    //        Image image = getImageById(imageId);
    //        try {
    //            image.setFileName(file.getOriginalFilename());
    //            image.setFileType(file.getContentType());
    //            image.setImage(new SerialBlob(file.getBytes()));
    //            imageRepository.save(image);
    //        } catch (IOException | SQLException e) {
    //            throw new RuntimeException(e.getMessage());
    //        }
    //
    //    }
    void updateImage(MultipartFile file, Long imageId);


    //    @Override
    //    public void updateImage(MultipartFile file, Long imageId) {
    //        Image image = getImageById(imageId);
    //        try {
    //            image.setFileName(file.getOriginalFilename());
    //            image.setFileType(file.getContentType());
    //            image.setImage(new SerialBlob(file.getBytes()));
    //            imageRepository.save(image);
    //        } catch (IOException | SQLException e) {
    //            throw new RuntimeException(e.getMessage());
    //        }
    //
    //    }
}

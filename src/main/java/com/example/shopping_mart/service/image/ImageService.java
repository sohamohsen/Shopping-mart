package com.example.shopping_mart.service.image;

import com.example.shopping_mart.dto.ImageDto;
import com.example.shopping_mart.exceptions.ImageNotFoundExceptionError;
import com.example.shopping_mart.model.Image;
import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.repository.ImageRepository;
import com.example.shopping_mart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;
    private final ProductService productService;


    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundExceptionError("Image not found!"));
    }

    @Override
    public void deleteImageById(long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundExceptionError("No Image found with id: " + id);});
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl+image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto(savedImage.getId(),savedImage.getFileName(), savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            }   catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        Image newImage = new Image();
        try {
            newImage.setId(image.getId());
            newImage.setDownloadUrl(image.getDownloadUrl());
            newImage.setFileName(file.getOriginalFilename());
            newImage.setFileType(file.getContentType());
            log.info("image content type: " + file.getContentType());
            newImage.setImage(new SerialBlob(file.getBytes()));
            newImage.setProduct(image.getProduct());

            imageRepository.save(newImage);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

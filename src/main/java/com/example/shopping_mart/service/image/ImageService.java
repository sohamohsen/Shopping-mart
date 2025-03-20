package com.example.shopping_mart.service.image;

import com.example.shopping_mart.dto.ImageDto;
import com.example.shopping_mart.exceptions.ImageNotFoundExceptionError;
import com.example.shopping_mart.model.Image;
import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.repository.ImageRepository;
import com.example.shopping_mart.repository.ProductRepository;
import com.example.shopping_mart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundExceptionError("Image not found!"));
    }

    @Override
    public void deleteImageById(long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
            throw new ImageNotFoundExceptionError("No Image found with id: " + id);});    }

    @Override
    public List <ImageDto> saveImage(List<MultipartFile> file, Long productId) {
        Product product = productService.getProductById(productId);

        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile fileItem : file) {
            try{
                Image image = new Image();
                image.setFileName(fileItem.getOriginalFilename());
                image.setFileType(fileItem.getContentType());
                image.setImage(new SerialBlob(fileItem.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                savedImage = imageRepository.save(image);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownLoadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) throws IOException {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
        }catch (IOException | SQLException e){
            throw new RuntimeException(e);
        }
    }
}

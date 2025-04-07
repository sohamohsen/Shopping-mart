package com.example.shopping_mart.controller;

import com.example.shopping_mart.dto.ImageDto;
import com.example.shopping_mart.exceptions.CategoryNotFoundException;
import com.example.shopping_mart.exceptions.ImageNotFoundExceptionError;
import com.example.shopping_mart.exceptions.ProductNotFoundException;
import com.example.shopping_mart.exceptions.ResourceNotFoundException;
import com.example.shopping_mart.model.Image;
import com.example.shopping_mart.response.ApiResponse;
import com.example.shopping_mart.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files, @RequestParam long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("Upload success!", imageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }

    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);

        // Ensure the image exists, handle case where image is null
        if (image == null) {
            return ResponseEntity.notFound().build(); // Return 404 if the image is not found
        }

        // Convert the Blob to a ByteArrayResource, handling potential null Blob
        ByteArrayResource resource;
        if (image.getImage() != null) {
            resource = new ByteArrayResource(
                    image.getImage().getBytes(1, (int) image.getImage().length())
            );
        } else {
            return ResponseEntity.badRequest().build(); // Return 400 if Blob is null
        }

        // Return the image as a downloadable file
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType())) // Ensure this is a valid media type
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/productimages/{productId}")
    public ResponseEntity<ApiResponse> getImagesByProductId(@PathVariable Long productId) {
        try {
            List <ImageDto> images = imageService.getImagesByProductId(productId);
            return ResponseEntity.ok(new ApiResponse("Images: ", images));
        } catch (ProductNotFoundException | ImageNotFoundExceptionError e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload failed!", e.getMessage()));
        }
    }


    @PutMapping("/update/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile files, @PathVariable long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.updateImage(files, imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Update failed!", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping ("/delete/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if(image != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Delete success!", null));
            }
        } catch (ImageNotFoundExceptionError e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Delete failed!", INTERNAL_SERVER_ERROR));
    }

}

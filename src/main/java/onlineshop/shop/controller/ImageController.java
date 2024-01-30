package onlineshop.shop.controller;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.ProductImage;
import onlineshop.shop.repository.ProductImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private final ProductImageRepository productImageRepository;

    @GetMapping("/{imageId}")
    private ResponseEntity<?> getImageById(@PathVariable("imageId") Long id){
        ProductImage productImage = productImageRepository.findById(id).orElseThrow(null);
        return ResponseEntity
                .ok()
                .header("fileName",productImage.getName())
                .contentType(MediaType.valueOf(productImage.getContentType()))
                .contentLength(productImage.getSize())
                .body(new InputStreamResource((new ByteArrayInputStream(productImage.getImage()))));
    }
}

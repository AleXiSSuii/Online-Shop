package onlineshop.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "picture")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_picture")
    private String name;
    @Column(name = "image", columnDefinition="bytea")
    private byte[] image;
    @Column(name = "size_picture")
    private Long size;
    @Column(name = "uploaddate")
    private LocalDate uploadDate;
    @Column(name = "is_preview_image")
    private boolean isPreviewImage;
    @Column(name = "content_type")
    private String contentType;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_id")
    private Product product;
}

package onlineshop.shop.util;
import onlineshop.shop.model.ProductImage;
import java.util.List;

public class TestConstants {
    public static final Long ORDER_ID = 111L;
    public static final String ORDER_ADDRESS = "Wall Street 1";
    public static final String ORDER_CITY = "New York";
    public static final String ORDER_DATE = "2023-03-06";
    public static final String ORDER_EMAIL = "test123@test.com";
    public static final String ORDER_FIRST_NAME = "John";
    public static final String ORDER_LAST_NAME = "Doe";
    public static final String ORDER_PHONE_NUMBER = "1234567890";
    public static final Integer ORDER_POST_INDEX = 1111111;
    public static final Double ORDER_TOTAL_PRICE = 56.0;

    public static final Long PRODUCT_ID = 1L;
    public static final String PRODUCT_NAME = "NAME";
    public static final String PRODUCT_DESCRIPTION = "DESCRIPTION";
    public static final Double PRODUCT_PRICE = 111.1;
    public static final Integer PRODUCT_QUANTITY = 11;
    public static final List<ProductImage> PRODUCT_IMAGES = List.of(new ProductImage(),new ProductImage(),new ProductImage());
}
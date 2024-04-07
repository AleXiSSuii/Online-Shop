package onlineshop.shop.util;
import onlineshop.shop.model.ProductImage;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TestConstants {

    public static final Long ORDER_ID = 1L;
    public static final String ORDER_STATE = "Moscow";
    public static final String ORDER_CITY = "Moscow";
    public static final String ORDER_STREET = "Arbat";
    public static final String ORDER_BUILDING = "10";
    public static final String ORDER_APARTMENT = "10";
    public static final String ORDER_POST_INDEX = "111111";
    public static final String ORDER_EMAIL = "test123@test.com";
    public static final String ORDER_FIRST_NAME = "John";
    public static final String ORDER_LAST_NAME = "Doe";
    public static final String ORDER_PHONE_NUMBER = "1234567890";
    public static final LocalDateTime ORDER_DATE_TIME = LocalDateTime.of(2024, Month.JANUARY,1,10,0);
    public static final Double ORDER_TOTAL_PRICE = 900.0;


    public static final Long PRODUCT_ID = 1L;
    public static final String PRODUCT_NAME = "NAME";
    public static final String PRODUCT_DESCRIPTION = "DESCRIPTION";
    public static final Double PRODUCT_PRICE = 111.1;
    public static final Integer PRODUCT_QUANTITY = 11;
    public static final List<ProductImage> PRODUCT_IMAGES = List.of(new ProductImage(),new ProductImage(),new ProductImage());

    public static final String USER_EMAIL1 = "user@mail.com";
    public static final String USER_EMAIL2 = "user2@mail.com";
    public static final String USER_EMAIL3 = "user3@mail.com";
    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "User";
    public static final String USER_LASTNAME = "User";
    public static final String USER_NUMBER = "12345678";
    public static final String USER_BCRYPT_PASSWORD = "$2a$12$v6BG6KAno8x8TYQYRI4nTeHfzb1M79b9SccjvymrI0YHe/1oKhgC.";
    public static final String ACTIVATION_CODE = "CODE-FOR-USER";
    public static final String USER_PASSWORD = "123";

}

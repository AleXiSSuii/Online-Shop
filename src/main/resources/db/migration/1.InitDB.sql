create table users(
    id bigserial primary key,
    email varchar(255) unique not null ,
    number varchar(20) not null,
    password varchar(255),
    lastname varchar(255),
    "name" varchar(255),
    role varchar(20) not null default 'User',
    status varchar(20) not null default 'NOTACTIVATE',
    cart_id bigint,
    address_id bigint,
    activation_code varchar(255),
    FOREIGN KEY (cart_id) REFERENCES cart(id),
    FOREIGN KEY (address_id) REFERENCES address(id)
)
create table product(
    id bigserial primary key,
    "name" varchar(255),
    description varchar(255),
    price numeric(19,2),
    category_id integer,
    quantity integer default 0,
    foreign key (category_id) references category(id)
)

CREATE table orders(
    id BIGSERIAL PRIMARY KEY,
    total_price numeric(19,2) not null,
    order_date TIMESTAMP not null,
    status varchar(255),
    user_id BIGINT not null,
    FOREIGN KEY (user_id) REFERENCES users(id)
)
create table category(
    id integer primary key,
    "name" varchar(255)
)
create table cart(
    id bigserial primary key,
    final_price numeric(19,2)
)
CREATE TABLE IF NOT EXISTS public.address
(
    id bigint primary key,
    state character varying(30) not null,
    city character varying(30) not null,
    street character varying(30) not null,
    building character varying(30) not null,
    apartment character varying(30) not null,
    postal_code character varying(6) not null
)

create table cart_item(
    id BIGSERIAL PRIMARY KEY,
    quantity integer,
    price numeric(19,2),
    product_id BIGINT,
    cart_id BIGINT,
    order_id BIGINT,
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (cart_id) REFERENCES cart(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
)
CREATE TABLE picture (
    id          BIGSERIAL PRIMARY KEY,
    name_picture        VARCHAR(255),
    image       BYTEA,
    uploadDate  DATE,
    content_type varchar,
    size_picture BIGINT,
    is_preview_image BOOLEAN NOT NULL,
    product_id  BIGINT REFERENCES product(id)
);
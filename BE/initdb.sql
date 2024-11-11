CREATE DATABASE shop_sneaker_PH34331;

USE shop_sneaker_PH34331;

CREATE TABLE roles(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name ENUM('ADMIN', 'USER', 'GUEST'),
	created_at DATETIME,
	updated_at DATETIME
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(255),
    role_id INT,
    is_locked BIT DEFAULT 0,
    is_enabled BIT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_deleted BIT DEFAULT 0
);

CREATE TABLE employee_address(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    street_name varchar(255),
    ward varchar(255),
    district varchar(255),
    district_id INT,
    province varchar(255),
    province_id INT
);

CREATE TABLE employees(
		id INT AUTO_INCREMENT PRIMARY KEY,
		first_name VARCHAR(25),
		last_name  VARCHAR(50),
		address_id bigint,
		phone_number VARCHAR (20),
		gender enum('MALE','FEMALE','OTHER'),
		date_of_birth DATE ,
		image varchar(200),
    publicId varchar(200),
		position_id INT,
		salary_id INT,
		user_id BIGINT,
		created_at DATETIME,
		updated_at DATETIME
);
CREATE TABLE positions(
		id INT AUTO_INCREMENT PRIMARY KEY,
		name VARCHAR(100),
		created_at DATETIME,
		updated_at DATETIME
);
CREATE TABLE salary (
		id INT AUTO_INCREMENT PRIMARY KEY,
		amount DECIMAL(15,0),
		created_at DATETIME,
		updated_at DATETIME
);

-- Customer
CREATE TABLE customers (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(25),
    last_name varchar(50),
    phone_number varchar(20),
    gender enum('MALE', 'FEMALE', 'OTHER'),
    date_of_birth date,
    image varchar(200),
    publicId varchar(200),
    address_id bigint UNIQUE,
    user_id BIGINT,
    created_at datetime,
    updated_at datetime
);

CREATE TABLE customer_address (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    street_name varchar(255),
    ward varchar(255),
    district varchar(255),
	  district_id INT,
    city varchar(255),
	  city_id INT
);

-- Product
CREATE TABLE categories(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE brands(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE materials(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE products(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(200),
	description VARCHAR(255),
	status ENUM('ACTIVE', 'INACTIVE'),
	category_id INT,
	brand_id INT,
	material_id INT,
	origin VARCHAR(100),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE sizes(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100),
	length FLOAT,
	width FLOAT,
	sleeve FLOAT,
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE colors(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100),
	hex_color_code VARCHAR(100),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE product_images(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	product_id BIGINT,
	image_url VARCHAR(255),
	public_id VARCHAR(100)
);

CREATE TABLE product_details(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	product_id BIGINT,
	retail_price DECIMAL(15, 0),
	size_id INT,
	color_id INT,
	quantity INT,
	status ENUM('ACTIVE', 'INACTIVE')
);

-- Coupons
CREATE TABLE coupons (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(10) UNIQUE,
  name VARCHAR(100) NOT NULL,
  discount_type ENUM('FIXED_AMOUNT', 'PERCENTAGE'),
  discount_value DECIMAL(15,0),
  max_value DECIMAL(15,0),
  conditions DECIMAL(15,0),
  quantity INT,
  usage_count INT,
  type ENUM('PUBLIC', 'PERSONAL'),
  start_date DATETIME,
  end_date DATETIME,
  description TEXT,
  created_by BIGINT,
  updated_by BIGINT,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted BIT DEFAULT 0
);

CREATE TABLE coupon_images(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  coupon_id BIGINT,
  image_url VARCHAR(255),
  public_id VARCHAR(100),
  CONSTRAINT fk_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE CASCADE
);

CREATE TABLE coupon_share(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  coupon_id BIGINT,
  customer_id BIGINT,
  is_deleted BIT DEFAULT 0,
  CONSTRAINT fk_coupon_share FOREIGN KEY (coupon_id) REFERENCES coupons(id),
  CONSTRAINT fk_customer_share FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- promotions
CREATE TABLE promotions(
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
	code VARCHAR(20) UNIQUE,
  percent INT,
  start_date DATE,
  end_date DATE,
  note VARCHAR(255),
	created_by BIGINT,
	updated_by BIGINT,
	create_at DATETIME,
	update_at DATETIME,
	is_deleted BIT DEFAULT 0
);

CREATE TABLE promotion_details(
  id INT AUTO_INCREMENT PRIMARY KEY,
	product_id BIGINT,
  promotion_id INT
);

-- bill
CREATE TABLE bill_status (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(55),
	status ENUM(
    'PENDING',
    'PENDING_CONFIRMATION',
		'CONFIRMED', 
    'SHIPPED', 
    'DELIVERED', 
    'DELIVERY_FAILED',
    'CANCELED',
		'COMPLETED',
    'OTHER'
	),
  description TEXT
);

CREATE TABLE bill (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(10),
  bank_code VARCHAR(255),
  customer_id BIGINT,
  coupon_id BIGINT,
  bill_status_id INT,
  shipping DECIMAL(15, 0),
  subtotal DECIMAL(15,0),
  seller_discount DECIMAL(15,0),
  total DECIMAL(15, 0),
  payment_method ENUM('CASH','BANK','CASH_ON_DELIVERY'),
  message VARCHAR(255),
  note VARCHAR(255),
  payment_time DATETIME,
  created_by BIGINT,
  updated_by BIGINT,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted BIT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (coupon_id) REFERENCES coupons(id),
  FOREIGN KEY (bill_status_id) REFERENCES bill_status(id)
);

CREATE TABLE bill_detail (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_detail_id BIGINT,
  bill_id BIGINT,
  quantity INT,
  retail_price DECIMAL(15, 0),  
  discount_amount DECIMAL(15, 0), 
  total_amount_product DECIMAL(15, 0), 
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (product_detail_id) REFERENCES product_details(id),
  FOREIGN KEY (bill_id) REFERENCES bill(id) ON DELETE CASCADE
);

CREATE TABLE bill_status_detail (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  bill_id BIGINT,
  bill_status_id INT,
  note TEXT,
  created_by BIGINT,
  updated_by BIGINT,
  create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted BIT DEFAULT 0,
  FOREIGN KEY (bill_id) REFERENCES bill(id) ON DELETE CASCADE,
  FOREIGN KEY (bill_status_id) REFERENCES bill_status(id)
);

-- Employee
ALTER TABLE employees ADD CONSTRAINT fk_employee_address_id FOREIGN KEY (address_id) REFERENCES employee_address(id);

ALTER TABLE employees ADD CONSTRAINT fk_position_id FOREIGN KEY (position_id) REFERENCES positions(id);

ALTER TABLE employees ADD CONSTRAINT fk_salary_id FOREIGN KEY (salary_id) REFERENCES salary(id);

ALTER TABLE employees ADD CONSTRAINT fk_employees_user_id FOREIGN KEY (user_id) REFERENCES users(id);

-- Customer
ALTER TABLE customers ADD CONSTRAINT fk_customer_address FOREIGN KEY (address_id) REFERENCES customer_address(id);

ALTER TABLE customers ADD CONSTRAINT fk_customers_user_id FOREIGN KEY (user_id) REFERENCES users(id);

-- Product
ALTER TABLE users ADD CONSTRAINT fk_users_role_id FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE products ADD CONSTRAINT fk_products_category_id FOREIGN KEY (category_id) REFERENCES categories(id);

ALTER TABLE products ADD CONSTRAINT fk_products_brand_id FOREIGN KEY (brand_id) REFERENCES brands(id);

ALTER TABLE products ADD CONSTRAINT fk_products_material_id FOREIGN KEY (material_id) REFERENCES materials(id);

ALTER TABLE product_images ADD CONSTRAINT fk_products_id FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE product_details ADD CONSTRAINT fk_product_details_product_id FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE product_details ADD CONSTRAINT fk_product_details_size_id FOREIGN KEY (size_id) REFERENCES sizes(id);

ALTER TABLE product_details ADD CONSTRAINT fk_product_details_color_id FOREIGN KEY (color_id) REFERENCES colors(id);

ALTER TABLE products MODIFY description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- promotions
ALTER TABLE promotion_details ADD CONSTRAINT fk_promotion_product_id FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE promotion_details ADD CONSTRAINT fk_promotion_id FOREIGN KEY (promotion_id) REFERENCES promotions(id);

-- ROLE --
INSERT INTO roles (name, created_at, updated_at)
VALUES
('ADMIN', NOW(), NOW()),
('USER', NOW(), NOW()),
('GUEST', NOW(), NOW());

-- User System
INSERT INTO users (username, email, password, role_id, is_locked, is_enabled, created_at, updated_at, is_deleted)
VALUES
('sysadmin', 'admin@moe.vn', '$2a$12$ypc6KO9e7Re1GxDI3gfLf.mrSSma89BjKBm9GH96falWrIO56cxI.', 1, 0, 0, NOW(), NOW(), 0),
('vanthanh', 'thanhkvph34331@fpt.edu.vn', '$2a$12$85bbJKHgQ.hbQnnaPXgc7uPV2e6BKQa.Zbop5HqlcdwvI09cpzI6G', 1, 0, 0, NOW(), NOW(), 0),
('user', 'user@moe.vn', '$2a$12$L1voq9FiLnjeK9uk6t6fSu1JuTI.FnHOaKiTkjarX9Xxu4w0mWeRa', 2, 0, 0, NOW(), NOW(), 0);

-- Insert data into employee
INSERT INTO positions (name, created_at, updated_at)
VALUES
('Quản lý', NOW(), NOW()),
('Nhân viên', NOW(), NOW()),
('Hệ thống', NOW(), NOW());

INSERT INTO employee_address (street_name, ward, district, district_id, province, province_id)
VALUES
('81 Trung Kính', 'Phường Trung Hoà', 'Quận Cầu Giấy', 5, 'Thành phố Hà Nội', 1),
('81 Trung Kính', 'Phường Trung Hoà', 'Quận Cầu Giấy', 5, 'Thành phố Hà Nội', 1);

INSERT INTO salary (amount, created_at, updated_at)
VALUES
(1000000, NOW(), NOW()),
(1000000, NOW(), NOW());

INSERT INTO employees (first_name, last_name, address_id, phone_number, gender, date_of_birth, image, publicId, position_id, salary_id, user_id, created_at, updated_at)
VALUES
('system', 'admin', 1, '0123456789', 'OTHER', '2004-12-01', 'https://th.bing.com/th/id/OIP.O2j8k6g1NTAM0t_mIqOBtQHaE7?rs=1&pid=ImgDetMain', 'publicIdNotFound', 1, 1, 1, NOW(), NOW()),
('Thanh', 'Khuong Van', 2, '0777049085', 'MALE', '2004-12-01', 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337073/wyv5jqhzomkcgvejbjnl.jpg', 'publicIdNotFound', 1, 2, 2, NOW(), NOW());

-- Insert data into customer system
INSERT INTO customer_address(street_name, ward, district, district_id, city, city_id)
VALUES('81 Trung Kính', 'Phường Trung Hoà', 'Quận Cầu Giấy', 5, 'Thành phố Hà Nội', 1);

INSERT INTO customers (first_name, last_name, phone_number, gender, date_of_birth, image, publicId, address_id, user_id, created_at, updated_at)
VALUES('system', 'user', '0123456789', 'OTHER', '2004-12-01', 'https://th.bing.com/th/id/OIP.O2j8k6g1NTAM0t_mIqOBtQHaE7?rs=1&pid=ImgDetMain', 'publicIdNotFound', 1, 3, NOW(), NOW());

-- Insert data into categories
INSERT INTO categories (name, created_by, updated_by, create_at, update_at, is_deleted)
VALUES
('Giày thể thao', 1, 1, '2023-09-25 10:30:00', '2023-09-25 10:30:00', 0),
('Giày boots', 1, 1, '2023-09-26 11:00:00', '2023-09-26 11:00:00', 0),
('Giày slip-on', 1, 1, '2023-09-27 14:00:00', '2023-09-27 14:00:00', 0),
('Giày dép nữ', 1, 1, '2023-09-28 15:00:00', '2023-09-28 15:00:00', 0),
('Giày lười', 1, 1, '2023-09-29 16:00:00', '2023-09-29 16:00:00', 0);

-- Insert data into brands
INSERT INTO brands (name, created_by, updated_by, create_at, update_at, is_deleted)
VALUES
('Nike', 1, 1, '2023-09-25 10:30:00', '2023-09-25 10:30:00', 0),
('Adidas', 1, 1, '2023-09-26 11:00:00', '2023-09-26 11:00:00', 0),
('Puma', 1, 1, '2023-09-27 14:00:00', '2023-09-27 14:00:00', 0),
('Converse', 1, 1, '2023-09-28 15:00:00', '2023-09-28 15:00:00', 0),
('Reebok', 1, 1, '2023-09-29 16:00:00', '2023-09-29 16:00:00', 0);

-- Insert data into materials
INSERT INTO materials (name, created_by, updated_by, create_at, update_at, is_deleted)
VALUES
('Leather', 1, 1, '2023-09-25 10:30:00', '2023-09-25 10:30:00', 0),
('Rubber', 1, 1, '2023-09-26 11:00:00', '2023-09-26 11:00:00', 0),
('Suede', 1, 1, '2023-09-27 14:00:00', '2023-09-27 14:00:00', 0),
('Mesh', 1, 1, '2023-09-28 15:00:00', '2023-09-28 15:00:00', 0),
('Canvas', 1, 1, '2023-09-29 16:00:00', '2023-09-29 16:00:00', 0);

INSERT INTO sizes (name, length, width, sleeve, created_by, updated_by, create_at, update_at, is_deleted)
VALUES
('36', 23, 9, NULL, 1, 1, '2023-09-25 09:30:00', '2023-09-25 09:30:00', 0),
('37', 23.5, 9.2, NULL, 1, 1, '2023-09-26 10:00:00', '2023-09-26 10:00:00', 0),
('38', 24, 9.5, NULL, 1, 1, '2023-09-27 11:00:00', '2023-09-27 11:00:00', 0),
('39', 24.5, 9.8, NULL, 1, 1, '2023-09-28 12:00:00', '2023-09-28 12:00:00', 0),
('40', 25, 10, NULL, 1, 1, '2023-09-29 13:00:00', '2023-09-29 13:00:00', 0);

INSERT INTO colors (name, hex_color_code, created_by, updated_by, create_at, update_at, is_deleted)
VALUES
('Đỏ', '#FF0000', 1, 1, '2023-09-25 09:30:00', '2023-09-25 09:30:00', 0),
('Xanh Dương', '#0000FF', 1, 1, '2023-09-26 10:00:00', '2023-09-26 10:00:00', 0),
('Đen', '#000000', 1, 1, '2023-09-27 11:00:00', '2023-09-27 11:00:00', 0),
('Trắng', '#FFFFFF', 1, 1, '2023-09-28 12:00:00', '2023-09-28 12:00:00', 0),
('Vàng', '#FFFF00', 1, 1, '2023-09-29 13:00:00', '2023-09-29 13:00:00', 0);

INSERT INTO products (name, description, status, category_id, brand_id, material_id, origin, created_by, updated_by, create_at, update_at)
VALUES
('Giày thể thao nam cơ bản', 'Giày thể thao nam chất liệu thoáng khí, kiểu dáng đơn giản.', 'ACTIVE', 1, 1, 1, 'Vietnam', 1, 1, '2023-09-20 10:30:00', '2023-09-20 10:30:00'),
('Giày thể thao nữ cơ bản', 'Giày thể thao nữ chất liệu thoáng khí, kiểu dáng đơn giản.', 'ACTIVE', 1, 2, 1, 'Vietnam', 1, 1, '2023-09-21 10:30:00', '2023-09-21 10:30:00'),
('Giày thể thao nam cao cấp', 'Giày thể thao nam với chất liệu cao cấp, thoải mái khi vận động.', 'ACTIVE', 1, 3, 1, 'Japan', 2, 2, '2023-09-22 11:00:00', '2023-09-22 11:00:00'),
('Giày thể thao nữ cao cấp', 'Giày thể thao nữ với thiết kế sang trọng, chất liệu cao cấp.', 'ACTIVE', 1, 4, 1, 'Taiwan', 3, 3, '2023-09-23 12:00:00', '2023-09-23 12:00:00'),
('Giày boots nam', 'Giày boots nam, bảo vệ chân và giữ ấm tốt.', 'ACTIVE', 1, 1, 2, 'Vietnam', 1, 1, '2023-09-24 10:30:00', '2023-09-24 10:30:00'),
('Giày boots nữ', 'Giày boots nữ, phong cách và sang trọng.', 'ACTIVE', 1, 2, 2, 'Japan', 2, 2, '2023-09-25 10:30:00', '2023-09-25 10:30:00'),
('Giày lười nam', 'Giày lười nam, tiện lợi và phong cách.', 'ACTIVE', 1, 3, 1, 'Taiwan', 3, 3, '2023-09-26 11:00:00', '2023-09-26 11:00:00'),
('Giày lười nữ', 'Giày lười nữ, thời trang và thoải mái.', 'ACTIVE', 1, 4, 1, 'Vietnam', 1, 1, '2023-09-27 12:00:00', '2023-09-27 12:00:00'),
('Giày slip-on nam', 'Giày slip-on nam, trẻ trung và năng động.', 'ACTIVE', 1, 1, 2, 'Japan', 2, 2, '2023-09-28 10:30:00', '2023-09-28 10:30:00'),
('Giày slip-on nữ', 'Giày slip-on nữ, nhẹ nhàng và dễ chịu.', 'ACTIVE', 1, 2, 2, 'Taiwan', 3, 3, '2023-09-29 11:00:00', '2023-09-29 11:00:00'),
('Giày thể thao mùa hè nam', 'Giày thể thao mùa hè, thoáng khí và nhẹ nhàng.', 'ACTIVE', 1, 1, 1, 'Vietnam', 1, 1, '2023-09-30 12:00:00', '2023-09-30 12:00:00'),
('Giày thể thao mùa hè nữ', 'Giày thể thao mùa hè cho nữ, thoải mái và thoáng mát.', 'ACTIVE', 1, 2, 1, 'Japan', 2, 2, '2023-08-20 10:30:00', '2023-08-20 10:30:00'),
('Giày thể thao cổ điển nam', 'Giày thể thao cổ điển, phong cách nam tính.', 'ACTIVE', 1, 3, 1, 'Taiwan', 3, 3, '2023-08-21 11:00:00', '2023-08-21 11:00:00'),
('Giày thể thao cổ điển nữ', 'Giày thể thao cổ điển, phong cách nữ tính và năng động.', 'ACTIVE', 1, 4, 1, 'Vietnam', 1, 1, '2023-08-22 12:00:00', '2023-08-22 12:00:00'),
('Giày thể thao năng động nam', 'Giày thể thao năng động, thích hợp cho mọi hoạt động thể thao.', 'ACTIVE', 1, 1, 2, 'Japan', 2, 2, '2023-08-23 10:30:00', '2023-08-23 10:30:00'),
('Giày thể thao năng động nữ', 'Giày thể thao năng động, phù hợp cho vận động thể thao nữ.', 'ACTIVE', 1, 2, 2, 'Taiwan', 3, 3, '2023-08-24 11:00:00', '2023-08-24 11:00:00'),
('Giày thể thao chống nước nam', 'Giày thể thao chống nước cho nam, phù hợp cho mọi thời tiết.', 'ACTIVE', 1, 3, 1, 'Japan', 2, 2, '2023-08-25 12:00:00', '2023-08-25 12:00:00'),
('Giày thể thao chống nước nữ', 'Giày thể thao chống nước cho nữ, bảo vệ đôi chân trong mọi điều kiện thời tiết.', 'ACTIVE', 1, 4, 1, 'Taiwan', 3, 3, '2023-08-26 10:30:00', '2023-08-26 10:30:00'),
('Giày mùa đông nam', 'Giày mùa đông cho nam, giữ ấm và chống lạnh hiệu quả.', 'ACTIVE', 1, 1, 2, 'Vietnam', 1, 1, '2023-08-27 11:00:00', '2023-08-27 11:00:00'),
('Giày mùa đông nữ', 'Giày mùa đông cho nữ, thiết kế đẹp và giữ ấm.', 'ACTIVE', 1, 2, 2, 'Japan', 2, 2, '2023-08-28 12:00:00', '2023-08-28 12:00:00'),
('Giày lót mỏng nam', 'Giày lót mỏng cho nam, thoáng mát và dễ chịu.', 'ACTIVE', 1, 3, 1, 'Taiwan', 3, 3, '2023-08-29 10:30:00', '2023-08-29 10:30:00'),
('Giày lót mỏng nữ', 'Giày lót mỏng cho nữ, thoáng mát và tiện dụng.', 'ACTIVE', 1, 4, 1, 'Vietnam', 1, 1, '2023-08-30 11:00:00', '2023-08-30 11:00:00'),
('Giày thể thao đơn giản nam', 'Giày thể thao đơn giản cho nam, dễ phối đồ.', 'ACTIVE', 1, 1, 2, 'Japan', 2, 2, '2023-09-01 10:30:00', '2023-09-01 10:30:00'),
('Giày thể thao đơn giản nữ', 'Giày thể thao đơn giản cho nữ, dễ dàng phối hợp với nhiều trang phục.', 'ACTIVE', 1, 2, 2, 'Taiwan', 3, 3, '2023-09-02 11:00:00', '2023-09-02 11:00:00'),
('Giày thể thao oversized nam', 'Giày thể thao oversized, phong cách cho nam.', 'ACTIVE', 1, 3, 1, 'Vietnam', 1, 1, '2023-09-03 12:00:00', '2023-09-03 12:00:00'),
('Giày thể thao oversized nữ', 'Giày thể thao oversized cho nữ, thiết kế trẻ trung.', 'ACTIVE', 1, 4, 1, 'Japan', 2, 2, '2023-09-04 10:30:00', '2023-09-04 10:30:00'),
('Giày thể thao màu sắc nam', 'Giày thể thao với nhiều màu sắc cho nam, phong cách năng động.', 'ACTIVE', 1, 1, 2, 'Taiwan', 3, 3, '2023-09-05 11:00:00', '2023-09-05 11:00:00');

INSERT INTO product_images (product_id, image_url, public_id) VALUES
(1, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337647/giaythethao/kut16cwokosa4morsn45.jpg', 'kut16cwokosa4morsn45'),
(2, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337647/giaythethao/n2ntafgpgccrgq3wajgj.jpg', 'n2ntafgpgccrgq3wajgj'),
(3, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337647/giaythethao/zqlimajey13qdwuwltyr.jpg', 'zqlimajey13qdwuwltyr'),
(4, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/ge88sdw7spqb3kr2mths.jpg', 'ge88sdw7spqb3kr2mths'),
(5, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/oulpgfhaqj7a1mefjvea.jpg', 'oulpgfhaqj7a1mefjvea'),
(6, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/rcffbnuy9fkirjhttrod.jpg', 'rcffbnuy9fkirjhttrod'),
(7, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/qfxbnfcdfuiamsmy0qsk.jpg', 'qfxbnfcdfuiamsmy0qsk'),
(8, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/xydasm1c5cao11eogddz.jpg', 'xydasm1c5cao11eogddz'),
(9, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/a4w1ig0svgwrmuyngjsc.jpg', 'a4w1ig0svgwrmuyngjsc'),
(10, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/odxmbvijnoayv8kzpqt0.jpg', 'odxmbvijnoayv8kzpqt0'),
(11, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/vt3b9kddupjgwdwlcthl.jpg', 'vt3b9kddupjgwdwlcthl'),
(12, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/kxvr9iivfjyi7uz1fb7z.jpg', 'kxvr9iivfjyi7uz1fb7z'),
(13, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/uq6z91scgjjzolx0qrc8.jpg', 'uq6z91scgjjzolx0qrc8'),
(14, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/uq6z91scgjjzolx0qrc8.jpg', 'uq6z91scgjjzolx0qrc8'),
(15, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/g617ghr1q6qiplt974av.jpg', 'g617ghr1q6qiplt974av'),
(16, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/g617ghr1q6qiplt974av.jpg', 'g617ghr1q6qiplt974av'),
(17, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/xbjmucz1hjczyebweiyd.jpg', 'xbjmucz1hjczyebweiyd'),
(18, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/xbjmucz1hjczyebweiyd.jpg', 'xbjmucz1hjczyebweiyd'),
(19, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/xbjmucz1hjczyebweiyd.jpg', 'xbjmucz1hjczyebweiyd'),
(20, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337646/giaythethao/a4w1ig0svgwrmuyngjsc.jpg', 'a4w1ig0svgwrmuyngjsc'),
(21, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337647/giaythethao/zqlimajey13qdwuwltyr.jpg', 'zqlimajey13qdwuwltyr'),
(22, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/uq6z91scgjjzolx0qrc8.jpg', 'uq6z91scgjjzolx0qrc8'),
(23, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337647/giaythethao/kut16cwokosa4morsn45.jpg', 'kut16cwokosa4morsn45'),
(24, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/uq6z91scgjjzolx0qrc8.jpg', 'uq6z91scgjjzolx0qrc8'),
(25, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/kxvr9iivfjyi7uz1fb7z.jpg', 'kxvr9iivfjyi7uz1fb7z'),
(26, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/xbjmucz1hjczyebweiyd.jpg', 'xbjmucz1hjczyebweiyd'),
(27, 'https://res.cloudinary.com/dp0odec5s/image/upload/v1731337645/giaythethao/xbjmucz1hjczyebweiyd.jpg', 'xbjmucz1hjczyebweiyd');

INSERT INTO product_details (product_id, retail_price, size_id, color_id, quantity, status)
VALUES
(1, 299000, 2, 1, 50, 'ACTIVE'),
(2, 399000, 3, 2, 30, 'ACTIVE'),
(4, 599000, 4, 1, 20, 'ACTIVE'),
(5, 499000, 2, 2, 15, 'INACTIVE'),
(6, 350000, 1, 3, 25, 'ACTIVE'),
(7, 450000, 3, 4, 35, 'ACTIVE'),
(8, 290000, 1, 2, 60, 'ACTIVE'),
(9, 550000, 2, 1, 20, 'INACTIVE'),
(10, 720000, 4, 3, 10, 'ACTIVE'),
(11, 299000, 1, 4, 55, 'ACTIVE'),
(12, 399000, 2, 1, 30, 'ACTIVE'),
(13, 249000, 3, 2, 40, 'ACTIVE'),
(14, 599000, 4, 3, 22, 'ACTIVE'),
(15, 499000, 5, 4, 15, 'INACTIVE'),
(16, 350000, 1, 1, 20, 'ACTIVE'),
(17, 450000, 2, 2, 35, 'ACTIVE'),
(18, 290000, 3, 3, 45, 'ACTIVE'),
(19, 550000, 4, 4, 25, 'INACTIVE'),
(20, 720000, 5, 1, 18, 'ACTIVE'),
(21, 299000, 2, 1, 50, 'ACTIVE'),
(22, 399000, 3, 2, 30, 'ACTIVE'),
(23, 249000, 1, 3, 40, 'ACTIVE'),
(24, 599000, 4, 1, 20, 'ACTIVE'),
(25, 499000, 2, 2, 15, 'INACTIVE'),
(26, 350000, 1, 3, 25, 'ACTIVE'),
(27, 450000, 3, 4, 35, 'ACTIVE');

-- promotions
INSERT INTO promotions (name, code, percent, start_date, end_date, note, created_by, updated_by, create_at, update_at, is_deleted)
VALUES 
('Khuyến mãi Tết Nguyên Đán', 'TET2024', 15, '2024-02-01', '2024-02-15', 'Giảm giá cho Tết Nguyên Đán', 1, 1, '2023-10-01 08:00:00', '2023-10-01 08:00:00', 0),
('Khuyến mãi 8/3', 'QUOCTEPN', 20, '2024-03-01', '2024-03-08', 'Khuyến mãi nhân ngày Quốc tế Phụ nữ', 2, 2, '2023-10-05 09:00:00', '2023-10-05 09:00:00', 0),
('Sale cuối năm', 'CUOINAM2024', 30, '2024-12-15', '2024-12-31', 'Giảm giá đặc biệt cuối năm', 3, 3, '2023-10-10 10:30:00', '2023-10-10 10:30:00', 0),
('Mừng Ngày Nhà giáo', 'NGAYNHA23', 10, '2024-11-10', '2024-11-20', 'Khuyến mãi nhân ngày Nhà giáo Việt Nam', 1, 1, '2023-10-15 11:45:00', '2023-10-15 11:45:00', 0),
('Giảm giá mùa hè', 'SUMMER2024', 25, '2024-06-01', '2024-06-30', 'Khuyến mãi mùa hè', 2, 2, '2023-10-20 13:20:00', '2023-10-20 13:20:00', 0),
('Khuyến mãi Giáng sinh', 'NOEL2024', 35, '2024-12-20', '2024-12-25', 'Ưu đãi đặc biệt nhân dịp Giáng sinh', 3, 3, '2023-10-25 15:00:00', '2023-10-25 15:00:00', 0),
('Ưu đãi mùa thu', 'AUTUMN23', 10, '2024-09-01', '2024-09-30', 'Giảm giá mùa thu', 1, 1, '2023-10-28 16:45:00', '2023-10-28 16:45:00', 0),
('Giảm giá dịp lễ 30/4', 'LE304', 20, '2024-04-25', '2024-05-01', 'Ưu đãi đặc biệt dịp lễ 30/4', 2, 2, '2023-10-30 17:30:00', '2023-10-30 17:30:00', 0),
('Khuyến mãi Thứ 6 Đen', 'BLACKFRI2024', 50, '2024-11-25', '2024-11-29', 'Khuyến mãi Black Friday', 3, 3, '2023-11-01 08:15:00', '2023-11-01 08:15:00', 0),
('Ưu đãi Valentine', 'VALENTINE', 15, '2024-02-10', '2024-02-14', 'Ưu đãi dành cho ngày Valentine', 1, 1, '2023-11-05 09:45:00', '2023-11-05 09:45:00', 0);


-- Coupons
INSERT INTO coupons (code, name, discount_type, discount_value, max_value, conditions, quantity,usage_count, type, start_date, end_date, description, created_by, updated_by) VALUES
('COUP01', 'Summer Sale', 'PERCENTAGE', 10, 5000, 100, 100,0, 'PUBLIC', '2024-06-01 00:00:00', '2024-07-01 00:00:00', '10% off summer sale', 1, 1),
('COUP02', 'Winter Offer', 'FIXED_AMOUNT', 500, 5000, 1000, 50,0, 'PERSONAL', '2024-12-01 00:00:00', '2024-12-31 00:00:00', '500 off winter offer', 2, 2),
('COUP03', 'Black Friday', 'PERCENTAGE', 20, 10000, 500, 200,0, 'PUBLIC', '2024-11-25 00:00:00', '2024-11-30 00:00:00', '20% off Black Friday', 3, 3),
('COUP04', 'New Year Discount', 'FIXED_AMOUNT', 1000, 10000,0, 2000, 150, 'PERSONAL', '2024-12-31 00:00:00', '2025-01-01 23:59:59', '1000 off New Year Discount', 4, 4),
('COUP05', 'Flash Sale', 'PERCENTAGE', 15, 8000, 300, 500,0, 'PUBLIC', '2024-10-15 00:00:00', '2024-10-16 00:00:00', '15% off Flash Sale', 5, 5),
('COUP06', 'Holiday Offer', 'FIXED_AMOUNT', 200, 2000, 1000,0, 300, 'PERSONAL', '2024-12-20 00:00:00', '2024-12-25 00:00:00', '200 off Holiday Offer', 6, 6),
('COUP07', 'Exclusive Discount', 'PERCENTAGE', 5, 3000, 500, 50,0, 'PERSONAL', '2024-11-01 00:00:00', '2024-11-10 00:00:00', '5% Exclusive Discount', 7, 7),
('COUP08', 'Limited Offer', 'FIXED_AMOUNT', 1500, 6000, 2000, 100,0, 'PUBLIC', '2024-10-20 00:00:00', '2024-10-30 00:00:00', '1500 off Limited Offer', 8, 8),
('COUP09', 'Birthday Special', 'PERCENTAGE', 25, 7000, 500, 50,0, 'PERSONAL', '2024-11-20 00:00:00', '2024-11-25 00:00:00', '25% off Birthday Special', 9, 9),
('COUP10', 'Anniversary Deal', 'FIXED_AMOUNT', 300, 2000, 800, 50,0, 'PUBLIC', '2024-10-01 00:00:00', '2024-10-10 00:00:00', '300 off Anniversary Deal', 10, 10);

INSERT INTO coupon_images (coupon_id, image_url, public_id) VALUES
(1, 'https://example.com/images/coupon01.jpg', 'abc123'),
(2, 'https://example.com/images/coupon02.jpg', 'def456'),
(3, 'https://example.com/images/coupon03.jpg', 'ghi789'),
(4, 'https://example.com/images/coupon04.jpg', 'jkl012'),
(5, 'https://example.com/images/coupon05.jpg', 'mno345'),
(6, 'https://example.com/images/coupon06.jpg', 'pqr678'),
(7, 'https://example.com/images/coupon07.jpg', 'stu901'),
(8, 'https://example.com/images/coupon08.jpg', 'vwx234'),
(9, 'https://example.com/images/coupon09.jpg', 'yz567'),
(10, 'https://example.com/images/coupon10.jpg', 'abc890');
    
INSERT INTO bill_status (name, status) VALUES
('Đang chờ xử lý', 'PENDING'),
('Đang chờ xác nhận', 'PENDING_CONFIRMATION'),
('Đã xác nhận', 'CONFIRMED'),
('Đã bàn giao cho đơn vị vận chuyển', 'SHIPPED'),
('Đã giao thành công', 'DELIVERED'),
('Giao hàng thất bại', 'DELIVERY_FAILED'),
('Đã hủy đơn hàng', 'CANCELED'),
('Đơn hàng hoàn tất', 'COMPLETED'),
('Khác', 'OTHER');
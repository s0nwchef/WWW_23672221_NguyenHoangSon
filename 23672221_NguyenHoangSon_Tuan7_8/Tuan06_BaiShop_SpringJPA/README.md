# Bài tập Spring Boot – Spring Data JPA + Thymeleaf

Database: **shoppingdb** (MariaDB) – bảng `customers`, `products`, `orders`, `orderlines`, `comments`.

## 1. Chuẩn bị
| Thứ cần | Ghi chú |
|---|---|
| JDK 21 | `java -version` phải ra 21 |
| IntelliJ IDEA | Mở bằng **File > Open** → chọn thư mục chứa `pom.xml` |
| MariaDB/MySQL | XAMPP/HeidiSQL đều được, cổng 3306 |

## 2. Các bước triển khai
1. **Import DB**: chạy file `C:/Users/amind/Downloads/shopping.sql` (đã có `CREATE DATABASE` + dữ liệu mẫu).
2. **Mở project** trong IntelliJ, chờ Maven tải dependency.
3. **Project SDK = 21**.
4. **Sửa kết nối** trong `src/main/resources/application.properties` (`username`, `password`).
5. **Chạy** `ShoppingApplication`.
6. Mở http://localhost:8085/home

## 3. URL các chức năng
`/home`, `/product`, `/product/{id}`, `/order`, `/order/{id}`, `/customer`, `/customer/{id}`.

## 4. Kiến trúc
```
Browser → Controller → Service → Repository (interface, Spring tự sinh) → MariaDB
                ↓
        View (Thymeleaf)
```
Các entity dùng `@Entity`, `@Table`, `@Id` và các quan hệ JPA `@OneToMany`, `@ManyToOne`. Repository mở rộng `JpaRepository`; Spring tự tạo phần cài đặt CRUD.

## 5. Quan hệ dữ liệu

- `Customer` 1-n `PurchaseOrder`.
- `PurchaseOrder` 1-n `OrderLine`.
- `Product` 1-n `OrderLine` và 1-n `Comment`.

Các repository có method naming, ví dụ `findAllByOrderByNameAsc()`. Trang chi tiết dùng `@EntityGraph` để tải đúng các quan hệ cần hiển thị khi `spring.jpa.open-in-view=false`.

## 6. Lưu ý schema

File SQL có cả `inStock`/`in_stock` và `purchasePrice`/`purchase_price`. Dữ liệu mẫu nằm ở hai cột camelCase, nên entity map tường minh vào `inStock` và `purchasePrice`.

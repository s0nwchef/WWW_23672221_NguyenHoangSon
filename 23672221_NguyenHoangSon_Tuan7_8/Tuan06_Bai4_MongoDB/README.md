# Bài 2 – Spring Data JDBC (CrudRepository) + Thymeleaf

Database: **employeedb** (MariaDB) – bảng `employees`, `departments`, `positions`.

## 1. Chuẩn bị
| Thứ cần | Ghi chú |
|---|---|
| JDK 21 | `java -version` phải ra 21 |
| IntelliJ IDEA | Mở bằng **File > Open** → chọn thư mục chứa `pom.xml` |
| MariaDB/MySQL | XAMPP/HeidiSQL đều được, cổng 3306 |

## 2. Các bước triển khai
1. **Import DB**: chạy `database/employeedb.sql` (đã có `CREATE DATABASE` + dữ liệu mẫu).
2. **Mở project** trong IntelliJ, chờ Maven tải dependency.
3. **Project SDK = 21**.
4. **Sửa kết nối** trong `src/main/resources/application.properties` (`username`, `password`).
5. **Chạy** `EmployeeManagementApplication`.
6. Mở http://localhost:8080

## 3. URL các chức năng
Giống Bài 1: `/employees`, `/employees/new`, `/employees/search`, `/reports`.

## 4. Kiến trúc
```
Browser → Controller → Service → Repository (interface, Spring tự sinh) → MariaDB
                ↓
        View (Thymeleaf)
```
Khác Bài 1 ở **3 chỗ**:
| | Bài 1 – Spring JDBC | Bài 2 – Spring Data JDBC |
|---|---|---|
| Starter | `spring-boot-starter-jdbc` | `spring-boot-starter-data-jdbc` |
| Entity | POJO thường | `@Table("employees")`, `@Id` |
| Repository | `class` + `JdbcTemplate`, tự viết SQL | `interface extends ListCrudRepository` – **không viết code cài đặt** |
`Service`, `Controller`, `Thymeleaf` hầu như giữ nguyên.

## 5. Entity (xem `entity/Employee.java`)
```java
@Table("employees")            // map class -> bảng
public class Employee {
    @Id private Integer id;    // khóa chính, tự tăng (id null => INSERT)
    ...
    @Column("department_id") private Integer departmentId;
}
```
- Không có `@Entity` (đó là của JPA). Không có lazy loading, không có `@ManyToOne`: quan hệ 1–n chỉ lưu **khóa ngoại dạng Integer** (`departmentId`, `positionId`).

## 6. Chức năng → method (trong `EmployeeRepository`)
| Chức năng | Cách làm |
|---|---|
| Thêm / Sửa | `save(employee)` – id null thì INSERT, có id thì UPDATE |
| Xóa | `deleteById(id)` |
| Danh sách | `findAll()` |
| Tìm theo id | `findById(id)` |
| Tìm theo name | `findByNameContainingIgnoreCase(name)` *(derived query)* |
| Tìm theo age | `findByAgeBetween(a, b)` |
| Tìm theo salary | `findBySalaryGreaterThan(s)` |
| Tìm kết hợp (form) | `@Query ... (:name IS NULL OR ...)` – tham số null thì bỏ qua điều kiện |
| Theo Position | `findByPositionId(id)` |
| Theo Dept + Position | `findByDepartmentIdAndPositionId(d, p)` |
| Top N lương | `findTop5ByDepartmentIdOrderBySalaryDesc(id)` hoặc `@Query ... LIMIT :n` |
| Đếm NV / phòng, lương TB/tổng | `@Query(value=..., rowMapperClass=...)` map sang DTO |
| Phòng chưa có NV | `@Query` trong `DepartmentRepository` |

## 7. Điểm cần nhớ (để thuyết trình / thi)
- **Derived query**: Spring đọc tên method → tự sinh SQL. `Containing` = LIKE %x%, `IgnoreCase` = không phân biệt hoa thường, `Between`, `GreaterThan`, `And`, `Top5`, `OrderBySalaryDesc`.
- Tên property Java (`departmentId`) tự ánh xạ sang cột `department_id` (camelCase → snake_case).
- Truy vấn phức tạp (GROUP BY, JOIN) → dùng `@Query` viết SQL thuần; kết quả không phải entity thì dùng `rowMapperClass`.
- Interface hỗ trợ **default method** (`search(criteria)`) để gói lại `searchAll(...)`.
- **Spring Data JDBC ≠ JPA**: không có Hibernate, không cache cấp 1, không lazy loading → đơn giản, dễ đoán SQL sinh ra.

## 8. Lỗi thường gặp
Giống Bài 1, thêm:
| Lỗi | Cách xử lý |
|---|---|
| `Table 'employeedb.employee' doesn't exist` | Thiếu `@Table("employees")` trên entity |
| `No property xxx found for type Employee` | Gõ sai tên method `findByXxx` (phải trùng tên field Java) |
| Muốn xem SQL được sinh | Bỏ comment dòng `logging.level.org.springframework.data.jdbc=DEBUG` |

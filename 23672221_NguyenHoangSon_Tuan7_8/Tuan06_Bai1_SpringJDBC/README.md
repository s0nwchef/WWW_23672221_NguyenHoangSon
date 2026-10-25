# Bài 1 – Spring JDBC (JdbcTemplate) + Thymeleaf

Database: **employeedb** (MariaDB) – bảng `employees`, `departments`, `positions`.

## 1. Chuẩn bị
| Thứ cần | Ghi chú |
|---|---|
| JDK 21 | `java -version` phải ra 21 |
| IntelliJ IDEA | Mở bằng **File > Open** → chọn thư mục chứa `pom.xml` |
| MariaDB/MySQL | XAMPP/HeidiSQL đều được, cổng 3306 |

## 2. Các bước triển khai
1. **Import DB**: mở HeidiSQL (hoặc phpMyAdmin) → chạy file `database/employeedb.sql` (file đã có `CREATE DATABASE` + dữ liệu mẫu).
2. **Mở project** trong IntelliJ, chờ Maven tải dependency (góc dưới phải).
3. **Project SDK = 21**: File > Project Structure > Project > SDK 21.
4. **Sửa kết nối** trong `src/main/resources/application.properties`: `spring.datasource.username` / `password` (XAMPP mặc định: `root` / rỗng).
5. **Chạy** class `EmployeeManagementApplication` (nút ▶).
6. Mở trình duyệt: http://localhost:8080

## 3. URL các chức năng
| URL | Chức năng |
|---|---|
| `/employees` | Danh sách + Sửa/Xóa |
| `/employees/new` | Thêm mới (có validate) |
| `/employees/{id}` , `/employees/{id}/edit` | Chi tiết, sửa |
| `/employees/search` | Tìm theo id, tên, tuổi (từ–đến), lương (từ), vị trí |
| `/reports` | Toàn bộ thống kê quan hệ 1–n |

## 4. Kiến trúc (MVC nhiều lớp)
```
Browser → Controller → Service → Repository (JdbcTemplate) → MariaDB
                ↓
        View (Thymeleaf template)
```
```
controller/  EmployeeController, ReportController, HomeController
service/     EmployeeService
repository/  EmployeeRepository, DepartmentRepository, PositionRepository   <-- JdbcTemplate
entity/      Employee, Department, Position   (POJO thường, KHÔNG annotation)
dto/         EmployeeForm, EmployeeSearchCriteria, DepartmentCount, DepartmentSalary
mapper/      DepartmentCountRowMapper, DepartmentSalaryRowMapper
```

## 5. Chức năng → câu SQL (trong `EmployeeRepository`)
| Chức năng | Method | SQL |
|---|---|---|
| Thêm | `save()` (id null) | `INSERT INTO employees ...` + `GeneratedKeyHolder` lấy id tự tăng |
| Sửa | `save()` (id có) | `UPDATE employees SET ... WHERE id=?` |
| Xóa | `deleteById()` | `DELETE FROM employees WHERE id=?` |
| Danh sách | `findAll()` | `SELECT ... FROM employees` + RowMapper |
| Tìm theo id | `findById()` | `WHERE id = ?` |
| Tìm theo name | `findByNameContainingIgnoreCase()` | `WHERE LOWER(name) LIKE ?` |
| Tìm theo age | `findByAgeBetween()` | `WHERE age BETWEEN ? AND ?` |
| Tìm theo salary | `findBySalaryGreaterThan()` | `WHERE salary > ?` |
| Tìm kết hợp (form) | `search()` | ghép `WHERE` động bằng StringBuilder |
| Theo Position | `findByPositionId()` | `WHERE position_id = ?` |
| Theo Dept + Position | `findByDepartmentIdAndPositionId()` | `WHERE department_id=? AND position_id=?` |
| Top N lương | `findTopByDepartmentId()` | `ORDER BY salary DESC LIMIT ?` |
| Đếm NV / phòng | `countByDepartment()` | `LEFT JOIN ... GROUP BY` |
| Lương TB, tổng | `salaryStatsByDepartment()` | `AVG(), SUM() ... GROUP BY` |
| Phòng chưa có NV | `DepartmentRepository.findWithoutEmployees()` | `LEFT JOIN ... WHERE e.id IS NULL` |

## 6. Điểm cần nhớ (để thuyết trình / thi)
- **Spring JDBC = tự viết SQL**: `JdbcTemplate.query()` (đọc nhiều dòng), `update()` (INSERT/UPDATE/DELETE).
- **RowMapper** = đoạn code chuyển 1 dòng `ResultSet` → 1 object Java (thay cho annotation entity).
- Entity là POJO thường, không cần `@Entity`/`@Table`/`@Id`.
- Dependency: `spring-boot-starter-jdbc`. Spring Boot tự tạo `DataSource` + `JdbcTemplate` từ `application.properties`.
- Dùng **`?` (PreparedStatement)** chống SQL Injection – không nối chuỗi trực tiếp giá trị người dùng vào SQL.

## 7. Lỗi thường gặp
| Lỗi | Cách xử lý |
|---|---|
| `Communications link failure` / `Connection refused` | Chưa bật MariaDB, hoặc sai cổng |
| `Access denied for user` | Sai username/password trong `application.properties` |
| `Unknown database 'employeedb'` | Chưa import `employeedb.sql` |
| Tiếng Việt bị `???` | Import lại SQL với UTF-8; URL đã có `characterEncoding=UTF-8` |
| `release version 21 not supported` | IntelliJ đang dùng JDK thấp hơn 21 |
| Port 8080 đã bị chiếm | Đổi `server.port=8081` |

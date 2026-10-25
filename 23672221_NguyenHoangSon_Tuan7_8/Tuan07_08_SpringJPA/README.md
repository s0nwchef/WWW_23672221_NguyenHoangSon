# Tuần 8–9 – Spring Data JPA + Spring Security + Thymeleaf

Database: **employeedb** (MariaDB) – bảng `employees`, `departments`, `positions`, và `user` (đăng nhập).

## 1. Chuẩn bị
| Thứ cần | Ghi chú |
|---|---|
| JDK 21 | `java -version` phải ra 21 |
| IntelliJ IDEA | **File > Open** → chọn thư mục chứa `pom.xml` |
| MariaDB/MySQL | XAMPP/HeidiSQL đều được, cổng 3306 |

## 2. Các bước chạy
1. Import `database/employeedb.sql` (có `CREATE DATABASE` + dữ liệu mẫu).
2. Import `database/user.sql` (tạo bảng `user` + 6 tài khoản). Nếu bảng `user` đã có sẵn trong DB thì bỏ qua bước này.
3. Mở project trong IntelliJ, chờ Maven tải dependency, Project SDK = 21.
4. Sửa `username`, `password` DB trong `src/main/resources/application.properties`.
5. Chạy `EmployeeManagementApplication`, mở http://localhost:8080 (sẽ chuyển sang `/login`).

## 3. Tài khoản test (mật khẩu lưu thô như đề, không BCrypt)
| Username | Password | Role |
|---|---|---|
| admin1, admin2 | 123 | ADMIN |
| user1 | 111 | USER |
| user2 | 222 | USER |
| guest1 | 333 | GUEST |
| guest2 | 444 | GUEST |

Role lưu trong DB là `ADMIN` / `USER` / `GUEST` (không có `ROLE_`), Spring tự thêm `ROLE_` khi dùng `.roles(...)`.

## 4. Phân quyền
| Chức năng | URL | GUEST | USER | ADMIN |
|---|---|:-:|:-:|:-:|
| Danh sách | `GET /employees` | ✓ | ✓ | ✓ |
| Tìm kiếm id / name / position | `GET /employees/search` | ✓ | ✓ | ✓ |
| Tìm theo **lương** | `GET /employees/search?minSalary=...` | ✗ (403) | ✓ | ✓ |
| Xem **cột Lương** (danh sách, kết quả tìm, chi tiết) | `GET /employees`, `/employees/search`, `/employees/{id}` | ✗ (ẩn) | ✓ | ✓ |
| Chi tiết | `GET /employees/{id}` | ✓ | ✓ | ✓ |
| Form thêm / Thêm | `GET /employees/new`, `POST /employees` | ✗ | ✓ | ✓ |
| Form sửa / Cập nhật | `GET /employees/{id}/edit`, `POST /employees/{id}` | ✗ | ✓ | ✓ |
| Xóa | `POST /employees/{id}/delete` | ✗ | ✗ | ✓ |
| Thống kê | `GET /reports/**` | ✗ | ✓ | ✓ |

Người chưa đăng nhập luôn bị chuyển sang `/login`.
Dòng "Thống kê" là quyết định của bài (đề không nêu): báo cáo chứa lương trung bình / tổng lương nên không cho GUEST.

## 5. Các file Security
```
config/SecurityConfig.java            SecurityFilterChain, form login, logout, PasswordEncoder (NoOp)
entity/UserAccount.java               map bảng `user`
repository/UserAccountRepository.java findByUsername
service/MyUserDetailsService.java     đọc user từ DB cho Spring Security
controller/AuthController.java        GET /login
controller/EmployeeController.java    @PreAuthorize từng method + chặn GUEST tìm lương
controller/ReportController.java      @PreAuthorize ở cấp class
templates/login.html, error/403.html
templates/fragments/layout.html       sec:authorize ẩn/hiện nút, Đăng xuất (POST)
```

## 6. Luồng đăng nhập
```
Browser → SecurityFilterChain → (chưa đăng nhập) → /login
POST /login → AuthenticationManager → DaoAuthenticationProvider
            → MyUserDetailsService (đọc bảng user) → PasswordEncoder so khớp
            → SecurityContextHolder lưu Authentication (ROLE_xxx)
Request sau → @PreAuthorize trên controller → Service → Repository → MariaDB
```

## 7. Điểm cần nhớ
- **Authentication** = bạn là ai; **Authorization** = bạn được làm gì.
- Phân quyền chi tiết đặt ở `@PreAuthorize` (cần `@EnableMethodSecurity`); `sec:authorize` trong view chỉ **ẩn nút**, server vẫn phải chặn.
- **CSRF** bật mặc định: form dùng `th:action` tự gắn token; đăng xuất phải là `POST /logout`.
- Cần `permitAll` cho `/error`, nếu không trang 403/404 bị chuyển về `/login`.
- Spring Security 6 bắt buộc có `PasswordEncoder`. Bài này dùng `NoOpPasswordEncoder` vì mật khẩu thô; muốn BCrypt thì đổi bean thành `new BCryptPasswordEncoder()` và lưu hash vào DB.

## 8. Lỗi thường gặp
| Lỗi | Cách xử lý |
|---|---|
| `There is no PasswordEncoder mapped for the id "null"` | Thiếu bean `PasswordEncoder` (hoặc DB lưu thô nhưng dùng encoder khác) |
| `Table 'employeedb.user' doesn't exist` | Chưa chạy `database/user.sql` |
| Đăng nhập đúng vẫn báo sai | Kiểm tra cột `role` là `ADMIN`/`USER`/`GUEST`, không phải `ROLE_ADMIN` |
| Bấm Đăng xuất bị 403 / 405 | Đăng xuất phải là form `POST` có CSRF, không dùng link `GET` |
| Trang 403 bị đẩy về `/login` | Thiếu `.requestMatchers("/error").permitAll()` |

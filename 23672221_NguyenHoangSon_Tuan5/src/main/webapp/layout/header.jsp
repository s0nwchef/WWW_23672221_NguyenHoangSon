<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${param.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="banner">
    <div class="container">
        <h1>Quản lý tin tức trực tuyến</h1>
        <div class="nav">
            <a href="${pageContext.request.contextPath}/danhsachtintuc">Danh sách tin tức</a>
            <a href="${pageContext.request.contextPath}/tintucform">Thêm tin tức</a>
            <a href="${pageContext.request.contextPath}/quanly">Quản lý (xóa tin)</a>
        </div>
    </div>
</div>
<div class="container">

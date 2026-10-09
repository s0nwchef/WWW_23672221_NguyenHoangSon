<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Quản lý tin tức"/>
</jsp:include>

<h2>Quản lý tin tức</h2>
<a href="${pageContext.request.contextPath}/tintucform">Thêm tin tức</a>
<br/>
<table class="table table-primary">
    <tr>
        <th>Mã TT</th>
        <th>Tiêu đề</th>
        <th>Danh mục</th>
        <th>Liên kết</th>
        <th>Action</th>
    </tr>

    <c:forEach var="tt" items="${tinTucs}">
        <tr>
            <td>${tt.maTT}</td>
            <td><c:out value="${tt.tieuDe}"/></td>
            <td><c:out value="${tt.tenDanhMuc}"/></td>
            <td><c:out value="${tt.lienKet}"/></td>
            <td>
                <a href="${pageContext.request.contextPath}/quanly?action=delete&maTT=${tt.maTT}"
                   onclick="return confirm('Bạn có chắc muốn hủy tin tức này?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty tinTucs}">
        <tr><td colspan="5" class="text-center">Không có tin tức nào</td></tr>
    </c:if>
</table>
<a href="${pageContext.request.contextPath}/danhsachtintuc">Danh sách tin tức</a>

<jsp:include page="/layout/footer.jsp"/>

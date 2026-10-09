<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Danh sách tin tức"/>
</jsp:include>

<h2>Danh sách tin tức</h2>
<a href="${pageContext.request.contextPath}/tintucform">Thêm tin tức</a>
<br/>
<div class="cat-links my-3">
    <a class="btn btn-sm ${empty param.maDM ? 'btn-primary' : 'btn-outline-primary'}"
       href="${pageContext.request.contextPath}/danhsachtintuc">Tất cả</a>
    <c:forEach var="dm" items="${danhMucs}">
        <a class="btn btn-sm ${param.maDM == dm.maDM ? 'btn-primary' : 'btn-outline-primary'}"
           href="${pageContext.request.contextPath}/danhsachtintuc?maDM=${dm.maDM}">
            <c:out value="${dm.tenDanhMuc}"/>
        </a>
    </c:forEach>
</div>

<table class="table table-primary">
    <tr>
        <th>Mã TT</th>
        <th>Tiêu đề</th>
        <th>Nội dung</th>
        <th>Liên kết</th>
        <th>Danh mục</th>
    </tr>

    <c:forEach var="tt" items="${tinTucs}">
        <tr>
            <td>${tt.maTT}</td>
            <td><c:out value="${tt.tieuDe}"/></td>
            <td><c:out value="${tt.noiDungTT}"/></td>
            <td><a href="<c:out value='${tt.lienKet}'/>" target="_blank"><c:out value="${tt.lienKet}"/></a></td>
            <td><c:out value="${tt.tenDanhMuc}"/></td>
        </tr>
    </c:forEach>
    <c:if test="${empty tinTucs}">
        <tr><td colspan="5" class="text-center">Không có tin tức nào</td></tr>
    </c:if>
</table>
<a href="${pageContext.request.contextPath}/quanly">Quản lý tin tức</a>

<jsp:include page="/layout/footer.jsp"/>

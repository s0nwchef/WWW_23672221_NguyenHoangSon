<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/layout/header.jsp">
    <jsp:param name="title" value="Thêm tin tức"/>
</jsp:include>

<h2>Thông tin tin tức</h2>

<c:if test="${not empty error}">
    <div class="alert alert-danger"><c:out value="${error}"/></div>
</c:if>

<form id="tinTucForm" action="${pageContext.request.contextPath}/tintucform" method="post"
      onsubmit="return validateForm();" novalidate>
    <div class="mb-2">
        Mã TT: <input type="text" name="maTT" id="maTT" value="<c:out value='${param.maTT}'/>"/>
        <span class="error-msg" id="err-maTT"></span>
    </div>
    <div class="mb-2">
        Tiêu đề: <input type="text" name="tieuDe" id="tieuDe" maxlength="200" size="60" value="<c:out value='${param.tieuDe}'/>"/>
        <span class="error-msg" id="err-tieuDe"></span>
    </div>
    <div class="mb-2">
        Liên kết: <input type="text" name="lienKet" id="lienKet" maxlength="200" size="60" value="<c:out value='${param.lienKet}'/>"/>
        <span class="error-msg" id="err-lienKet"></span>
    </div>
    <div class="mb-2">
        Nội dung: <br/>
        <textarea name="noiDungTT" id="noiDungTT" rows="5" cols="70"><c:out value="${param.noiDungTT}"/></textarea>
        <span class="error-msg" id="err-noiDungTT"></span>
    </div>
    <div class="mb-2">
        Danh mục:
        <select name="maDM" id="maDM">
            <option value="">-- Chọn danh mục --</option>
            <c:forEach var="dm" items="${danhMucs}">
                <option value="${dm.maDM}" ${param.maDM == dm.maDM ? 'selected' : ''}>
                    <c:out value="${dm.tenDanhMuc}"/>
                </option>
            </c:forEach>
        </select>
        <span class="error-msg" id="err-maDM"></span>
    </div>

    <input type="submit" value="Thêm"/>
    <input type="reset" value="Nhập lại"/>
</form>
<br/>
<a href="${pageContext.request.contextPath}/danhsachtintuc">Danh sách tin tức</a>

<script>
    // Kiểm tra dữ liệu phía Client bằng Regular Expression
    var reMaTT = /^\d+$/;               // Mã TT: số
    var reLienKet = /^http:\/\/\S+$/;   // Liên kết bắt đầu bởi http://
    var reNoiDung = /^[\s\S]{1,255}$/;  // Nội dung không quá 255 ký tự (và không rỗng)

    function check(id, ok, msg) {
        document.getElementById('err-' + id).textContent = ok ? '' : msg;
        return ok;
    }

    function validateForm() {
        var f = document.getElementById('tinTucForm');
        var maTT = f.maTT.value.trim();
        var tieuDe = f.tieuDe.value.trim();
        var lienKet = f.lienKet.value.trim();
        var noiDung = f.noiDungTT.value;
        var maDM = f.maDM.value;

        var ok = true;
        ok = check('maTT', maTT !== '' && reMaTT.test(maTT), maTT === '' ? 'Mã TT là bắt buộc' : 'Mã TT phải là số') && ok;
        ok = check('tieuDe', tieuDe !== '', 'Tiêu đề là bắt buộc') && ok;
        ok = check('lienKet', lienKet !== '' && reLienKet.test(lienKet),
                   lienKet === '' ? 'Liên kết là bắt buộc' : 'Liên kết phải bắt đầu bằng http://') && ok;
        ok = check('noiDungTT', noiDung.trim() !== '' && reNoiDung.test(noiDung),
                   noiDung.trim() === '' ? 'Nội dung là bắt buộc' : 'Nội dung không quá 255 ký tự') && ok;
        ok = check('maDM', maDM !== '', 'Danh mục là bắt buộc') && ok;
        return ok;
    }
</script>

<jsp:include page="/layout/footer.jsp"/>

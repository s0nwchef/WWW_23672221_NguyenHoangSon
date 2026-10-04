package org.example.bai6_REST.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.bai6_REST.dao.DanhMucDAO;
import org.example.bai6_REST.dao.TinTucDAO;
import org.example.bai6_REST.model.TinTuc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Path("/tintuc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TinTucResource {

    // Liên kết phải bắt đầu bằng http:// (hoặc https://, vì dữ liệu mẫu dùng https://)
    private static final Pattern LIEN_KET_PATTERN =
            Pattern.compile("^https?://\\S+$");

    // Nội dung không quá 255 ký tự (và không rỗng)
    private static final Pattern NOI_DUNG_PATTERN =
            Pattern.compile("^.{1,255}$", Pattern.DOTALL);

    private final TinTucDAO dao = new TinTucDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();

    // GET /api/tintuc
    @GET
    public Response getAll() {

        try {
            return Response.ok(dao.getAll()).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    // GET /api/tintuc/1
    @GET
    @Path("/{id: \\d+}")
    public Response getById(@PathParam("id") int id) {

        try {

            TinTuc tinTuc = dao.getById(id);

            if (tinTuc == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Khong tim thay tin tuc")
                        .build();
            }

            return Response.ok(tinTuc).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    // GET /api/tintuc/danhmuc/1
    @GET
    @Path("/danhmuc/{maDM: \\d+}")
    public Response getByDanhMuc(
            @PathParam("maDM") int maDM) {

        try {

            if (danhMucDAO.getById(maDM) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Khong tim thay danh muc")
                        .build();
            }

            return Response.ok(
                    dao.getByDanhMuc(maDM)
            ).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    // POST /api/tintuc
    @POST
    public Response insert(TinTuc tinTuc) {

        try {

            List<String> errors = validate(tinTuc);

            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(String.join("; ", errors))
                        .build();
            }

            // Danh mục phải tồn tại (tránh lỗi khóa ngoại 500)
            if (danhMucDAO.getById(tinTuc.getDanhMuc().getMaDM()) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Khong tim thay danh muc")
                        .build();
            }

            // Mã TT nhập trùng với tin đã có
            if (tinTuc.getMaTT() > 0 && dao.getById(tinTuc.getMaTT()) != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Ma tin tuc da ton tai")
                        .build();
            }

            dao.add(tinTuc);

            // Trả về đầy đủ thông tin (kèm danh mục) giống GET
            return Response.status(Response.Status.CREATED)
                    .entity(dao.getById(tinTuc.getMaTT()))
                    .build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    // DELETE /api/tintuc/1
    @DELETE
    @Path("/{id: \\d+}")
    public Response delete(@PathParam("id") int id) {

        try {

            if (dao.getById(id) == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Khong tim thay tin tuc")
                        .build();
            }

            dao.delete(id);

            return Response.ok("Xoa tin tuc thanh cong").build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }

    // Kiểm tra dữ liệu nhập: tiêu đề, liên kết, nội dung, danh mục bắt buộc
    private List<String> validate(TinTuc tinTuc) {

        List<String> errors = new ArrayList<>();

        if (tinTuc == null) {
            errors.add("Du lieu tin tuc khong duoc de trong");
            return errors;
        }

        if (tinTuc.getTieuDe() == null || tinTuc.getTieuDe().trim().isEmpty()) {
            errors.add("Tieu de la bat buoc");
        } else if (tinTuc.getTieuDe().length() > 200) {
            errors.add("Tieu de khong qua 200 ky tu");
        }

        if (tinTuc.getLienKet() == null || tinTuc.getLienKet().trim().isEmpty()) {
            errors.add("Lien ket la bat buoc");
        } else if (!LIEN_KET_PATTERN.matcher(tinTuc.getLienKet().trim()).matches()
                || tinTuc.getLienKet().trim().length() > 200) {
            errors.add("Lien ket phai bat dau bang http:// (toi da 200 ky tu)");
        }

        if (tinTuc.getNoiDungTT() == null || tinTuc.getNoiDungTT().trim().isEmpty()) {
            errors.add("Noi dung la bat buoc");
        } else if (!NOI_DUNG_PATTERN.matcher(tinTuc.getNoiDungTT()).matches()) {
            errors.add("Noi dung khong qua 255 ky tu");
        }

        if (tinTuc.getDanhMuc() == null || tinTuc.getDanhMuc().getMaDM() <= 0) {
            errors.add("Thong tin danh muc la bat buoc");
        }

        return errors;
    }
}

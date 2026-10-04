package org.example.bai6_REST.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.bai6_REST.dao.DanhMucDAO;
import org.example.bai6_REST.model.DanhMuc;

import java.sql.SQLException;

@Path("/danhmuc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DanhMucResource {
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();

    // GET /api/danhmuc
    @GET
    public Response getAll(){
        try {
            return Response.ok(danhMucDAO.getAll()).build();
        } catch (Exception e){
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // GET /api/danhmuc/1
    @GET
    @Path("/{id: \\d+}")
    public Response getById(@PathParam("id") int id){
        try {
            DanhMuc danhMuc = danhMucDAO.getById(id);

            if (danhMuc == null){
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Khong tim thay danh muc")
                        .build();
            }

            return Response.ok(danhMuc).build();

        } catch (SQLException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}

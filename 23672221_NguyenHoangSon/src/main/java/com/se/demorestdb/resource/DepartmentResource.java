package com.se.demorestdb.resource;

import com.se.demorestdb.model.Department;
import com.se.demorestdb.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/department")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResource {

    @Inject
    private DepartmentService departmentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        return Response.ok(departments).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentById(@PathParam("id") int id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(department).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDepartment(Department department) {
        Department newDepartment = departmentService.addDepartment(department);
        return Response.status(Response.Status.CREATED).entity(newDepartment).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(@PathParam("id") int id, Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        if (updatedDepartment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedDepartment).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDepartment(@PathParam("id") int id) {
        try {
            if (!departmentService.deleteDepartment(id)) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.noContent().build();
        } catch (RuntimeException e) {
            // Phòng ban còn nhân viên -> vi phạm khóa ngoại
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Không thể xóa phòng ban đang có nhân viên\"}")
                    .build();
        }
    }
}

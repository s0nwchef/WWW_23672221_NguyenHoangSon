package org.example.bai6_REST;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.bai6_REST.util.DBUtil;

import java.sql.Connection;

@Path("/test-db")
public class TestDBResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String testDB() {

        try {
            Connection connection = DBUtil.getConnection();

            connection.close();

            return "Ket noi database thanh cong!";

        } catch (Exception e) {
            e.printStackTrace();

            return "Ket noi database that bai: " + e.getMessage();
        }
    }
}
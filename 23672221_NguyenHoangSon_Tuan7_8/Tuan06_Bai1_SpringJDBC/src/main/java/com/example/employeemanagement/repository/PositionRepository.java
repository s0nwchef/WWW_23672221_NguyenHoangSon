package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PositionRepository {

    private static final RowMapper<Position> ROW_MAPPER = (rs, rowNum) -> {
        Position p = new Position();
        p.setId(rs.getInt("id"));
        p.setTitle(rs.getString("title"));
        return p;
    };

    private final JdbcTemplate jdbc;

    public PositionRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Position> findAll() {
        return jdbc.query("SELECT id, title FROM positions ORDER BY id", ROW_MAPPER);
    }
}

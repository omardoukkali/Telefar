package com.chatdesktop.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJdbcRepository<T, ID> implements Repository<T, ID> {

    private final String table;
    private final String columns;
    private final RowMapper<T> mapper;

    protected AbstractJdbcRepository(String table, String columns, RowMapper<T> mapper) {
        this.table = table;
        this.columns = columns;
        this.mapper = mapper;
    }

    protected String columns() {
        return columns;
    }

    protected List<T> queryList(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur de lecture dans " + table, e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        return queryList("SELECT " + columns + " FROM " + table + " WHERE id = ?", id)
                .stream().findFirst();
    }

    @Override
    public List<T> findAll() {
        return queryList("SELECT " + columns + " FROM " + table + " ORDER BY id");
    }

    @Override
    public boolean deleteById(ID id) {
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM " + table + " WHERE id = ?")) {
            ps.setObject(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur de suppression dans " + table, e);
        }
    }
}
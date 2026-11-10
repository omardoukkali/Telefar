package com.chatdesktop.server.db;

import com.chatdesktop.common.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao extends AbstractJdbcRepository<User, Long> {

    public UserDao() {
        super("users", "id, username",
                rs -> new User(rs.getLong("id"), rs.getString("username")));
    }

    public User create(String username, String passwordHash) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new User(rs.getLong("id"), username);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new UsernameAlreadyExistsException(username);
            }
            throw new DataAccessException("Erreur de création de l'utilisateur", e);
        }
    }

    public Optional<User> findByUsername(String username) {
        return queryList("SELECT " + columns() + " FROM users WHERE username = ?", username)
                .stream().findFirst();
    }

    public Optional<String> findPasswordHash(String username) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(rs.getString("password_hash")) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur de lecture du mot de passe", e);
        }
    }
}
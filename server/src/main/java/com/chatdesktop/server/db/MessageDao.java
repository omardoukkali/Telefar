package com.chatdesktop.server.db;

import com.chatdesktop.common.model.Message;
import com.chatdesktop.common.model.MessageStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageDao extends AbstractJdbcRepository<Message, Long> {

    public MessageDao() {
        super("messages", "id, sender_id, receiver_id, content, sent_at, status", MessageDao::map);
    }

    private static Message map(ResultSet rs) throws SQLException {
        return new Message(
                rs.getLong("id"),
                rs.getLong("sender_id"),
                rs.getLong("receiver_id"),
                rs.getString("content"),
                rs.getTimestamp("sent_at").toLocalDateTime(),
                MessageStatus.valueOf(rs.getString("status")));
    }

    public Message save(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, status) "
                + "VALUES (?, ?, ?, ?) RETURNING id, sent_at";
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, message.getSenderId());
            ps.setLong(2, message.getReceiverId());
            ps.setString(3, message.getContent());
            ps.setString(4, message.getStatus().name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Message(rs.getLong("id"), message.getSenderId(), message.getReceiverId(),
                        message.getContent(), rs.getTimestamp("sent_at").toLocalDateTime(),
                        message.getStatus());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur d'enregistrement du message", e);
        }
    }

    public List<Message> findConversation(long userA, long userB) {
        String sql = "SELECT " + columns() + " FROM messages "
                + "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) "
                + "ORDER BY sent_at, id";
        return queryList(sql, userA, userB, userB, userA);
    }

    public boolean updateStatus(long messageId, MessageStatus status) {
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement("UPDATE messages SET status = ? WHERE id = ?")) {
            ps.setString(1, status.name());
            ps.setLong(2, messageId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur de mise à jour du statut", e);
        }
    }
}
package com.chatdesktop.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    private final long id;
    private final long senderId;
    private final long receiverId;
    private final String content;
    private final LocalDateTime sentAt;
    private MessageStatus status;

    public Message(long id, long senderId, long receiverId, String content) {
        this(id, senderId, receiverId, content, LocalDateTime.now(), MessageStatus.SENT);
    }

    public Message(long id, long senderId, long receiverId, String content,
                   LocalDateTime sentAt, MessageStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentAt = sentAt;
        this.status = status;
    }

    public long getId() { return id; }
    public long getSenderId() { return senderId; }
    public long getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }
}



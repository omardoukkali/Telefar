package com.chatdesktop.common.model;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final long id;
    private final String username;

    public User(long id, String username) {
        this.id = id;
        this.username = username;
}
    public long getId() { return id; }
    public String getUsername() { return username; }

    @Override
    public String toString() { return username; }

}

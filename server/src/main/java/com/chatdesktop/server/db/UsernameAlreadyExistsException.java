package com.chatdesktop.server.db;

public class UsernameAlreadyExistsException extends DataAccessException {
    public UsernameAlreadyExistsException(String username) {
        super("Nom d'utilisateur déjà utilisé : " + username);
    }
}
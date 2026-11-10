package com.chatdesktop.server.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    @Test
    void verifyAcceptsCorrectPassword() {
        String stored = PasswordHasher.hash("MotDePasse123");
        assertTrue(PasswordHasher.verify("MotDePasse123", stored));
    }

    @Test
    void verifyRejectsWrongPassword() {
        String stored = PasswordHasher.hash("MotDePasse123");
        assertFalse(PasswordHasher.verify("autre", stored));
    }

    @Test
    void sameConfigurationProducesDifferentHashes() {
        assertNotEquals(PasswordHasher.hash("abc"), PasswordHasher.hash("abc"));
    }

    @Test
    void verifyRejectsMalformedStoredValue() {
        assertFalse(PasswordHasher.verify("abc", "n'importe quoi"));
    }
}
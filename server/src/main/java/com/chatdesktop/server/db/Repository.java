package com.chatdesktop.server.db;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    boolean deleteById(ID id);
}
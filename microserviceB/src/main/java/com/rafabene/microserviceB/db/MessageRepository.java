package com.rafabene.microserviceb.db;

import org.springframework.data.repository.CrudRepository;

/**
 * MessageRepository
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    
    
}
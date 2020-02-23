package com.rafabene.microserviceB;

import org.springframework.data.repository.CrudRepository;

/**
 * MessageRepository
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    
    
}
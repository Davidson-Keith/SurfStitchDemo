package com.keithdavidson.surfstitchdemo.db;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Uses JPA/Hibernate to automagically create all CRUD operations and other DB operations for the
 * Product table.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}

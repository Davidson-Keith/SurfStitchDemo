package com.keithdavidson.surfstitchdemo.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A JPA/Hibernate entity representing a row in the Product table of the DB.
 *
 * Note the use of the Lombok Project to create all boiler plate constructors, getters and setters.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Product {
    @Id
//    @GeneratedValue  // ID is NOT auto generated!
    @Column(name="ProductID")
    @JsonProperty("ProductID")
    private long id;

    @Column(name="ProductName")
    @JsonProperty("ProductName")
    private String name;

    @Column(name="ProductDescription")
    @JsonProperty("ProductDescription")
    private String description;
}

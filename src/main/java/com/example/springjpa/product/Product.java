package com.example.springjpa.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    private String name;
    private String description;
    private String category;
    private double price;

}

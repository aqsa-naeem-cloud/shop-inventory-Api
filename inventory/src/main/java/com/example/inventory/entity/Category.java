package com.example.inventory.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="Categories")
@Data
@NoArgsConstructor
public class Category {
     @Column(nullable = false, unique = true)
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



}

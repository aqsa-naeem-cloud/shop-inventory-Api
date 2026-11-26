package com.example.inventory.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name="Stockmovements")
public class Stockmove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
    @ManyToOne(fetch = FetchType.LAZY)
      private Product product;
    @Column(nullable = false)
      private int quantity;
      private String note;
    @CreationTimestamp
    @Column(updatable = false)
      private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

}

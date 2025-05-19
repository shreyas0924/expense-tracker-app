package com.expense.ledger.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Expense {

     @Id
     @Column(name = "id")
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     @Column(name = "external_id")
     private String externalId;

     @Column(name = "amount")
     private String amount;

     @Column(name = "user_id")
     private String userId;

     @Column(name = "merchant")
     private String merchant;

     @Column(name = "currency")
     private String currency;

     @Column(name = "created_at")
     private String createdAt;

     @PrePersist
     @PreUpdate
     private void generateExternalId() {
          if (this.externalId == null) {
               this.externalId = UUID.randomUUID().toString();
          }
     }
}

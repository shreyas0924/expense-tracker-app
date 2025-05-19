package com.expense.auth.entities;

import java.time.Instant;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Table(name= "tokens")
public class RefreshToken {
     
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private String token;
     private Instant expiryDate;

     @OneToOne
     @JoinColumn(name = "id", referencedColumnName = "user_id")
     UserInfo userInfo;
}

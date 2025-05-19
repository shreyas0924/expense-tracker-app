package com.expense.userservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserInfo {

     @Column(name = "id")
     private Long id; 

     @Id
     @JsonProperty("user_id")
     @NonNull
     @Column(name = "user_id", unique = true)
     private String userId;

     @JsonProperty("first_name")
     @NonNull
     @Column(name = "first_name")
     private String firstName;

     @JsonProperty("last_name")
     @NonNull
     @Column(name = "last_name")
     private String lastName;

     @JsonProperty("phone_number")
     @NonNull
     @Column(name = "phone_number")
     private Long phoneNumber;

     @JsonProperty("email")
     @NonNull
     @Column(name = "email", unique = true)
     private String email;

     @JsonProperty("profile_pic")
     @Column(name = "profile_pic")
     private String profilePic;
}
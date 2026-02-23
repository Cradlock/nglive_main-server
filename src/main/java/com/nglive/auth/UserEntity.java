package com.nglive.auth;

import java.time.LocalDateTime;

public class UserEntity{
  public enum Roles{
    CLIENT,
    ADMIN
  }
  
  public static String table_name = "users";

  private long id;
  private String username;
  private String password;
  private Roles role;
  private LocalDateTime createdAt;


  UserEntity() {}

  UserEntity(
    long id,
    String username,
    String password,
    Roles role,
    LocalDateTime createdAt
  ) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
    this.createdAt = createdAt;
  }


  

}

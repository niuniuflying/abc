package com.nac.abc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String username;
    @JsonIgnore
    private String password;
    private String dormitory;
    private String contact;
    private String avatar;
    private String reputation;
    private String isAdmin;
    private String createTime;
    private String updateTime;
    private String isDelete;
}

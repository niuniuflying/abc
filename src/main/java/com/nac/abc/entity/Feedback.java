package com.nac.abc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Integer id;
    private Integer userId;
    private String email;
    private String title;
    private String text;
    private String createTime;
    private String updateTime;
    private String isDelete;
}

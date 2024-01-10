package com.nac.abc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    private Integer id;
    private String email;
    private String title;
    private String news;
    private String createTime;
    private String updateTime;
    private String isDelete;
}

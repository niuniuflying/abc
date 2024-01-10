package com.nac.abc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    private Integer id;
    private Integer userId;
    private String name;
    private String time;
    private String description;
    private String status;
    private String createTime;
    private String updateTime;
    private Integer outdated;
    private String isDelete;
}

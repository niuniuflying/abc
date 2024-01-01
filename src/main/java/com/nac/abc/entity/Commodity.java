package com.nac.abc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    private Integer id;
    private String name;
    private String image;
    private String price;
    private String degree;
    private String description;
    private String type;
    private String status;
    private String createTime;
    private String updateTime;
    private String isDelete;
}

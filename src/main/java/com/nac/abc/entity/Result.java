package com.nac.abc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T>{
    private Integer code;
    private String msg;
    private T data;;
}

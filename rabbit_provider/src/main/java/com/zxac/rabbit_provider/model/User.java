package com.zxac.rabbit_provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String name;

    private String idCard;

    private String sex;

    private String road;

    private String tel;

    private String email;

    private Date createTime;

    private Date updateTime;


}

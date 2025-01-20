package com.dyrnq.sbs.common.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private String metaData;

    private T data;
}

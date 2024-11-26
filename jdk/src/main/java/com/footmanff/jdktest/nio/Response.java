package com.footmanff.jdktest.nio;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response implements Serializable {

    private static final long serialVersionUID = -1L;

    private String code;
    
    private String msg;

}

package com.footmanff.jdktest.nio;

import lombok.Data;
import java.io.Serializable;

@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -1L;
    
    private String content;
    
}

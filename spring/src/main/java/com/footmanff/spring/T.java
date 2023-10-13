package com.footmanff.spring;

import cn.hutool.core.io.file.FileReader;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class T {

    public static void main(String[] args) {
//        String p = "/Users/footmanff/Nutstore/我的坚果云/知识仓库/杂项/工作/weimob-work/项目/项目-06/fix_last.csv";
//        FileReader fileReader = new FileReader(p, Charset.defaultCharset());
//        List<String> list = new ArrayList<>();
//        fileReader.readLines(list);
//        
//        String sql = "update ";
//
//        for (String s : list) {
//            String[] a = s.split(",");
//            String am = a[0];
//            String no = a[1];
//            
//            
//        }

        for (int i = 0; i < 4; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }

}

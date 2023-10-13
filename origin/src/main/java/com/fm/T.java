package com.fm;

import java.util.ArrayList;
import java.util.List;

public class T {
    /**
     * 根据分片索引计算所有表序列
     *
     * @param totalTask     总任务数
     * @param shardingTotal 总分片数
     * @param shardingIndex 分片索引，从0开始
     * @return 分片索引针对的所有表序号，从0开始
     */
    private static List<Integer> calc(Integer totalTask, Integer shardingTotal, Integer shardingIndex) {
        int per = totalTask / shardingTotal;
        int last = totalTask % shardingTotal;

        List<Integer> result = new ArrayList<>();
        int first = shardingIndex * per;
        for (int i = 0; i < per; i++) {
            int idx = first + i;
            result.add(idx);
        }
        if (shardingIndex == shardingTotal - 1) {
            for (int i = totalTask - last; last > 0; i++, last--) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 根据分片索引计算所有表序列
     *
     * @param totalTask     总任务数
     * @param shardingTotal 总分片数
     * @param shardingIndex 分片索引，从0开始
     * @return 分片索引针对的所有表序号，从0开始
     */
    private static List<Integer> calc2(Integer totalTask, Integer shardingTotal, Integer shardingIndex) {
        int per = totalTask / shardingTotal;

        List<Integer> result = new ArrayList<>();
        int first = shardingIndex * per;
        for (int i = 1; i <= per; i++) {
            int idx = first + i - 1;
            result.add(idx);
        }

        int last = totalTask % shardingTotal;
        
        if (shardingIndex + 1 <= last) {
            // 剩余分片的第一个
            int lastFirstIdx = totalTask - last;
            
            result.add(lastFirstIdx + shardingIndex);
        }

        return result;
    }

    public static void main(String[] args) {
        int totalTask = 512;
        int shardingTotal = 5;
        for (int i = 0; i < shardingTotal; i++) {
            System.out.println(calc2(totalTask, shardingTotal, i));
        }
    }

}

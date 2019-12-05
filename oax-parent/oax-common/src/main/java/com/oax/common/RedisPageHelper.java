package com.oax.common;

/**
 * @Auther: hyp
 * @Date: 2019/1/15 10:44
 * @Description: 通过页数和每页个数计算开始位置和结束位置
 */
public class RedisPageHelper {

    public static int getStart(Integer pageNum, Integer pageSize) {
        int start = (pageNum - 1) * pageSize;
        return start;
    }

    public static int getEnd(Integer pageNum, Integer pageSize) {
        int start = getStart(pageNum, pageSize);
        int end = start + pageSize - 1;
        return end;
    }
}

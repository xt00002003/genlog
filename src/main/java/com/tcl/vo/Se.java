package com.tcl.vo;

/**
 * Created by tengxue on 16-9-7.
 * 对应日志中的se
 */
public class Se {
    private long s;
    private long e;

    public long getS() {
        return s;
    }

    public void setS(long s) {
        this.s = s;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "Se{" + "s=" + s + ", e=" + e + '}';
    }
}

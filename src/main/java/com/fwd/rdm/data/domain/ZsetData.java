package com.fwd.rdm.data.domain;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 16:39 2018/11/22
 */
public class ZsetData extends ListData {

    private double score;

    public ZsetData(Long index, String value, double score) {
        super(index, value);
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}

package com.tianji.dam.domain;

import lombok.Data;

/**
 * 碾压
 */
@Data
public class QualifiedRate {
    int alltime;//总的碾压
    int qualifiedtime;//合格碾压

    public QualifiedRate() {
    }

    public QualifiedRate(int alltime, int qualifiedtime) {
        this.alltime = alltime;
        this.qualifiedtime = qualifiedtime;
    }
}

package com.tianji.dam.domain;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ApiModel(description = "矩阵项")
@Data
public class MatrixItemGrid implements Serializable {

         List<Integer> items;

         List<Double> vcvvalue;


}

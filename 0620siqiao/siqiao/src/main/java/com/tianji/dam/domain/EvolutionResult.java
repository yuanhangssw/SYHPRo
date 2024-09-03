package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@ApiModel(description = "摊铺厚度遍数结果")
@Data
public class EvolutionResult {
    @ApiModelProperty(name = "time0", value = "合格", required = true, example = "2")
    private Integer time0 = 0;
    @ApiModelProperty(name = "time1", value = "低洼", required = true, example = "2")
    private Integer time1 = 0;
    @ApiModelProperty(name = "time2", value = "突出", required = true, example = "2")
    private Integer time2 = 0;


}

package com.tianji.dam.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@ApiModel(description = "碾压遍数结果")
@Data
public class RollingResult {
    @ApiModelProperty(name = "time0", value = "0次", required = true, example = "2")
    private Integer time0=0;
    @ApiModelProperty(name = "time1", value = "1次", required = true, example = "2")
    private Integer time1=0;
    @ApiModelProperty(name = "time2", value = "2次", required = true, example = "2")
    private Integer time2=0;
    @ApiModelProperty(name = "time3", value = "3次", required = true, example = "2")
    private Integer time3=0;
    @ApiModelProperty(name = "time4", value = "4次", required = true, example = "2")
    private Integer time4=0;
    @ApiModelProperty(name = "time5", value = "5次", required = true, example = "2")
    private Integer time5=0;
    @ApiModelProperty(name = "time6", value = "6次", required = true, example = "2")
    private Integer time6=0;
    @ApiModelProperty(name = "time7", value = "7次", required = true, example = "2")
    private Integer time7=0;
    @ApiModelProperty(name = "time8", value = "8次", required = true, example = "2")
    private Integer time8=0;
    @ApiModelProperty(name = "time9", value = "9次", required = true, example = "2")
    private Integer time9=0;
    @ApiModelProperty(name = "time10", value = "10次", required = true, example = "2")
    private Integer time10=0;
    @ApiModelProperty(name = "time11", value = "11次", required = true, example = "2")
    private Integer time11=0;
    @ApiModelProperty(name = "time11Up", value = "11次以上", required = true, example = "2")
    private Integer time11Up=0;

}

package com.ruoyi.system.domain.vo;

import java.util.List;

public class InspectorUnitVo {
    private Long inspectorId;
    private Long projectId;
    private List<Long> unit;

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<Long> getUnit() {
        return unit;
    }

    public void setUnit(List<Long> unit) {
        this.unit = unit;
    }
}

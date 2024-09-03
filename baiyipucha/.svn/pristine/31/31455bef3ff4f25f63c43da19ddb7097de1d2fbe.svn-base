package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import io.netty.handler.ssl.PemPrivateKey;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 普查处管理对象 b_patrol_unit_place
 * 
 * @author ruoyi
 * @date 2024-04-08
 */
public class BPatrolUnitPlaceAll extends BaseEntity
{
    private static final Long serialVersionUID = 1L;

    private String address;

    private String patrolUnit;


    private Long patrolType;

    private Long projectId;

    private Long inspectorId;

    private  int lon;

    private  int lat;

    private List<BPatrol> patrols;


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(Long inspectorId) {
        this.inspectorId = inspectorId;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }


    public String getPatrolUnit() {
        return patrolUnit;
    }

    public void setPatrolUnit(String patrolUnit) {
        this.patrolUnit = patrolUnit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




    public Long getPatrolType() {
        return patrolType;
    }

    public void setPatrolType(Long patrolType) {
        this.patrolType = patrolType;
    }



    public List<BPatrol> getPatrols() {
        return patrols;
    }

    public void setPatrols(List<BPatrol> patrols) {
        this.patrols = patrols;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("patrolUnit", getPatrolUnit())
                .append("address", getAddress())
                .append("patrolType", getPatrolType())
                .toString();
    }

}

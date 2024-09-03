package com.tianji.dam.domain;

import java.util.LinkedList;import java.util.ArrayList;
import java.util.List;

public class TTrackExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TTrackExample() {
        oredCriteria = new LinkedList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new LinkedList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCoordxIsNull() {
            addCriterion("CoordX is null");
            return (Criteria) this;
        }

        public Criteria andCoordxIsNotNull() {
            addCriterion("CoordX is not null");
            return (Criteria) this;
        }

        public Criteria andCoordxEqualTo(Double value) {
            addCriterion("CoordX =", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxNotEqualTo(Double value) {
            addCriterion("CoordX <>", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxGreaterThan(Double value) {
            addCriterion("CoordX >", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordX >=", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxLessThan(Double value) {
            addCriterion("CoordX <", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxLessThanOrEqualTo(Double value) {
            addCriterion("CoordX <=", value, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxIn(List<Double> values) {
            addCriterion("CoordX in", values, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxNotIn(List<Double> values) {
            addCriterion("CoordX not in", values, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxBetween(Double value1, Double value2) {
            addCriterion("CoordX between", value1, value2, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordxNotBetween(Double value1, Double value2) {
            addCriterion("CoordX not between", value1, value2, "coordx");
            return (Criteria) this;
        }

        public Criteria andCoordyIsNull() {
            addCriterion("CoordY is null");
            return (Criteria) this;
        }

        public Criteria andCoordyIsNotNull() {
            addCriterion("CoordY is not null");
            return (Criteria) this;
        }

        public Criteria andCoordyEqualTo(Double value) {
            addCriterion("CoordY =", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyNotEqualTo(Double value) {
            addCriterion("CoordY <>", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyGreaterThan(Double value) {
            addCriterion("CoordY >", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordY >=", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyLessThan(Double value) {
            addCriterion("CoordY <", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyLessThanOrEqualTo(Double value) {
            addCriterion("CoordY <=", value, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyIn(List<Double> values) {
            addCriterion("CoordY in", values, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyNotIn(List<Double> values) {
            addCriterion("CoordY not in", values, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyBetween(Double value1, Double value2) {
            addCriterion("CoordY between", value1, value2, "coordy");
            return (Criteria) this;
        }

        public Criteria andCoordyNotBetween(Double value1, Double value2) {
            addCriterion("CoordY not between", value1, value2, "coordy");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNull() {
            addCriterion("Latitude is null");
            return (Criteria) this;
        }

        public Criteria andLatitudeIsNotNull() {
            addCriterion("Latitude is not null");
            return (Criteria) this;
        }

        public Criteria andLatitudeEqualTo(Double value) {
            addCriterion("Latitude =", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotEqualTo(Double value) {
            addCriterion("Latitude <>", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThan(Double value) {
            addCriterion("Latitude >", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("Latitude >=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThan(Double value) {
            addCriterion("Latitude <", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeLessThanOrEqualTo(Double value) {
            addCriterion("Latitude <=", value, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeIn(List<Double> values) {
            addCriterion("Latitude in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotIn(List<Double> values) {
            addCriterion("Latitude not in", values, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeBetween(Double value1, Double value2) {
            addCriterion("Latitude between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLatitudeNotBetween(Double value1, Double value2) {
            addCriterion("Latitude not between", value1, value2, "latitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNull() {
            addCriterion("Longitude is null");
            return (Criteria) this;
        }

        public Criteria andLongitudeIsNotNull() {
            addCriterion("Longitude is not null");
            return (Criteria) this;
        }

        public Criteria andLongitudeEqualTo(Double value) {
            addCriterion("Longitude =", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotEqualTo(Double value) {
            addCriterion("Longitude <>", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThan(Double value) {
            addCriterion("Longitude >", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("Longitude >=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThan(Double value) {
            addCriterion("Longitude <", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeLessThanOrEqualTo(Double value) {
            addCriterion("Longitude <=", value, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeIn(List<Double> values) {
            addCriterion("Longitude in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotIn(List<Double> values) {
            addCriterion("Longitude not in", values, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeBetween(Double value1, Double value2) {
            addCriterion("Longitude between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andLongitudeNotBetween(Double value1, Double value2) {
            addCriterion("Longitude not between", value1, value2, "longitude");
            return (Criteria) this;
        }

        public Criteria andElevationIsNull() {
            addCriterion("Elevation is null");
            return (Criteria) this;
        }

        public Criteria andElevationIsNotNull() {
            addCriterion("Elevation is not null");
            return (Criteria) this;
        }

        public Criteria andElevationEqualTo(Float value) {
            addCriterion("Elevation =", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationNotEqualTo(Float value) {
            addCriterion("Elevation <>", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationGreaterThan(Float value) {
            addCriterion("Elevation >", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationGreaterThanOrEqualTo(Float value) {
            addCriterion("Elevation >=", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationLessThan(Float value) {
            addCriterion("Elevation <", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationLessThanOrEqualTo(Float value) {
            addCriterion("Elevation <=", value, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationIn(List<Float> values) {
            addCriterion("Elevation in", values, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationNotIn(List<Float> values) {
            addCriterion("Elevation not in", values, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationBetween(Float value1, Float value2) {
            addCriterion("Elevation between", value1, value2, "elevation");
            return (Criteria) this;
        }

        public Criteria andElevationNotBetween(Float value1, Float value2) {
            addCriterion("Elevation not between", value1, value2, "elevation");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("Timestamp is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("Timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Long value) {
            addCriterion("Timestamp =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Long value) {
            addCriterion("Timestamp <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Long value) {
            addCriterion("Timestamp >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Long value) {
            addCriterion("Timestamp >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Long value) {
            addCriterion("Timestamp <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Long value) {
            addCriterion("Timestamp <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Long> values) {
            addCriterion("Timestamp in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Long> values) {
            addCriterion("Timestamp not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Long value1, Long value2) {
            addCriterion("Timestamp between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Long value1, Long value2) {
            addCriterion("Timestamp not between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andVehicleidIsNull() {
            addCriterion("VehicleID is null");
            return (Criteria) this;
        }

        public Criteria andVehicleidIsNotNull() {
            addCriterion("VehicleID is not null");
            return (Criteria) this;
        }

        public Criteria andVehicleidEqualTo(String value) {
            addCriterion("VehicleID =", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidNotEqualTo(String value) {
            addCriterion("VehicleID <>", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidGreaterThan(String value) {
            addCriterion("VehicleID >", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidGreaterThanOrEqualTo(String value) {
            addCriterion("VehicleID >=", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidLessThan(String value) {
            addCriterion("VehicleID <", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidLessThanOrEqualTo(String value) {
            addCriterion("VehicleID <=", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidLike(String value) {
            addCriterion("VehicleID like", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidNotLike(String value) {
            addCriterion("VehicleID not like", value, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidIn(List<String> values) {
            addCriterion("VehicleID in", values, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidNotIn(List<String> values) {
            addCriterion("VehicleID not in", values, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidBetween(String value1, String value2) {
            addCriterion("VehicleID between", value1, value2, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andVehicleidNotBetween(String value1, String value2) {
            addCriterion("VehicleID not between", value1, value2, "vehicleid");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNull() {
            addCriterion("Speed is null");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNotNull() {
            addCriterion("Speed is not null");
            return (Criteria) this;
        }

        public Criteria andSpeedEqualTo(Float value) {
            addCriterion("Speed =", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotEqualTo(Float value) {
            addCriterion("Speed <>", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThan(Float value) {
            addCriterion("Speed >", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThanOrEqualTo(Float value) {
            addCriterion("Speed >=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThan(Float value) {
            addCriterion("Speed <", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThanOrEqualTo(Float value) {
            addCriterion("Speed <=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedIn(List<Float> values) {
            addCriterion("Speed in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotIn(List<Float> values) {
            addCriterion("Speed not in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedBetween(Float value1, Float value2) {
            addCriterion("Speed between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotBetween(Float value1, Float value2) {
            addCriterion("Speed not between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andLayeridIsNull() {
            addCriterion("LayerID is null");
            return (Criteria) this;
        }

        public Criteria andLayeridIsNotNull() {
            addCriterion("LayerID is not null");
            return (Criteria) this;
        }

        public Criteria andLayeridEqualTo(Integer value) {
            addCriterion("LayerID =", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridNotEqualTo(Integer value) {
            addCriterion("LayerID <>", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridGreaterThan(Integer value) {
            addCriterion("LayerID >", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridGreaterThanOrEqualTo(Integer value) {
            addCriterion("LayerID >=", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridLessThan(Integer value) {
            addCriterion("LayerID <", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridLessThanOrEqualTo(Integer value) {
            addCriterion("LayerID <=", value, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridIn(List<Integer> values) {
            addCriterion("LayerID in", values, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridNotIn(List<Integer> values) {
            addCriterion("LayerID not in", values, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridBetween(Integer value1, Integer value2) {
            addCriterion("LayerID between", value1, value2, "layerid");
            return (Criteria) this;
        }

        public Criteria andLayeridNotBetween(Integer value1, Integer value2) {
            addCriterion("LayerID not between", value1, value2, "layerid");
            return (Criteria) this;
        }

        public Criteria andIsforwardIsNull() {
            addCriterion("IsForward is null");
            return (Criteria) this;
        }

        public Criteria andIsforwardIsNotNull() {
            addCriterion("IsForward is not null");
            return (Criteria) this;
        }

        public Criteria andIsforwardEqualTo(Integer value) {
            addCriterion("IsForward =", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardNotEqualTo(Integer value) {
            addCriterion("IsForward <>", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardGreaterThan(Integer value) {
            addCriterion("IsForward >", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardGreaterThanOrEqualTo(Integer value) {
            addCriterion("IsForward >=", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardLessThan(Integer value) {
            addCriterion("IsForward <", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardLessThanOrEqualTo(Integer value) {
            addCriterion("IsForward <=", value, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardIn(List<Integer> values) {
            addCriterion("IsForward in", values, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardNotIn(List<Integer> values) {
            addCriterion("IsForward not in", values, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardBetween(Integer value1, Integer value2) {
            addCriterion("IsForward between", value1, value2, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsforwardNotBetween(Integer value1, Integer value2) {
            addCriterion("IsForward not between", value1, value2, "isforward");
            return (Criteria) this;
        }

        public Criteria andIsvibrateIsNull() {
            addCriterion("IsVibrate is null");
            return (Criteria) this;
        }

        public Criteria andIsvibrateIsNotNull() {
            addCriterion("IsVibrate is not null");
            return (Criteria) this;
        }

        public Criteria andIsvibrateEqualTo(Integer value) {
            addCriterion("IsVibrate =", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateNotEqualTo(Integer value) {
            addCriterion("IsVibrate <>", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateGreaterThan(Integer value) {
            addCriterion("IsVibrate >", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateGreaterThanOrEqualTo(Integer value) {
            addCriterion("IsVibrate >=", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateLessThan(Integer value) {
            addCriterion("IsVibrate <", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateLessThanOrEqualTo(Integer value) {
            addCriterion("IsVibrate <=", value, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateIn(List<Integer> values) {
            addCriterion("IsVibrate in", values, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateNotIn(List<Integer> values) {
            addCriterion("IsVibrate not in", values, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateBetween(Integer value1, Integer value2) {
            addCriterion("IsVibrate between", value1, value2, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andIsvibrateNotBetween(Integer value1, Integer value2) {
            addCriterion("IsVibrate not between", value1, value2, "isvibrate");
            return (Criteria) this;
        }

        public Criteria andVibratevalueIsNull() {
            addCriterion("VibrateValue is null");
            return (Criteria) this;
        }

        public Criteria andVibratevalueIsNotNull() {
            addCriterion("VibrateValue is not null");
            return (Criteria) this;
        }

        public Criteria andVibratevalueEqualTo(Double value) {
            addCriterion("VibrateValue =", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueNotEqualTo(Double value) {
            addCriterion("VibrateValue <>", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueGreaterThan(Double value) {
            addCriterion("VibrateValue >", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueGreaterThanOrEqualTo(Double value) {
            addCriterion("VibrateValue >=", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueLessThan(Double value) {
            addCriterion("VibrateValue <", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueLessThanOrEqualTo(Double value) {
            addCriterion("VibrateValue <=", value, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueIn(List<Double> values) {
            addCriterion("VibrateValue in", values, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueNotIn(List<Double> values) {
            addCriterion("VibrateValue not in", values, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueBetween(Double value1, Double value2) {
            addCriterion("VibrateValue between", value1, value2, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andVibratevalueNotBetween(Double value1, Double value2) {
            addCriterion("VibrateValue not between", value1, value2, "vibratevalue");
            return (Criteria) this;
        }

        public Criteria andFrequencyIsNull() {
            addCriterion("Frequency is null");
            return (Criteria) this;
        }

        public Criteria andFrequencyIsNotNull() {
            addCriterion("Frequency is not null");
            return (Criteria) this;
        }

        public Criteria andFrequencyEqualTo(Double value) {
            addCriterion("Frequency =", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyNotEqualTo(Double value) {
            addCriterion("Frequency <>", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyGreaterThan(Double value) {
            addCriterion("Frequency >", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyGreaterThanOrEqualTo(Double value) {
            addCriterion("Frequency >=", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyLessThan(Double value) {
            addCriterion("Frequency <", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyLessThanOrEqualTo(Double value) {
            addCriterion("Frequency <=", value, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyIn(List<Double> values) {
            addCriterion("Frequency in", values, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyNotIn(List<Double> values) {
            addCriterion("Frequency not in", values, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyBetween(Double value1, Double value2) {
            addCriterion("Frequency between", value1, value2, "frequency");
            return (Criteria) this;
        }

        public Criteria andFrequencyNotBetween(Double value1, Double value2) {
            addCriterion("Frequency not between", value1, value2, "frequency");
            return (Criteria) this;
        }

        public Criteria andAccelerationIsNull() {
            addCriterion("Acceleration is null");
            return (Criteria) this;
        }

        public Criteria andAccelerationIsNotNull() {
            addCriterion("Acceleration is not null");
            return (Criteria) this;
        }

        public Criteria andAccelerationEqualTo(Double value) {
            addCriterion("Acceleration =", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationNotEqualTo(Double value) {
            addCriterion("Acceleration <>", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationGreaterThan(Double value) {
            addCriterion("Acceleration >", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationGreaterThanOrEqualTo(Double value) {
            addCriterion("Acceleration >=", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationLessThan(Double value) {
            addCriterion("Acceleration <", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationLessThanOrEqualTo(Double value) {
            addCriterion("Acceleration <=", value, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationIn(List<Double> values) {
            addCriterion("Acceleration in", values, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationNotIn(List<Double> values) {
            addCriterion("Acceleration not in", values, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationBetween(Double value1, Double value2) {
            addCriterion("Acceleration between", value1, value2, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAccelerationNotBetween(Double value1, Double value2) {
            addCriterion("Acceleration not between", value1, value2, "acceleration");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIsNull() {
            addCriterion("Amplitude is null");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIsNotNull() {
            addCriterion("Amplitude is not null");
            return (Criteria) this;
        }

        public Criteria andAmplitudeEqualTo(Double value) {
            addCriterion("Amplitude =", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotEqualTo(Double value) {
            addCriterion("Amplitude <>", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeGreaterThan(Double value) {
            addCriterion("Amplitude >", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeGreaterThanOrEqualTo(Double value) {
            addCriterion("Amplitude >=", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeLessThan(Double value) {
            addCriterion("Amplitude <", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeLessThanOrEqualTo(Double value) {
            addCriterion("Amplitude <=", value, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeIn(List<Double> values) {
            addCriterion("Amplitude in", values, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotIn(List<Double> values) {
            addCriterion("Amplitude not in", values, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeBetween(Double value1, Double value2) {
            addCriterion("Amplitude between", value1, value2, "amplitude");
            return (Criteria) this;
        }

        public Criteria andAmplitudeNotBetween(Double value1, Double value2) {
            addCriterion("Amplitude not between", value1, value2, "amplitude");
            return (Criteria) this;
        }

        public Criteria andSatellitesIsNull() {
            addCriterion("Satellites is null");
            return (Criteria) this;
        }

        public Criteria andSatellitesIsNotNull() {
            addCriterion("Satellites is not null");
            return (Criteria) this;
        }

        public Criteria andSatellitesEqualTo(Integer value) {
            addCriterion("Satellites =", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesNotEqualTo(Integer value) {
            addCriterion("Satellites <>", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesGreaterThan(Integer value) {
            addCriterion("Satellites >", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesGreaterThanOrEqualTo(Integer value) {
            addCriterion("Satellites >=", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesLessThan(Integer value) {
            addCriterion("Satellites <", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesLessThanOrEqualTo(Integer value) {
            addCriterion("Satellites <=", value, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesIn(List<Integer> values) {
            addCriterion("Satellites in", values, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesNotIn(List<Integer> values) {
            addCriterion("Satellites not in", values, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesBetween(Integer value1, Integer value2) {
            addCriterion("Satellites between", value1, value2, "satellites");
            return (Criteria) this;
        }

        public Criteria andSatellitesNotBetween(Integer value1, Integer value2) {
            addCriterion("Satellites not between", value1, value2, "satellites");
            return (Criteria) this;
        }

        public Criteria andZhuangxIsNull() {
            addCriterion("ZhuangX is null");
            return (Criteria) this;
        }

        public Criteria andZhuangxIsNotNull() {
            addCriterion("ZhuangX is not null");
            return (Criteria) this;
        }

        public Criteria andZhuangxEqualTo(Double value) {
            addCriterion("ZhuangX =", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxNotEqualTo(Double value) {
            addCriterion("ZhuangX <>", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxGreaterThan(Double value) {
            addCriterion("ZhuangX >", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxGreaterThanOrEqualTo(Double value) {
            addCriterion("ZhuangX >=", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxLessThan(Double value) {
            addCriterion("ZhuangX <", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxLessThanOrEqualTo(Double value) {
            addCriterion("ZhuangX <=", value, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxIn(List<Double> values) {
            addCriterion("ZhuangX in", values, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxNotIn(List<Double> values) {
            addCriterion("ZhuangX not in", values, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxBetween(Double value1, Double value2) {
            addCriterion("ZhuangX between", value1, value2, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangxNotBetween(Double value1, Double value2) {
            addCriterion("ZhuangX not between", value1, value2, "zhuangx");
            return (Criteria) this;
        }

        public Criteria andZhuangyIsNull() {
            addCriterion("ZhuangY is null");
            return (Criteria) this;
        }

        public Criteria andZhuangyIsNotNull() {
            addCriterion("ZhuangY is not null");
            return (Criteria) this;
        }

        public Criteria andZhuangyEqualTo(Double value) {
            addCriterion("ZhuangY =", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyNotEqualTo(Double value) {
            addCriterion("ZhuangY <>", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyGreaterThan(Double value) {
            addCriterion("ZhuangY >", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyGreaterThanOrEqualTo(Double value) {
            addCriterion("ZhuangY >=", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyLessThan(Double value) {
            addCriterion("ZhuangY <", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyLessThanOrEqualTo(Double value) {
            addCriterion("ZhuangY <=", value, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyIn(List<Double> values) {
            addCriterion("ZhuangY in", values, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyNotIn(List<Double> values) {
            addCriterion("ZhuangY not in", values, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyBetween(Double value1, Double value2) {
            addCriterion("ZhuangY between", value1, value2, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andZhuangyNotBetween(Double value1, Double value2) {
            addCriterion("ZhuangY not between", value1, value2, "zhuangy");
            return (Criteria) this;
        }

        public Criteria andIshistoryIsNull() {
            addCriterion("ishistory is null");
            return (Criteria) this;
        }

        public Criteria andIshistoryIsNotNull() {
            addCriterion("ishistory is not null");
            return (Criteria) this;
        }

        public Criteria andIshistoryEqualTo(Integer value) {
            addCriterion("ishistory =", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryNotEqualTo(Integer value) {
            addCriterion("ishistory <>", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryGreaterThan(Integer value) {
            addCriterion("ishistory >", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("ishistory >=", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryLessThan(Integer value) {
            addCriterion("ishistory <", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryLessThanOrEqualTo(Integer value) {
            addCriterion("ishistory <=", value, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryIn(List<Integer> values) {
            addCriterion("ishistory in", values, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryNotIn(List<Integer> values) {
            addCriterion("ishistory not in", values, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryBetween(Integer value1, Integer value2) {
            addCriterion("ishistory between", value1, value2, "ishistory");
            return (Criteria) this;
        }

        public Criteria andIshistoryNotBetween(Integer value1, Integer value2) {
            addCriterion("ishistory not between", value1, value2, "ishistory");
            return (Criteria) this;
        }

        public Criteria andMaterialnameIsNull() {
            addCriterion("materialname is null");
            return (Criteria) this;
        }

        public Criteria andMaterialnameIsNotNull() {
            addCriterion("materialname is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialnameEqualTo(Integer value) {
            addCriterion("materialname =", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameNotEqualTo(Integer value) {
            addCriterion("materialname <>", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameGreaterThan(Integer value) {
            addCriterion("materialname >", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameGreaterThanOrEqualTo(Integer value) {
            addCriterion("materialname >=", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameLessThan(Integer value) {
            addCriterion("materialname <", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameLessThanOrEqualTo(Integer value) {
            addCriterion("materialname <=", value, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameIn(List<Integer> values) {
            addCriterion("materialname in", values, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameNotIn(List<Integer> values) {
            addCriterion("materialname not in", values, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameBetween(Integer value1, Integer value2) {
            addCriterion("materialname between", value1, value2, "materialname");
            return (Criteria) this;
        }

        public Criteria andMaterialnameNotBetween(Integer value1, Integer value2) {
            addCriterion("materialname not between", value1, value2, "materialname");
            return (Criteria) this;
        }

        public Criteria andOrdernumIsNull() {
            addCriterion("OrderNum is null");
            return (Criteria) this;
        }

        public Criteria andOrdernumIsNotNull() {
            addCriterion("OrderNum is not null");
            return (Criteria) this;
        }

        public Criteria andOrdernumEqualTo(Integer value) {
            addCriterion("OrderNum =", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotEqualTo(Integer value) {
            addCriterion("OrderNum <>", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumGreaterThan(Integer value) {
            addCriterion("OrderNum >", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumGreaterThanOrEqualTo(Integer value) {
            addCriterion("OrderNum >=", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumLessThan(Integer value) {
            addCriterion("OrderNum <", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumLessThanOrEqualTo(Integer value) {
            addCriterion("OrderNum <=", value, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumIn(List<Integer> values) {
            addCriterion("OrderNum in", values, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotIn(List<Integer> values) {
            addCriterion("OrderNum not in", values, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumBetween(Integer value1, Integer value2) {
            addCriterion("OrderNum between", value1, value2, "ordernum");
            return (Criteria) this;
        }

        public Criteria andOrdernumNotBetween(Integer value1, Integer value2) {
            addCriterion("OrderNum not between", value1, value2, "ordernum");
            return (Criteria) this;
        }

        public Criteria andAngleIsNull() {
            addCriterion("Angle is null");
            return (Criteria) this;
        }

        public Criteria andAngleIsNotNull() {
            addCriterion("Angle is not null");
            return (Criteria) this;
        }

        public Criteria andAngleEqualTo(Float value) {
            addCriterion("Angle =", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotEqualTo(Float value) {
            addCriterion("Angle <>", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleGreaterThan(Float value) {
            addCriterion("Angle >", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleGreaterThanOrEqualTo(Float value) {
            addCriterion("Angle >=", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleLessThan(Float value) {
            addCriterion("Angle <", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleLessThanOrEqualTo(Float value) {
            addCriterion("Angle <=", value, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleIn(List<Float> values) {
            addCriterion("Angle in", values, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotIn(List<Float> values) {
            addCriterion("Angle not in", values, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleBetween(Float value1, Float value2) {
            addCriterion("Angle between", value1, value2, "angle");
            return (Criteria) this;
        }

        public Criteria andAngleNotBetween(Float value1, Float value2) {
            addCriterion("Angle not between", value1, value2, "angle");
            return (Criteria) this;
        }

        public Criteria andCoordlxIsNull() {
            addCriterion("CoordLX is null");
            return (Criteria) this;
        }

        public Criteria andCoordlxIsNotNull() {
            addCriterion("CoordLX is not null");
            return (Criteria) this;
        }

        public Criteria andCoordlxEqualTo(Double value) {
            addCriterion("CoordLX =", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxNotEqualTo(Double value) {
            addCriterion("CoordLX <>", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxGreaterThan(Double value) {
            addCriterion("CoordLX >", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordLX >=", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxLessThan(Double value) {
            addCriterion("CoordLX <", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxLessThanOrEqualTo(Double value) {
            addCriterion("CoordLX <=", value, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxIn(List<Double> values) {
            addCriterion("CoordLX in", values, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxNotIn(List<Double> values) {
            addCriterion("CoordLX not in", values, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxBetween(Double value1, Double value2) {
            addCriterion("CoordLX between", value1, value2, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlxNotBetween(Double value1, Double value2) {
            addCriterion("CoordLX not between", value1, value2, "coordlx");
            return (Criteria) this;
        }

        public Criteria andCoordlyIsNull() {
            addCriterion("CoordLY is null");
            return (Criteria) this;
        }

        public Criteria andCoordlyIsNotNull() {
            addCriterion("CoordLY is not null");
            return (Criteria) this;
        }

        public Criteria andCoordlyEqualTo(Double value) {
            addCriterion("CoordLY =", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyNotEqualTo(Double value) {
            addCriterion("CoordLY <>", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyGreaterThan(Double value) {
            addCriterion("CoordLY >", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordLY >=", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyLessThan(Double value) {
            addCriterion("CoordLY <", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyLessThanOrEqualTo(Double value) {
            addCriterion("CoordLY <=", value, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyIn(List<Double> values) {
            addCriterion("CoordLY in", values, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyNotIn(List<Double> values) {
            addCriterion("CoordLY not in", values, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyBetween(Double value1, Double value2) {
            addCriterion("CoordLY between", value1, value2, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordlyNotBetween(Double value1, Double value2) {
            addCriterion("CoordLY not between", value1, value2, "coordly");
            return (Criteria) this;
        }

        public Criteria andCoordrxIsNull() {
            addCriterion("CoordRX is null");
            return (Criteria) this;
        }

        public Criteria andCoordrxIsNotNull() {
            addCriterion("CoordRX is not null");
            return (Criteria) this;
        }

        public Criteria andCoordrxEqualTo(Double value) {
            addCriterion("CoordRX =", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxNotEqualTo(Double value) {
            addCriterion("CoordRX <>", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxGreaterThan(Double value) {
            addCriterion("CoordRX >", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordRX >=", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxLessThan(Double value) {
            addCriterion("CoordRX <", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxLessThanOrEqualTo(Double value) {
            addCriterion("CoordRX <=", value, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxIn(List<Double> values) {
            addCriterion("CoordRX in", values, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxNotIn(List<Double> values) {
            addCriterion("CoordRX not in", values, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxBetween(Double value1, Double value2) {
            addCriterion("CoordRX between", value1, value2, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordrxNotBetween(Double value1, Double value2) {
            addCriterion("CoordRX not between", value1, value2, "coordrx");
            return (Criteria) this;
        }

        public Criteria andCoordryIsNull() {
            addCriterion("CoordRY is null");
            return (Criteria) this;
        }

        public Criteria andCoordryIsNotNull() {
            addCriterion("CoordRY is not null");
            return (Criteria) this;
        }

        public Criteria andCoordryEqualTo(Double value) {
            addCriterion("CoordRY =", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryNotEqualTo(Double value) {
            addCriterion("CoordRY <>", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryGreaterThan(Double value) {
            addCriterion("CoordRY >", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryGreaterThanOrEqualTo(Double value) {
            addCriterion("CoordRY >=", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryLessThan(Double value) {
            addCriterion("CoordRY <", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryLessThanOrEqualTo(Double value) {
            addCriterion("CoordRY <=", value, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryIn(List<Double> values) {
            addCriterion("CoordRY in", values, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryNotIn(List<Double> values) {
            addCriterion("CoordRY not in", values, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryBetween(Double value1, Double value2) {
            addCriterion("CoordRY between", value1, value2, "coordry");
            return (Criteria) this;
        }

        public Criteria andCoordryNotBetween(Double value1, Double value2) {
            addCriterion("CoordRY not between", value1, value2, "coordry");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
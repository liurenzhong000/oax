package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovesayMoneyActiveExample {
    /**
     * movesay_money_active
     */
    protected String orderByClause;

    /**
     * movesay_money_active
     */
    protected boolean distinct;

    /**
     * movesay_money_active
     */
    protected List<Criteria> oredCriteria;

    public MovesayMoneyActiveExample() {
        oredCriteria = new ArrayList<Criteria>();
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

    /**
     * movesay_money_active null
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
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

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(Integer value) {
            addCriterion("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(Integer value) {
            addCriterion("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(Integer value) {
            addCriterion("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(Integer value) {
            addCriterion("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(Integer value) {
            addCriterion("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(Integer value) {
            addCriterion("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<Integer> values) {
            addCriterion("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<Integer> values) {
            addCriterion("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(Integer value1, Integer value2) {
            addCriterion("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(Integer value1, Integer value2) {
            addCriterion("date not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andProfitsIsNull() {
            addCriterion("profits is null");
            return (Criteria) this;
        }

        public Criteria andProfitsIsNotNull() {
            addCriterion("profits is not null");
            return (Criteria) this;
        }

        public Criteria andProfitsEqualTo(Integer value) {
            addCriterion("profits =", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsNotEqualTo(Integer value) {
            addCriterion("profits <>", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsGreaterThan(Integer value) {
            addCriterion("profits >", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsGreaterThanOrEqualTo(Integer value) {
            addCriterion("profits >=", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsLessThan(Integer value) {
            addCriterion("profits <", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsLessThanOrEqualTo(Integer value) {
            addCriterion("profits <=", value, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsIn(List<Integer> values) {
            addCriterion("profits in", values, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsNotIn(List<Integer> values) {
            addCriterion("profits not in", values, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsBetween(Integer value1, Integer value2) {
            addCriterion("profits between", value1, value2, "profits");
            return (Criteria) this;
        }

        public Criteria andProfitsNotBetween(Integer value1, Integer value2) {
            addCriterion("profits not between", value1, value2, "profits");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andJoinNumIsNull() {
            addCriterion("join_num is null");
            return (Criteria) this;
        }

        public Criteria andJoinNumIsNotNull() {
            addCriterion("join_num is not null");
            return (Criteria) this;
        }

        public Criteria andJoinNumEqualTo(Integer value) {
            addCriterion("join_num =", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumNotEqualTo(Integer value) {
            addCriterion("join_num <>", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumGreaterThan(Integer value) {
            addCriterion("join_num >", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("join_num >=", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumLessThan(Integer value) {
            addCriterion("join_num <", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumLessThanOrEqualTo(Integer value) {
            addCriterion("join_num <=", value, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumIn(List<Integer> values) {
            addCriterion("join_num in", values, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumNotIn(List<Integer> values) {
            addCriterion("join_num not in", values, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumBetween(Integer value1, Integer value2) {
            addCriterion("join_num between", value1, value2, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinNumNotBetween(Integer value1, Integer value2) {
            addCriterion("join_num not between", value1, value2, "joinNum");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyIsNull() {
            addCriterion("join_money is null");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyIsNotNull() {
            addCriterion("join_money is not null");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyEqualTo(BigDecimal value) {
            addCriterion("join_money =", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyNotEqualTo(BigDecimal value) {
            addCriterion("join_money <>", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyGreaterThan(BigDecimal value) {
            addCriterion("join_money >", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("join_money >=", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyLessThan(BigDecimal value) {
            addCriterion("join_money <", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("join_money <=", value, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyIn(List<BigDecimal> values) {
            addCriterion("join_money in", values, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyNotIn(List<BigDecimal> values) {
            addCriterion("join_money not in", values, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("join_money between", value1, value2, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andJoinMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("join_money not between", value1, value2, "joinMoney");
            return (Criteria) this;
        }

        public Criteria andMarketIdIsNull() {
            addCriterion("market_id is null");
            return (Criteria) this;
        }

        public Criteria andMarketIdIsNotNull() {
            addCriterion("market_id is not null");
            return (Criteria) this;
        }

        public Criteria andMarketIdEqualTo(Integer value) {
            addCriterion("market_id =", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdNotEqualTo(Integer value) {
            addCriterion("market_id <>", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdGreaterThan(Integer value) {
            addCriterion("market_id >", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("market_id >=", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdLessThan(Integer value) {
            addCriterion("market_id <", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdLessThanOrEqualTo(Integer value) {
            addCriterion("market_id <=", value, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdIn(List<Integer> values) {
            addCriterion("market_id in", values, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdNotIn(List<Integer> values) {
            addCriterion("market_id not in", values, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdBetween(Integer value1, Integer value2) {
            addCriterion("market_id between", value1, value2, "marketId");
            return (Criteria) this;
        }

        public Criteria andMarketIdNotBetween(Integer value1, Integer value2) {
            addCriterion("market_id not between", value1, value2, "marketId");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(BigDecimal value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(BigDecimal value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(BigDecimal value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(BigDecimal value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<BigDecimal> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<BigDecimal> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andLimitMaxIsNull() {
            addCriterion("limit_max is null");
            return (Criteria) this;
        }

        public Criteria andLimitMaxIsNotNull() {
            addCriterion("limit_max is not null");
            return (Criteria) this;
        }

        public Criteria andLimitMaxEqualTo(BigDecimal value) {
            addCriterion("limit_max =", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxNotEqualTo(BigDecimal value) {
            addCriterion("limit_max <>", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxGreaterThan(BigDecimal value) {
            addCriterion("limit_max >", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_max >=", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxLessThan(BigDecimal value) {
            addCriterion("limit_max <", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxLessThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_max <=", value, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxIn(List<BigDecimal> values) {
            addCriterion("limit_max in", values, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxNotIn(List<BigDecimal> values) {
            addCriterion("limit_max not in", values, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_max between", value1, value2, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMaxNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_max not between", value1, value2, "limitMax");
            return (Criteria) this;
        }

        public Criteria andLimitMinIsNull() {
            addCriterion("limit_min is null");
            return (Criteria) this;
        }

        public Criteria andLimitMinIsNotNull() {
            addCriterion("limit_min is not null");
            return (Criteria) this;
        }

        public Criteria andLimitMinEqualTo(BigDecimal value) {
            addCriterion("limit_min =", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinNotEqualTo(BigDecimal value) {
            addCriterion("limit_min <>", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinGreaterThan(BigDecimal value) {
            addCriterion("limit_min >", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_min >=", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinLessThan(BigDecimal value) {
            addCriterion("limit_min <", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinLessThanOrEqualTo(BigDecimal value) {
            addCriterion("limit_min <=", value, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinIn(List<BigDecimal> values) {
            addCriterion("limit_min in", values, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinNotIn(List<BigDecimal> values) {
            addCriterion("limit_min not in", values, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_min between", value1, value2, "limitMin");
            return (Criteria) this;
        }

        public Criteria andLimitMinNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("limit_min not between", value1, value2, "limitMin");
            return (Criteria) this;
        }

        public Criteria andRecommendIsNull() {
            addCriterion("recommend is null");
            return (Criteria) this;
        }

        public Criteria andRecommendIsNotNull() {
            addCriterion("recommend is not null");
            return (Criteria) this;
        }

        public Criteria andRecommendEqualTo(Byte value) {
            addCriterion("recommend =", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendNotEqualTo(Byte value) {
            addCriterion("recommend <>", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendGreaterThan(Byte value) {
            addCriterion("recommend >", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendGreaterThanOrEqualTo(Byte value) {
            addCriterion("recommend >=", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendLessThan(Byte value) {
            addCriterion("recommend <", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendLessThanOrEqualTo(Byte value) {
            addCriterion("recommend <=", value, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendIn(List<Byte> values) {
            addCriterion("recommend in", values, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendNotIn(List<Byte> values) {
            addCriterion("recommend not in", values, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendBetween(Byte value1, Byte value2) {
            addCriterion("recommend between", value1, value2, "recommend");
            return (Criteria) this;
        }

        public Criteria andRecommendNotBetween(Byte value1, Byte value2) {
            addCriterion("recommend not between", value1, value2, "recommend");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenIsNull() {
            addCriterion("display_open is null");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenIsNotNull() {
            addCriterion("display_open is not null");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenEqualTo(Byte value) {
            addCriterion("display_open =", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenNotEqualTo(Byte value) {
            addCriterion("display_open <>", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenGreaterThan(Byte value) {
            addCriterion("display_open >", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenGreaterThanOrEqualTo(Byte value) {
            addCriterion("display_open >=", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenLessThan(Byte value) {
            addCriterion("display_open <", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenLessThanOrEqualTo(Byte value) {
            addCriterion("display_open <=", value, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenIn(List<Byte> values) {
            addCriterion("display_open in", values, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenNotIn(List<Byte> values) {
            addCriterion("display_open not in", values, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenBetween(Byte value1, Byte value2) {
            addCriterion("display_open between", value1, value2, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andDisplayOpenNotBetween(Byte value1, Byte value2) {
            addCriterion("display_open not between", value1, value2, "displayOpen");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeIsNull() {
            addCriterion("interstart_time is null");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeIsNotNull() {
            addCriterion("interstart_time is not null");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeEqualTo(Date value) {
            addCriterion("interstart_time =", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeNotEqualTo(Date value) {
            addCriterion("interstart_time <>", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeGreaterThan(Date value) {
            addCriterion("interstart_time >", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("interstart_time >=", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeLessThan(Date value) {
            addCriterion("interstart_time <", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeLessThanOrEqualTo(Date value) {
            addCriterion("interstart_time <=", value, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeIn(List<Date> values) {
            addCriterion("interstart_time in", values, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeNotIn(List<Date> values) {
            addCriterion("interstart_time not in", values, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeBetween(Date value1, Date value2) {
            addCriterion("interstart_time between", value1, value2, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterstartTimeNotBetween(Date value1, Date value2) {
            addCriterion("interstart_time not between", value1, value2, "interstartTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeIsNull() {
            addCriterion("interend_time is null");
            return (Criteria) this;
        }

        public Criteria andInterendTimeIsNotNull() {
            addCriterion("interend_time is not null");
            return (Criteria) this;
        }

        public Criteria andInterendTimeEqualTo(Date value) {
            addCriterion("interend_time =", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeNotEqualTo(Date value) {
            addCriterion("interend_time <>", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeGreaterThan(Date value) {
            addCriterion("interend_time >", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("interend_time >=", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeLessThan(Date value) {
            addCriterion("interend_time <", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeLessThanOrEqualTo(Date value) {
            addCriterion("interend_time <=", value, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeIn(List<Date> values) {
            addCriterion("interend_time in", values, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeNotIn(List<Date> values) {
            addCriterion("interend_time not in", values, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeBetween(Date value1, Date value2) {
            addCriterion("interend_time between", value1, value2, "interendTime");
            return (Criteria) this;
        }

        public Criteria andInterendTimeNotBetween(Date value1, Date value2) {
            addCriterion("interend_time not between", value1, value2, "interendTime");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Integer value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Integer value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Integer value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Integer value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Integer value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Integer> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Integer> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Integer value1, Integer value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyIsNull() {
            addCriterion("deposit_money is null");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyIsNotNull() {
            addCriterion("deposit_money is not null");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyEqualTo(BigDecimal value) {
            addCriterion("deposit_money =", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyNotEqualTo(BigDecimal value) {
            addCriterion("deposit_money <>", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyGreaterThan(BigDecimal value) {
            addCriterion("deposit_money >", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("deposit_money >=", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyLessThan(BigDecimal value) {
            addCriterion("deposit_money <", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("deposit_money <=", value, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyIn(List<BigDecimal> values) {
            addCriterion("deposit_money in", values, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyNotIn(List<BigDecimal> values) {
            addCriterion("deposit_money not in", values, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("deposit_money between", value1, value2, "depositMoney");
            return (Criteria) this;
        }

        public Criteria andDepositMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("deposit_money not between", value1, value2, "depositMoney");
            return (Criteria) this;
        }
    }

    /**
     * movesay_money_active
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * movesay_money_active null
     */
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
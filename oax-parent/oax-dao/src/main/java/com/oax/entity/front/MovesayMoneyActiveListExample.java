package com.oax.entity.front;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovesayMoneyActiveListExample {
    /**
     * movesay_money_active_list
     */
    protected String orderByClause;

    /**
     * movesay_money_active_list
     */
    protected boolean distinct;

    /**
     * movesay_money_active_list
     */
    protected List<Criteria> oredCriteria;

    public MovesayMoneyActiveListExample() {
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
     * movesay_money_active_list null
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andActiveIdIsNull() {
            addCriterion("active_id is null");
            return (Criteria) this;
        }

        public Criteria andActiveIdIsNotNull() {
            addCriterion("active_id is not null");
            return (Criteria) this;
        }

        public Criteria andActiveIdEqualTo(Integer value) {
            addCriterion("active_id =", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdNotEqualTo(Integer value) {
            addCriterion("active_id <>", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdGreaterThan(Integer value) {
            addCriterion("active_id >", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("active_id >=", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdLessThan(Integer value) {
            addCriterion("active_id <", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdLessThanOrEqualTo(Integer value) {
            addCriterion("active_id <=", value, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdIn(List<Integer> values) {
            addCriterion("active_id in", values, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdNotIn(List<Integer> values) {
            addCriterion("active_id not in", values, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdBetween(Integer value1, Integer value2) {
            addCriterion("active_id between", value1, value2, "activeId");
            return (Criteria) this;
        }

        public Criteria andActiveIdNotBetween(Integer value1, Integer value2) {
            addCriterion("active_id not between", value1, value2, "activeId");
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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

        public Criteria andProfitsMoneyIsNull() {
            addCriterion("profits_money is null");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyIsNotNull() {
            addCriterion("profits_money is not null");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyEqualTo(BigDecimal value) {
            addCriterion("profits_money =", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyNotEqualTo(BigDecimal value) {
            addCriterion("profits_money <>", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyGreaterThan(BigDecimal value) {
            addCriterion("profits_money >", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("profits_money >=", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyLessThan(BigDecimal value) {
            addCriterion("profits_money <", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyLessThanOrEqualTo(BigDecimal value) {
            addCriterion("profits_money <=", value, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyIn(List<BigDecimal> values) {
            addCriterion("profits_money in", values, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyNotIn(List<BigDecimal> values) {
            addCriterion("profits_money not in", values, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("profits_money between", value1, value2, "profitsMoney");
            return (Criteria) this;
        }

        public Criteria andProfitsMoneyNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("profits_money not between", value1, value2, "profitsMoney");
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

        public Criteria andCumulativeJoinIsNull() {
            addCriterion("cumulative_join is null");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinIsNotNull() {
            addCriterion("cumulative_join is not null");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinEqualTo(BigDecimal value) {
            addCriterion("cumulative_join =", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinNotEqualTo(BigDecimal value) {
            addCriterion("cumulative_join <>", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinGreaterThan(BigDecimal value) {
            addCriterion("cumulative_join >", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_join >=", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinLessThan(BigDecimal value) {
            addCriterion("cumulative_join <", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinLessThanOrEqualTo(BigDecimal value) {
            addCriterion("cumulative_join <=", value, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinIn(List<BigDecimal> values) {
            addCriterion("cumulative_join in", values, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinNotIn(List<BigDecimal> values) {
            addCriterion("cumulative_join not in", values, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_join between", value1, value2, "cumulativeJoin");
            return (Criteria) this;
        }

        public Criteria andCumulativeJoinNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("cumulative_join not between", value1, value2, "cumulativeJoin");
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
    }

    /**
     * movesay_money_active_list
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * movesay_money_active_list null
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
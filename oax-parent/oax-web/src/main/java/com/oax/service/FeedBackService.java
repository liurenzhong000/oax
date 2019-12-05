package com.oax.service;

import java.util.List;
import java.util.Map;

public interface FeedBackService {

	Map<String, Object> queryOverview();

	Map<String, Object> queryFeedBackSum();

	Map<String, Object> queryMyIncome(Integer userId);

	List<Map<String, Object>> queryTodayFeedBackdetail();
}

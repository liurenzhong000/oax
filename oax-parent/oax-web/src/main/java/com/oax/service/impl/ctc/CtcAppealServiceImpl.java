package com.oax.service.impl.ctc;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.ctc.CtcAppeal;
import com.oax.mapper.ctc.CtcAppealMapper;
import com.oax.service.ctc.CtcAppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CTC 订单申诉记录
 */
@Service
@Transactional
public class CtcAppealServiceImpl implements CtcAppealService {

    @Autowired
    private CtcAppealMapper ctcAppealMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public void saveOne(Integer userId, Long ctcOrderId, String appealDesc) {
        CtcAppeal ctcAppeal = CtcAppeal.newInstance(userId, ctcOrderId, appealDesc);
        ctcAppealMapper.insert(ctcAppeal);
    }
}

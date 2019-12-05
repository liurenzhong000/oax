package com.oax.service.impl.ctc;

import com.oax.druid.DataSource;
import com.oax.druid.DataSourceType;
import com.oax.entity.ctc.CtcOrderLog;
import com.oax.entity.enums.CtcOrderLogType;
import com.oax.entity.enums.CtcOrderStatus;
import com.oax.mapper.ctc.CtcOrderLogMapper;
import com.oax.service.ctc.CtcOrderLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CtcOrderLogServiceImpl implements CtcOrderLogService {

    @Autowired
    private CtcOrderLogMapper ctcOrderLogMapper;

    @Override
    @DataSource(DataSourceType.MASTER)
    public void saveOne(Long ctcOrderId, CtcOrderStatus beforeStatus, CtcOrderStatus afterStatus, CtcOrderLogType type, Integer userId) {
        CtcOrderLog ctcOrderLog = CtcOrderLog.newInstance(ctcOrderId, beforeStatus, afterStatus, type, userId);
        ctcOrderLogMapper.insert(ctcOrderLog);
    }
}

package com.oax.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.CoinLockConfigService;
import com.oax.common.RedisUtil;
import com.oax.common.enums.RedisKeyEnum;
import com.oax.entity.admin.vo.CoinLockConfigVo;
import com.oax.entity.front.CoinLockConfig;
import com.oax.mapper.front.CoinLockConfigMapper;

@Service
public class CoinLockConfigServiceImpl implements CoinLockConfigService {

    @Autowired
    private CoinLockConfigMapper mapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean saveOrUpdate(CoinLockConfig coinLockConfig) {
        coinLockConfig.setUpdateTime(new Date());
        if(coinLockConfig.getId()!=null){
            //update 修改
            Integer update = mapper.update(coinLockConfig);
            if (update!=null&&update>0){
                //删除缓存
                redisUtil.delete(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey());
                return true;
            }
        }else{
            //save 增加
/*            List<InrestRatio> list = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                InrestRatio inrestRatio = new InrestRatio();
                inrestRatio.setDay(i+1);
                inrestRatio.setRatio(BigDecimal.ONE.add(new BigDecimal(i)));
                list.add(inrestRatio);
            }
            coinLockConfig.setRatio(JSON.toJSONString(list));*/
            coinLockConfig.setIsShow(0);
            coinLockConfig.setCreateTime(new Date());
            Integer save = mapper.save(coinLockConfig);
            if (save!=null&&save>0){
                //删除缓存
                redisUtil.delete(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey());
                return true;
            }
        }
        return false;
    }

    @Override
    public CoinLockConfig selectById(Integer id) {
        return mapper.selectById(id);
    }

    @Override
    public PageInfo<CoinLockConfig> getPage(CoinLockConfigVo vo) {
        PageHelper.startPage(vo.getPageNo(), vo.getPageSize());
        List<CoinLockConfig> list = mapper.getList();
        return new PageInfo<>(list);
    }

    @Override
    public boolean isShow(Integer id, Integer isShow) {
        isShow = isShow==0?1:0;
        Integer count = mapper.isShow(id,isShow);
        if (count!=null&&count>0){
            redisUtil.delete(RedisKeyEnum.COIN_LOCK_CONFIG_LIST.getKey());
            return true;
        }
        return false;
    }
}

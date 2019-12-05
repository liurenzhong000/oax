package com.oax.admin.service.impl.bonus;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oax.admin.form.UserCoinSnapshootLightForm;
import com.oax.admin.service.activity.UserCoinSnapshootLightService;
import com.oax.entity.front.UserCoinSnapshootLight;
import com.oax.mapper.front.UserCoinSnapshootLightMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: hyp
 * @Date: 2019/1/14 18:19
 * @Description:
 */
@Service
public class UserCoinSnapshootLightServiceImpl implements UserCoinSnapshootLightService {

    @Autowired
    private UserCoinSnapshootLightMapper userCoinSnapshootLightMapper;

    @Override
    public PageInfo<UserCoinSnapshootLight> pageForAdmin(UserCoinSnapshootLightForm form) {
        PageHelper.startPage(form.getPageNum(), form.getPageSize());
        List<UserCoinSnapshootLight> userCoinSnapshootLights = userCoinSnapshootLightMapper.pageForAdmin(form.getUserId(), form.getStartTime(), form.getEndTime());
        return new PageInfo<>(userCoinSnapshootLights);
    }
}


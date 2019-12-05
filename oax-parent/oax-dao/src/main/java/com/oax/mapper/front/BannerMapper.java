package com.oax.mapper.front;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.oax.entity.admin.param.BannerParam;
import com.oax.entity.admin.param.BannerUpdateParam;
import com.oax.entity.admin.vo.BannerVo;
import com.oax.entity.front.Banner;
import com.oax.entity.front.BannerInfo;

@Mapper
public interface BannerMapper {

    int insert(Banner record);

    Integer update(BannerUpdateParam record);

    List<BannerVo> bannerManageList(BannerParam banner);

    /**
     * @param ：@param  status
     * @param ：@return
     * @return ：List<Banner>
     * @throws
     * @Title：findList
     * @Description：查询已发布的所有的banner信息
     */
    List<BannerInfo> findList(Map<String, Object> map);


    /*
     * banner删除
     */
    Integer deleteBanner(Integer id);

    Banner selectById(Integer id);
}
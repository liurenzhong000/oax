package com.oax.mapper.front;

import java.util.Date;
import java.util.List;

import com.oax.entity.front.vo.PlatformTransferSumVo;
import com.oax.entity.front.vo.RechargeSumVo;
import org.apache.ibatis.annotations.Param;

import com.oax.entity.front.PlatformTransfer;

public interface PlatformTransferMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformTransfer record);

    int insertSelective(PlatformTransfer record);

    PlatformTransfer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformTransfer record);

    int updateByPrimaryKey(PlatformTransfer record);

    List<PlatformTransfer> selectNotVerifyByAddressAndType(String address);

    List<PlatformTransfer> selectByType(int type);

    List<PlatformTransfer> selectByTypeAndStatus(@Param(value = "type") Integer type, @Param(value = "status") Integer status);

    PlatformTransfer selectByHash(String hash);

    List<PlatformTransfer> selectByCoinIdAndTime(@Param(value = "coinId") Integer coinId, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);

    List<PlatformTransfer> selectByCoinIdAndStatus(@Param(value = "coinId")Integer coinId, @Param(value = "type")Integer type);

    List<PlatformTransferSumVo> selectSumVoByCoinIdAndTime(@Param(value = "coinId") Integer coinId, @Param(value = "startTime") Date startTime, @Param(value = "endTime") Date endTime);
}
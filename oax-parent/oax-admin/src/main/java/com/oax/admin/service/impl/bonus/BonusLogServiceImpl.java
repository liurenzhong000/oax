package com.oax.admin.service.impl.bonus;

import com.github.pagehelper.PageInfo;
import com.oax.admin.service.bonus.BonusLogService;
import com.oax.admin.vo.BonusDataVo;
import com.oax.common.DateHelper;
import com.oax.common.RedisUtil;
import com.oax.common.constant.RedisKeyConstant;
import com.oax.common.json.JsonHelper;
import com.oax.common.util.excel.ExcelUtils;
import com.oax.entity.admin.param.PageParam;
import com.oax.entity.admin.vo.BonusLogVo;
import com.oax.mapper.front.BonusLogMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: hyp
 * @Date: 2019/1/11 18:11
 * @Description:
 */
@Service
public class BonusLogServiceImpl implements BonusLogService {

    @Autowired
    private BonusLogMapper bonusLogMapper;

    @Autowired
    private RedisUtil redisUtil;

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public String getReferer(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Referer");
    }

    //https://github.com/andyczy/czy-nexus-commons-utils/blob/master/README-3.2.md
    @Override
    public void exportExcel(BigDecimal bhbUsdtRatio, BigDecimal allBonus, Integer reachCount, Date startTime, Date endTime){
        startTime = DateUtils.addSeconds(startTime, -10);
        endTime = DateUtils.addSeconds(endTime, 10);
        List<BonusLogVo> bonusLogVoList = bonusLogMapper.selectVoForExcel(startTime, endTime);
        List<BonusLogVo> employeeBonusLogVoList = bonusLogMapper.selectEmployeeForExcel(startTime, endTime);
        BigDecimal employeeBonus = bonusLogMapper.getBonusLogMapper(startTime, endTime);
        //导出excel
        ExcelUtils excelUtils = ExcelUtils.setExcelUtils();
        excelUtils.setDataLists(exportBonus(bonusLogVoList, employeeBonusLogVoList));
        excelUtils.setSheetName(getSheetNames(endTime));
        excelUtils.setResponse(getResponse());
        excelUtils.setLabelName(getLabelNames(bhbUsdtRatio, allBonus, reachCount, employeeBonus));
        excelUtils.setRegionMap(getRegionMap());
        // 执行导出
        excelUtils.exportForExcelsOptimize();
    }

    @Override
    public PageInfo pageByRedis(PageParam pageParam) {
        int start = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        int end = start + pageParam.getPageSize() - 1;
        int size = redisUtil.zsize(RedisKeyConstant.BONUS_RECORD_ZSET_KEY).intValue();
        Set<String> bonusDataVoStrList = redisUtil.reverseRange(RedisKeyConstant.BONUS_RECORD_ZSET_KEY, start, end);
        List<BonusDataVo> bonusDataVos = bonusDataVoStrList.stream().map(bonusDataVoStr -> JsonHelper.readValue(bonusDataVoStr, BonusDataVo.class)).collect(Collectors.toList());
        bonusDataVos.forEach(item -> item.setExcelUrl(getExcelUrl(item)));
        PageInfo pageInfo = new PageInfo(bonusDataVos, pageParam.getPageNum());
        pageInfo.setTotal(size);
        return pageInfo;
    }

    private String getExcelUrl(BonusDataVo dataVo){
        return getReferer() + "BHBBonus/excel?reachCount="+dataVo.getReachCount()+"&bhbUsdtRatio="+dataVo.getBhbUsdtRatio()+"&allBonus="+dataVo.getAllBonus()+"&startTime="+DateHelper.format(dataVo.getStartTime())+"&endTime="+DateHelper.format(dataVo.getEndTime());
    }

    //获取sheet名称
    private String[] getSheetNames(Date endTime){
        Date bonusDate = DateUtils.addDays(endTime, 1);
        String bonusDateStr = DateHelper.format(bonusDate, DateHelper.DATE);
        return new String[]{ bonusDateStr + "分红全部数据", bonusDateStr + "员工分红数据"};
    }

    //合并单元格
    private HashMap getRegionMap(){
        List<List<Integer[]>> setMergedRegion = new ArrayList<>();
        List<Integer[]> sheet1 = new ArrayList<>();                  //第一个表格设置。
        Integer[] sheetColumn1 = new Integer[]{0, 8, 0, 30};       //代表起始行号，终止行号， 起始列号，终止列号进行合并。（excel从零行开始数）
        sheet1.add(sheetColumn1);

        List<Integer[]> sheet2 = new ArrayList<>();                  //第一个表格设置。
        Integer[] sheetColumn2 = new Integer[]{0, 5, 0, 30};       //代表起始行号，终止行号， 起始列号，终止列号进行合并。（excel从零行开始数）
        sheet2.add(sheetColumn2);

        setMergedRegion.add(sheet1);
        setMergedRegion.add(sheet2);
        HashMap map = new HashMap();
        map.put("0", setMergedRegion);
        return map;
    }

    //每个sheet的大标题
    private String[] getLabelNames(BigDecimal bhbUsdtRatio, BigDecimal allBonus, Integer reachCount, BigDecimal employeeBonus){
        return new String[]{"分红总量:"+allBonus.toPlainString()+"个usdt\n" +
                "BHB 转换 USDT的比例："+bhbUsdtRatio.toPlainString()+"\n" +
                "BHB分红：达标用户个数"+ reachCount

                ,"员工分红总量:"+employeeBonus.toPlainString()+"个usdt"};
    }

    public List<List<String[]>> exportBonus(List<BonusLogVo> bonusLogVoList, List<BonusLogVo> employeeBonusLogVoList){
        List<List<String[]>> dataLists = new ArrayList<>();
        List<String[]> allBonusList = new ArrayList<>();//全部用户
        List<String[]> employeeBonusList = new ArrayList<>();//员工
        String[] valueString = null;
        String[] headers = {"序号", "用户id","用户手机号","用户邮箱","用户真实姓名","用户当前持币数","总收益","用户自身收益","一级用户数",
                "一级用户收益","二级用户数","二级用户收益","三级用户数","三级用户收益","一级用户id","二级用户id","三级用户id","时间"};
        allBonusList.add(headers);
        employeeBonusList.add(headers);

        for (int i = 0; i < bonusLogVoList.size(); i++) {
            BonusLogVo bonusLogVo = bonusLogVoList.get(i);
            valueString = new String[]{
                    (i + 1) + "",
                    bonusLogVo.getUserId().toString(),
                    bonusLogVo.getPhone(),
                    bonusLogVo.getEmail(),
                    bonusLogVo.getIdName(),
                    bonusLogVo.getCurrQty().toPlainString(),
                    bonusLogVo.getBonus().toPlainString(),
                    bonusLogVo.getMyBonus().toPlainString(),
                    bonusLogVo.getOneCount().toString(),
                    bonusLogVo.getOneBonus().toPlainString(),
                    bonusLogVo.getTwoCount().toString(),
                    bonusLogVo.getTwoBonus().toPlainString(),
                    bonusLogVo.getThreeCount().toString(),
                    bonusLogVo.getThreeBonus().toPlainString(),
                    bonusLogVo.getOneUserIds(),
                    bonusLogVo.getTwoUserIds(),
                    bonusLogVo.getThreeUserIds(),
                    DateHelper.format(bonusLogVo.getCreateTime())};
            allBonusList.add(valueString);
        }

        for (int i = 0; i < employeeBonusLogVoList.size(); i++) {
            BonusLogVo bonusLogVo = employeeBonusLogVoList.get(i);
            valueString = new String[]{
                    (i + 1) + "",
                    bonusLogVo.getUserId().toString(),
                    bonusLogVo.getPhone(),
                    bonusLogVo.getEmail(),
                    bonusLogVo.getIdName(),
                    bonusLogVo.getCurrQty().toPlainString(),
                    bonusLogVo.getBonus().toPlainString(),
                    bonusLogVo.getMyBonus().toPlainString(),
                    bonusLogVo.getOneCount().toString(),
                    bonusLogVo.getOneBonus().toPlainString(),
                    bonusLogVo.getTwoCount().toString(),
                    bonusLogVo.getTwoBonus().toPlainString(),
                    bonusLogVo.getThreeCount().toString(),
                    bonusLogVo.getThreeBonus().toPlainString(),
                    bonusLogVo.getOneUserIds(),
                    bonusLogVo.getTwoUserIds(),
                    bonusLogVo.getThreeUserIds(),
                    DateHelper.format(bonusLogVo.getCreateTime())};
            employeeBonusList.add(valueString);
        }
        dataLists.add(allBonusList);
        dataLists.add(employeeBonusList);
        return dataLists;
    }
}

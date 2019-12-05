package com.oax.admin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.oax.admin.service.CoinService;
import com.oax.admin.service.EmployeeImportLogService;
import com.oax.admin.service.IRechargeService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.EmployeeImportLog;
import com.oax.entity.front.Coin;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/7/19
 * Time: 10:09
 * 福利 控制层
 */
@RestController
@RequestMapping("/welfares")
public class WelfareController {


    @Autowired
    private IRechargeService rechargeService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private EmployeeImportLogService employeeImportLogService;


    @Value("${local.coin.shortname}")
    private String coinShortName;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 员工福利
     *
     * @return
     */
    @PostMapping("/employee")
    public ResultResponse employeeWelfare(@RequestParam("remark") String remark, HttpServletRequest request) throws Exception {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if (multipartResolver.isMultipart(request)) {

            Coin coin = coinService.selectByShortName(coinShortName);
            if (coin == null) {
                return new ResultResponse(false, "不存在平台币,请设置application-***.properties中local.coin.shortname属性");
            }

            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();

            List<HashMap<String,Object>> jsonObjectList = new ArrayList<>();
            while (iter.hasNext()) {
                // 取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    InputStream inputStream = null;
                    try {
                        inputStream = file.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jsonObjectList.addAll(ExcelImportUtil.importExcel(inputStream, Map.class, new ImportParams()));
                }
            }


            Runnable runnable = () -> {
                try {
                    forkjoinImport(remark, coin, jsonObjectList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            threadPoolTaskExecutor.execute(runnable);
        }
        return new ResultResponse(true, "上传成功,稍后查看");
    }


    private void forkjoinImport(String remark, Coin coin, List<HashMap<String,Object>> jsonObjectList) throws Exception {
        ArrayList<Integer> failList = new ArrayList<>();

        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
        forkJoinPool.submit(() -> {
            jsonObjectList.parallelStream()
                    .forEach(o -> {
                        o.put("coinId", coin.getId());
                        o.put("remark", remark);
                        try {
                            rechargeService.employeeWelfare(o);
                        } catch (Exception e) {
                            JSONObject jsonObject = new JSONObject(o);
                            failList.add(jsonObject.getInteger("用户id"));
                        }
                    });
        }).get();
        EmployeeImportLog employeeImportLog = new EmployeeImportLog();
        if (CollectionUtils.isNotEmpty(failList)) {
            employeeImportLog.setImportLog("不存在用户id:" + failList.toString());
        } else {
            employeeImportLog.setImportLog("上传成功,无错误");
        }
        employeeImportLogService.insert(employeeImportLog);
    }

}
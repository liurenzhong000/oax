/**
 *
 */
package com.oax.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;

/**
 * @author ：xiangwh
 * @ClassName:：BaseController
 * @Description： 所有Controller的父类
 * @date ：2018年6月1日 下午2:27:20
 */
public class BaseController {
    private static Params param = new Params();

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public HttpSession getSession() {
        return this.getRequest().getSession();
    }

    /**
     * @param ：@param sheetName
     * @param ：@param clazz
     * @param ：@param list
     * @return ：void
     * @throws
     * @Title：setHeadDownloadExcel
     * @Description：导出数据到excel表
     */
    public void setHeadDownloadExcel(String sheetName, Class<?> clazz, List<?> list) {
        // 告诉浏览器用什么软件可以打开此文件
        getResponse().setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        getResponse().setHeader("Content-Disposition", "attachment;filename=" + sheetName + ".xls");
        //编码
        getResponse().setCharacterEncoding("UTF-8");
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), clazz, list);
        try {
            workbook.write(getResponse().getOutputStream());
        } catch (Exception e) {
            //相应json
            getResponse().setHeader("content-Type", "application/json; charset=utf-8");
            ResultResponse result = new ResultResponse(false, "导出数据异常");
            try {
                getResponse().getWriter().write(JSON.toJSONString(result));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public Params getParams() {
        HttpServletRequest request = this.getRequest();
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            param.put(entry.getKey(), arrToString(entry.getValue()));
        }
        return param;
    }

    public String arrToString(String[] arr) {
        StringBuffer sb = new StringBuffer();
        if (arr.length != 0) {
            for (String str : arr) {
                sb.append(str).append(",");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);

    }


}

package com.oax.controller;

import com.oax.common.ResultResponse;
import com.oax.entity.front.Robot;
import com.oax.mapper.front.RobotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/robot")
public class RobotController {

    @Autowired
    private RobotMapper robotMapper;

    /**
     * 更新机器人参数
     *
     * @return ResultResponse
     */
    @RequestMapping("/updateRobot")
    public ResultResponse updateRobot( HttpServletResponse response,Robot robot) {
        // 指定允许其他域名访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        boolean success = true;
        if(robot.getFixSleep()==null){
            robot.setFixSleep(6);
        }
        if(robot.getDynamicSleep()==null){
            robot.setDynamicSleep(4);
        }
        int count = robotMapper.updateById(robot);
        if (count > 0) {
        } else {
            success = false;
        }
        return new ResultResponse(success, "");
    }

    /**
     * 获取机器人参数
     *
     * @return ResultResponse
     */
    @RequestMapping("/getRobot")
    public ResultResponse getRobot(HttpServletResponse response ,Integer id) {
        // 指定允许其他域名访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        boolean success = true;
        Robot robot = new Robot();
        robot.setId(id);
        robot = robotMapper.selectById(robot);
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(success);
        resultResponse.setData(robot);
        return resultResponse;
    }
}

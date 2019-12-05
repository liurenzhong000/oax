package com.oax.admin.controller;

import com.oax.common.GoogleAuthenticator;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oax.admin.service.HomeService;
import com.oax.admin.util.UserUtils;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.User;
import com.oax.entity.admin.vo.HomeStatisticsVo;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/30
 * Time: 11:55
 * 后台 登录入口
 */
@RestController
public class HomeController {

    @PostMapping(value = "/login")
    public ResultResponse login(@RequestBody User user) {

        //参数校验
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return new ResultResponse(false, "帐户名或密码不能未空");
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            //成功
            subject.login(token);
            User loginUser = (User) subject.getPrincipal();

            //谷歌验证
            long time = System.currentTimeMillis();
            GoogleAuthenticator ga = new GoogleAuthenticator();
            ga.setWindowSize(5);
            boolean b=ga.checkCode(loginUser.getGoogleCode(),Long.parseLong(user.getGoogleCode()),time);
            if(!b)
            {
                return new ResultResponse(false,"谷歌验证码错误");
            }

            loginUser.setPassword(null);
            loginUser.setGoogleCode(null);
            return new ResultResponse(true, loginUser);

        } catch (LockedAccountException lae) {
            //用户已经被锁定不能登录，请与管理员联系！
            token.clear();
            return new ResultResponse(false, "用户已经被锁定不能登录，请与管理员联系！");
        } catch (AuthenticationException e) {
            token.clear();
            //用户或密码不正确！
            return new ResultResponse(false, "用户或密码不正确！");
        }
    }

    @PostMapping("/logout")
    public ResultResponse logout() {

        try {
            UserUtils.logout();
            return new ResultResponse(true, "退出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultResponse(false, "退出失败");
        }
    }

    @GetMapping("/403")
    public ResultResponse unauthorized() {

        return new ResultResponse(false, "权限不足");
    }



    @Autowired
    private HomeService homeService;

    /**
     * 首页统计
     * @return
     */
    @GetMapping("/home")
    public ResultResponse homeStatistics(){
        HomeStatisticsVo homeStatisticsVo = homeService.userandOrderStatistics();
        return new ResultResponse(true,homeStatisticsVo);
    }

}

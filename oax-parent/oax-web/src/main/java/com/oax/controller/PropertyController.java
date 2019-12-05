package com.oax.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.oax.Constant;
import com.oax.common.ResultResponse;
import com.oax.entity.front.RechargeResult;
import com.oax.entity.front.WithdrawResult;
import com.oax.exception.VoException;
import com.oax.exception.VoVailder;
import com.oax.service.IRechargeService;
import com.oax.service.IWithdrawService;
import com.oax.vo.RechargeListVO;
import com.oax.vo.WithdrawListVO;

@RestController
@RequestMapping("/property")
public class PropertyController {

    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IRechargeService rechargeService;

    /**
     * 根据用户id查出该用户资产下的用户提现记录
     * @param
     * @throws VoException
     */
    @PostMapping("/withdraw")
    public ResultResponse withdraw(@RequestBody @Valid WithdrawListVO vo, BindingResult result,HttpServletRequest request) throws VoException {
		String userId = request.getHeader(Constant.api_header_userId);
		vo.setUserId(Integer.parseInt(userId));
		VoVailder.valid(result);
  
		PageInfo<WithdrawResult> data = withdrawService.getByUserIdWithdraw(vo);
        return new ResultResponse(true, data);
    }


    /**
     * 充值记录: 根据用户id查询该用户资产下的充值所有记录
     * @param
     * @throws VoException
     */
    @PostMapping("/recharge")
    public ResultResponse recharge(@RequestBody @Valid RechargeListVO vo, BindingResult result,HttpServletRequest request) throws VoException{
		String userId = request.getHeader(Constant.api_header_userId);
		vo.setUserId(Integer.parseInt(userId));
		VoVailder.valid(result);
		
		PageInfo<RechargeResult>  data = rechargeService.getByUserIdRecharge(vo);
        return new ResultResponse(true, data);
    }
}

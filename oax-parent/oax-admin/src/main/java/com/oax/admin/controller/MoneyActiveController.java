package com.oax.admin.controller;
import com.github.pagehelper.PageInfo;
import com.oax.admin.service.MoneyActiveUserListService;
import com.oax.admin.service.PromoteFundService;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.param.OperationParam;
import com.oax.entity.admin.param.profitParam;
import com.oax.entity.admin.param.rushPageParam;
import com.oax.entity.admin.vo.MovesayMoneyActiveListVO;
import com.oax.entity.front.Active;
import com.oax.entity.front.MovesayMoneyActive;
import com.oax.entity.front.PromoteProfit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.oax.admin.service.MoneyActiveService;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("active")
@RestController
public class MoneyActiveController {

    @Autowired
    private MoneyActiveService moneyActiveService;


    @Autowired
    private MoneyActiveUserListService moneyActiveUserListService;


    @Autowired
    private PromoteFundService promoteFundService;


    //余利宝项目配置列表--获取余利宝项目
    @PostMapping("page")
    public ResultResponse getPage() {
        List<MovesayMoneyActive> movesayMoneyActive = new ArrayList<MovesayMoneyActive>();
        movesayMoneyActive = moneyActiveService.selectAllRecord();
        return new ResultResponse(true, movesayMoneyActive);

    }

    //余利宝项目配置列表--操作状态
    @PostMapping("modioperation")
    public ResultResponse modioperation(@RequestBody @Valid OperationParam operationParam,BindingResult results) {
        if (results.hasErrors())
            return new ResultResponse(false,results.getFieldError().getDefaultMessage());
        MovesayMoneyActive movesayMoneyActive = moneyActiveService.selectMoneyActiveById(operationParam.getId());
        movesayMoneyActive.setDisplayOpen(operationParam.getDisplay_open());
        int ret=moneyActiveService.updateMoneyActive(movesayMoneyActive);
        if (ret > 0) {
            return new ResultResponse(true, "更新余利宝活动状态成功");
        } else {
            return new ResultResponse(false, "更新余利宝活动状态失败");
        }
    }

    //余利宝项目配置列表--添加活动
    @PostMapping("save")
    public ResultResponse activeList(@RequestBody @Valid MovesayMoneyActive moneyActiveSysParam,BindingResult results){
        if (results.hasErrors())
            return new ResultResponse(false,results.getFieldError().getDefaultMessage());
        Integer ret =moneyActiveService.insertMoneyActiveSys(moneyActiveSysParam);
        if (ret > 0) {
            return new ResultResponse(true, "添加活动成功");
        } else {
            return new ResultResponse(false, "添加活动失败");
        }
    }

    //余利宝项目配置列表--编辑活动
    @PostMapping("editList")
    public ResultResponse editList(@RequestBody @Valid MovesayMoneyActive moneyActiveSysParam,BindingResult results){
        if (results.hasErrors())
            return new ResultResponse(false,results.getFieldError().getDefaultMessage());
        Integer ret =moneyActiveService.updateMoneyActive(moneyActiveSysParam);
        if (ret > 0) {
            return new ResultResponse(true, "更新活动成功");
        } else {
            return new ResultResponse(false, "更新活动失败");
        }
    }

    //查询用户抢购记录
    @PostMapping("rushPage")
    public ResultResponse rushPage(@RequestBody @Valid rushPageParam rushPageParam,BindingResult results) {
        if (results.hasErrors())
            return new ResultResponse(false,results.getFieldError().getDefaultMessage());
        PageInfo<MovesayMoneyActiveListVO> movesayMoneyActiveListSys = moneyActiveUserListService.selectByPageParam(rushPageParam);
        return new ResultResponse(true, movesayMoneyActiveListSys);
    }

    //查询所有活动
    @PostMapping("activeName")
    public ResultResponse activeName() {
         List<Active> actives=moneyActiveService.selectActive();
         return new ResultResponse(true, actives);
    }

    //推广收益记录
    @PostMapping("profitsRecord")
    public ResultResponse profitsRecord(@RequestBody @Valid  profitParam profitParam,BindingResult results) {
        PageInfo<PromoteProfit> promoteProfits =promoteFundService.getPromoteListAll(profitParam);
        return new ResultResponse(true, promoteProfits);
    }

}

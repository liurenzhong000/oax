package com.oax.admin.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.oax.admin.enums.ResourcesTypeEnum;
import com.oax.admin.service.ResourcesService;
import com.oax.admin.shiro.ShiroFilterChainManager;
import com.oax.common.ResultResponse;
import com.oax.entity.admin.Resources;
import com.oax.entity.admin.vo.MenuVo;

import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: 陈德智
 * Date: 2018/5/30
 * Time: 15:43
 * 权限资源 Controller
 */
@RestController
@RequestMapping("/resources")
@Slf4j
public class ResourcesController {

//    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResourcesService resourcesService;


    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;


    @GetMapping
    public ResultResponse getResourcesList() {
        List<Resources> pageInfo = resourcesService.selectAll();
        return new ResultResponse(true, pageInfo);
    }

    @GetMapping("/resourcesWithSelected/{roleId}")
    public ResultResponse resourcesWithSelected(@PathVariable(name = "roleId") Integer roleId) {

        List<MenuVo> resources = resourcesService.selectResourcesListWithSelected(roleId);

        Map<Integer, List<MenuVo>> listMap = resources.stream().collect(Collectors.groupingBy(MenuVo::getParentid));

        List<MenuVo> listByParentId = getListByParentId(listMap, 1);
        return new ResultResponse(true, listByParentId);
    }

    /**
     * 用户 菜单
     *
     * @return
     */
    @GetMapping("/loadMenu")
    public ResultResponse loadMenu() {
        Integer userid = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        List<MenuVo> resourcesList = resourcesService.selectUserMenu(userid);
        return new ResultResponse(true, resourcesList);
    }

    private List<MenuVo> getListByParentId(Map<Integer, List<MenuVo>> collect, Integer id) {

        List<MenuVo> menuVos = collect.get(id);
        if (CollectionUtils.isNotEmpty(menuVos)) {
            for (MenuVo menuVo : menuVos) {


                int pId = menuVo.getId();
                List<MenuVo> listByParentId = getListByParentId(collect, pId);
                if (listByParentId!=null){
                    menuVo.setChecked(false);
                }
                menuVo.setChildrens(listByParentId);

            }
        }
        return menuVos;


    }


    /**
     * 所有菜单 (添加资源时使用)
     *
     * @return
     */
    @GetMapping("/menus")
    public ResultResponse getAllMenu() {
        List<Resources> resourcesList = resourcesService.selectAllByType(ResourcesTypeEnum.MENU.getResourcesType());

        return new ResultResponse(true, resourcesList);
    }

    @PostMapping
    public ResultResponse add(@RequestBody Resources resources) {
        try {
            int save = resourcesService.save(resources);
            shiroFilterChainManager.reloadFilterChain();

            if (save > 0) {
                log.info("添加资源成功:::{}", JSON.toJSONString(resources));
                return new ResultResponse(true, "添加资源成功");
            } else {
                log.warn("添加资源失败:::{}", JSON.toJSONString(resources));
                return new ResultResponse(false, "添加资源失败");
            }
        } catch (Exception e) {
            log.error("添加资源失败", e);
            return new ResultResponse(false, "添加资源失败");
        }
    }

    @DeleteMapping("/{resourcesId}")
    public ResultResponse del(@PathVariable(name = "resourcesId") int resourcesId) {
        try {
            int delete = resourcesService.delete(resourcesId);
            shiroFilterChainManager.reloadFilterChain();
            if (delete > 0) {
                log.info("删除资源成功:::{}", resourcesId);
                return new ResultResponse(true, "删除资源成功");
            } else {
                log.warn("删除资源失败:::{}", resourcesId);
                return new ResultResponse(false, "删除资源失败");
            }
        } catch (Exception e) {
            log.error("删除资源失败", e);
            return new ResultResponse(false, "删除资源失败");
        }
    }

}

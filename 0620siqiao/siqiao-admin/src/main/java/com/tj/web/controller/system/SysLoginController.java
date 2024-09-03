package com.tj.web.controller.system;

import com.tj.common.constant.Constants;
import com.tj.common.core.domain.AjaxResult;
import com.tj.common.core.domain.entity.SysMenu;
import com.tj.common.core.domain.entity.SysUser;
import com.tj.common.core.domain.model.LoginBody;
import com.tj.common.utils.SecurityUtils;
import com.tj.framework.web.service.SysLoginService;
import com.tj.framework.web.service.SysPermissionService;
import com.tj.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody, HttpServletResponse response) {
        AjaxResult ajax = AjaxResult.success();


        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        ResponseCookie cookie = ResponseCookie.from(Constants.TOKEN, token) // key & value
                .httpOnly(true)        // 禁止js读取
                .secure(false)        // 在http下也传输
                .domain("115.238.57.228")// 域名
                .path("/")            // path
                .maxAge(Duration.ofHours(5))    // 1个小时候过期
                .sameSite("Lax")    // 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                .build();

        // 设置Cookie Header
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ajax;
    }


    @PostMapping("/loginonlyname")
    public AjaxResult loginonlyname(@RequestBody LoginBody loginBody, HttpServletResponse response) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.loginonlyname(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        ResponseCookie cookie = ResponseCookie.from(Constants.TOKEN, token) // key & value
                .httpOnly(true)        // 禁止js读取
                .secure(false)        // 在http下也传输
                .domain("115.238.57.228")// 域名
                .path("/")            // path
                .maxAge(Duration.ofHours(5))    // 1个小时候过期
                .sameSite("Lax")    // 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外
                .build();

        // 设置Cookie Header
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}

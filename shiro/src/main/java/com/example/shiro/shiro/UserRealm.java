package com.example.shiro.shiro;

import com.example.shiro.constants.LoginConstant;
import com.example.shiro.model.SysUser;
import com.example.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component("userRealm")
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Session session = SecurityUtils.getSubject().getSession();
        SysUser user = (SysUser) session.getAttribute(LoginConstant.SESSION_USER_INFO);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        List<String> collect = user.getPermissions().stream().map(x -> x.getPermissionCode()).collect(Collectors.toList());
        authorizationInfo.addRole(user.getRoles().getRoleName());
        authorizationInfo.addStringPermissions(collect);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName= (String) authenticationToken.getPrincipal();
        SysUser user = null;
        if (user == null) {
            throw new UnknownAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(userName), //采用明文访问时，不需要此句
                getName()
        );
        //将用户信息放入session中
        SecurityUtils.getSubject().getSession().setAttribute(LoginConstant.SESSION_USER_INFO, user);
        return authenticationInfo;
    }
}

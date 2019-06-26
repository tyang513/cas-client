package com.talkingdata.security.authentication;


import com.tendcloud.enterprise.um.umic.entity.Tenant;
import com.tendcloud.enterprise.um.umic.entity.User;
import com.tendcloud.enterprise.um.umic.entity.VirtualRole;
import com.tendcloud.enterprise.um.umic.rmi.BusinessException;
import com.tendcloud.enterprise.um.umic.rmi.SecurityService;
import com.tendcloud.enterprise.um.umic.rmi.UmRmiServiceFactory;
import org.apache.log4j.Logger;
import org.springframework.security.cas.authentication.CasAuthenticationToken;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * 菜单初始化 获取用户信息 获取资源类型信息，把权限信息放入session <br/>
 * session描述 <br/>
 * key:user 用户在um上维护的详细信息 <br/>
 * key:roleList 当前登陆用户在当前应用系统的角色信息 <br/>
 * key:menuList 系统的菜单详细信息 <br/>
 * key:UserACL 系统的按钮信息 <br/>
 * key:authList 登陆用户的权限信息 <br/>
 *
 *
 * application描述 <br/>
 * appcode：系统编码 <br/>
 * apptaken：系统令牌 <br/>
 *
 *	filter的用户描述如下（上面的配置信息在web.xml中配置，所实现的全部功能都有配置）	 <br/>
 *	配置可在部署项目的classpath下的文件sysConfig.properties中进行覆盖配置 key即为param-name；value为param-value，如果这个文件中配置会把web.xml中的配置给覆盖掉
 *	1：appcode 必须配置 是在um系统中所维护的系统编码	 <br/>
 *	2：apptaken 必须配置 是在um系统中所维护的系统令牌	 <br/>
 *	3：menu 必须配置 是在um系统中所维护的资源类型中菜单code	 <br/>
 *	4：button 必须配置 是在um系统中所维护的资源类型中按钮code	 <br/>
 *	5：typeList 非必须配置 参数为在um系统中维护的资源类型编码 ，如果多个用英文“,”号隔开，配置后，系统会根据放在session中，key为资源类型编码，
 *	如上：会在session中有MENU,BUTTON,TEST三个值	 <br/>
 *
 *	@author changpengfei
 */
public class AppBuilderAuthenticationFilter implements Filter {

    private static final String SESSION_USER_FLAG = "user";

    public static String dcdsdata;

    public static Logger logger = Logger.getLogger(AppBuilderAuthenticationFilter.class);

    public String apptaken="";

    public String appcode="";

    public String typeList="";

    public String menu="";

    public String button;

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request0=(HttpServletRequest) request;
        Principal principal = request0.getUserPrincipal();
        HttpSession session = request0.getSession();
        session.setMaxInactiveInterval(24 * 60 * 60);
        User user = (User) session.getAttribute(SESSION_USER_FLAG);
        SecurityService securityService;
        try{
            securityService=UmRmiServiceFactory.getSecurityService();
        }catch (Exception e){
            logger.error("UmRmiServiceFactory初始化错误",e);
            throw e;
        }

        if (user == null && principal instanceof CasAuthenticationToken) {
            CasAuthenticationToken p = (CasAuthenticationToken) principal;
            user = new User();
            //得到当前用户
            try {
                user= securityService.getUserByUmId(p.getUserDetails().getUsername() == null ? null : p.getUserDetails().getUsername());
            } catch (BusinessException e) {
                logger.error("执行失败",e);
                logger.error("获取用户失败");
            }
            //得到当前租户
            try {
                Tenant tenant = securityService.getTenantByUmId(user.getUmid());
                if(tenant == null){
                    tenant = new Tenant();
                }
                session.setAttribute("tenant", tenant);
            } catch (BusinessException e) {
                logger.error("执行失败",e);
                logger.error("获取租户失败");
            }
            session.setAttribute("user", user);

        }
        try {
            Object object = session.getAttribute("roleList");
            if(object == null && user != null){

                //用户拥有的角色放入session
                List<VirtualRole> roleList= securityService.getRoleListByUmId(user.getEmail());
                session.setAttribute("roleList", roleList);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        this.apptaken=arg0.getInitParameter("apptaken");
        this.appcode=arg0.getInitParameter("appcode");
        this.menu=arg0.getInitParameter("menu");
        this.button=arg0.getInitParameter("button");
        this.typeList=arg0.getInitParameter("typeList");
    }

}

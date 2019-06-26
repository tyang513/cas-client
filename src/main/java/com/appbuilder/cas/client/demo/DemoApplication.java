package com.appbuilder.cas.client.demo;

import com.tendcloud.enterprise.um.umic.entity.Tenant;
import com.tendcloud.enterprise.um.umic.entity.User;
import com.tendcloud.enterprise.um.umic.entity.VirtualRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        ConfigurableApplicationContext context = app.run(args);

        Environment env = context.getEnvironment();
        logger.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index() {
        logger.info("index");
        return "index";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        logger.info("hello world");
        return "hello world";
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getUser(HttpServletRequest request) {
        Map<String,Object> userInfoMap=new HashMap<>();
        User user=(User)request.getSession().getAttribute("user");
        Tenant tenant=(Tenant)request.getSession().getAttribute("tenant");
        List<VirtualRole> roleList=(List<VirtualRole>)request.getSession().getAttribute("roleList");
        userInfoMap.put("user",user);
        userInfoMap.put("tenant",tenant);
        userInfoMap.put("roleList",roleList);
        return userInfoMap;
    }
}


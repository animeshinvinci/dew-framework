package com.tairanchina.csp.dew.core.test.auth;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.Dew;
import com.tairanchina.csp.dew.core.DewContext;
import com.tairanchina.csp.dew.core.logger.DewLogger;
import com.tairanchina.csp.dew.core.test.auth.dto.OptInfoExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthExampleController {

    private static Map<String, User> MOCK_USER_CONTAINER = new HashMap<>();

    private Logger dewLogger = DewLogger.getLogger(AuthExampleController.class);
    private Logger oriLogger = LoggerFactory.getLogger(AuthExampleController.class);

    @PostConstruct
    public void init() {
        DewContext.setOptInfoClazz(OptInfoExt.class);
    }

    /**
     * 模拟用户注册
     */
    @PostMapping(value = "user/register")
    public Resp<Void> register(@RequestBody User user) {
        // 实际注册处理
        user.setId($.field.createUUID());
        MOCK_USER_CONTAINER.put(user.getId(), user);
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        return Resp.success(null);
    }

    /**
     * 模拟用户登录
     */
    @PostMapping(value = "auth/login")
    public Resp<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
        // 实际登录处理
        User user = MOCK_USER_CONTAINER.values().stream().filter(u -> u.getIdCard().equals(loginDTO.getIdCard())).findFirst().get();
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw Dew.E.e("ASXXX0", new Exception("密码错误"));
        }
        String token = $.field.createUUID();
        Dew.auth.setOptInfo(new OptInfoExt()
                .setIdCard(user.getIdCard())
                .setAccountCode($.field.createShortUUID())
                .setToken(token)
                .setName(user.getName())
                .setMobile(user.getPhone()));
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        return Resp.success(token);
    }

    /**
     * 模拟业务操作
     */
    @GetMapping(value = "business/someopt")
    public Resp<? extends Object> someOpt() {
        dewLogger.info("Dew.context().getId():     " + Dew.context().getId());
        dewLogger.info("Dew.context().getToken():     " + Dew.context().getToken());
        // 获取登录用户信息
        Optional<OptInfoExt> optInfoExtOpt = Dew.auth.getOptInfo();
        dewLogger.info("-----test-----");
        dewLogger.info("dewLogger:        info");
        oriLogger.info("oriLogger:        info");
        dewLogger.info("dewLogger.getname:        " + dewLogger.getName());


        try {
            dewLogger.info("dewLogger.isInfoEnabled:     " + dewLogger.isInfoEnabled());
            dewLogger.info("dewLogger.info:      TEST-INFO");
            dewLogger.info("dewLogger.info:      TEST-INFO", optInfoExtOpt.get());
            dewLogger.info("dewLogger.info:      TEST-INFO", optInfoExtOpt.get(), new Date());
            dewLogger.info("dewLogger.info:      TEST-INFO", optInfoExtOpt.get(), new Date(), new Date());
            dewLogger.info("dewLogger.info:      TEST-INFO", optInfoExtOpt.get(), new Date(), new Date(), new Date());
            dewLogger.info("dewLogger.info:      TEST-INFO", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isInfoEnabled(marker):      TEST-WARN", dewLogger.isInfoEnabled(MarkerFactory.getMarker("INFO-MARK")));
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO");
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO", optInfoExtOpt.get());
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO", new Date(), optInfoExtOpt.get());
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO", new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO", new Date(), new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.info(MarkerFactory.getMarker("INFO-MARK"), "TEST-INFO", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isDebugEnabled:     " + dewLogger.isDebugEnabled());
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG");
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG", optInfoExtOpt.get());
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG", optInfoExtOpt.get(), new Date());
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG", optInfoExtOpt.get(), new Date(), new Date());
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG", optInfoExtOpt.get(), new Date(), new Date(), new Date());
            dewLogger.debug("dewLogger.debug:      TEST-DEBUG", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isDebugEnabled(marker):      TEST-WARN", dewLogger.isDebugEnabled(MarkerFactory.getMarker("DEBUG-MARK")));
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG");
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG", optInfoExtOpt.get());
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG", new Date(), optInfoExtOpt.get());
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG", new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG", new Date(), new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.debug(MarkerFactory.getMarker("DEBUG-MARK"), "TEST-DEBUG", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isWarnEnable:     " + dewLogger.isWarnEnabled());
            dewLogger.warn("dewLogger.warn:      TEST-WARN");
            dewLogger.warn("dewLogger.warn:      TEST-WARN", optInfoExtOpt.get());
            dewLogger.warn("dewLogger.warn:      TEST-WARN", optInfoExtOpt.get(), new Date());
            dewLogger.warn("dewLogger.warn:      TEST-WARN", optInfoExtOpt.get(), new Date(), new Date());
            dewLogger.warn("dewLogger.warn:      TEST-WARN", optInfoExtOpt.get(), new Date(), new Date(), new Date());
            dewLogger.warn("dewLogger.warn:      TEST-WARN", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isWarnEnable(marker):      TEST-WARN", dewLogger.isWarnEnabled(MarkerFactory.getMarker("WARN-MARK")));
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN");
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN", optInfoExtOpt.get());
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN", new Date(), optInfoExtOpt.get());
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN", new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN", new Date(), new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.warn(MarkerFactory.getMarker("WARN-MARK"), "TEST-WARN", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isErrorEnable:     " + dewLogger.isErrorEnabled());
            dewLogger.error("dewLogger.error:      TEST-ERROR");
            dewLogger.error("dewLogger.error:      TEST-ERROR", optInfoExtOpt.get());
            dewLogger.error("dewLogger.error:      TEST-ERROR", optInfoExtOpt.get(), new Date());
            dewLogger.error("dewLogger.error:      TEST-ERROR", optInfoExtOpt.get(), new Date(), new Date());
            dewLogger.error("dewLogger.error:      TEST-ERROR", optInfoExtOpt.get(), new Date(), new Date(), new Date());
            dewLogger.error("dewLogger.error:      TEST-ERROR", new Exception("TEST-THROWABLE"));

        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isErrorEnable(marker):      TEST-ERROR", dewLogger.isErrorEnabled(MarkerFactory.getMarker("ERROR-MARK")));
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR");
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR", optInfoExtOpt.get());
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR", new Date(), optInfoExtOpt.get());
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR", new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR", new Date(), new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.error(MarkerFactory.getMarker("ERROR-MARK"), "TEST-ERROR", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isTraceEnabled:     " + dewLogger.isTraceEnabled());
            dewLogger.trace("dewLogger.trace:      TEST-TRACE");
            dewLogger.trace("dewLogger.trace:      TEST-TRACE", optInfoExtOpt.get());
            dewLogger.trace("dewLogger.trace:      TEST-TRACE", optInfoExtOpt.get(), new Date());
            dewLogger.trace("dewLogger.trace:      TEST-TRACE", optInfoExtOpt.get(), new Date(), new Date());
            dewLogger.trace("dewLogger.trace:      TEST-TRACE", optInfoExtOpt.get(), new Date(), new Date(), new Date());
            dewLogger.trace("dewLogger.trace:      TEST-TRACE", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        try {
            dewLogger.info("dewLogger.isTraceEnabled(marker):      TEST-WARN", dewLogger.isTraceEnabled(MarkerFactory.getMarker("INFO-MARK")));
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE");
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE", optInfoExtOpt.get());
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE", new Date(), optInfoExtOpt.get());
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE", new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE", new Date(), new Date(), new Date(), optInfoExtOpt.get());
            dewLogger.trace(MarkerFactory.getMarker("TRACE-MARK"), "TEST-TRACE", new Exception("TEST-THROWABLE"));
        } catch (Exception e) {
            dewLogger.info("e.getMessage:        " + e.getMessage());
        }
        dewLogger.info("-----test-----");
        return optInfoExtOpt.<Resp<? extends Object>>map(Resp::success).orElseGet(() -> Resp.unAuthorized("用户认证错误"));
    }

    /**
     * 模拟用户注销
     */
    @DeleteMapping(value = "auth/logout")
    public Resp<Void> logout() {
        dewLogger.info("dewLogger:        TEST");
        oriLogger.info("oriLogger:        TEST");
        // 实际注册处理
        Dew.auth.removeOptInfo();
        return Resp.success(null);
    }

    public static class LoginDTO {

        private String idCard;

        private String password;

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class User {

        private String id;

        private String name;

        private String phone;

        private String idCard;

        private String password;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }
    }

}

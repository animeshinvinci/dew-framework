package com.tairanchina.csp.dew.core.interceptor;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.DewContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

/**
 * Dew Servlet拦截器
 */
public class DewHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DewHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestFrom = request.getHeader(Dew.Constant.HTTP_REQUEST_FROM_FLAG);
        // 仅支持白名单内的服务
        if (Dew.dewConfig.getSecurity().getIncludeServices() != null) {
            for (String v : Dew.dewConfig.getSecurity().getIncludeServices()) {
                if (!v.equalsIgnoreCase(requestFrom)) {
                    throw Dew.E.e("401", new AuthException("The [" + requestFrom + "] does NOT allow access to this service."),401);
                }
            }
        }
        // 排除黑名单中的服务
        if (Dew.dewConfig.getSecurity().getIncludeServices() == null && Dew.dewConfig.getSecurity().getExcludeServices() != null) {
            for (String v : Dew.dewConfig.getSecurity().getExcludeServices()) {
                if (v.equalsIgnoreCase(requestFrom)) {
                    throw Dew.E.e("401", new AuthException("The [" + requestFrom + "] does NOT allow access to this service."),401);
                }
            }
        }
        // 配置跨域参数
        response.addHeader("Access-Control-Allow-Origin", Dew.dewConfig.getSecurity().getCors().getAllowOrigin());
        response.addHeader("Access-Control-Allow-Methods", Dew.dewConfig.getSecurity().getCors().getAllowMethods());
        response.addHeader("Access-Control-Allow-Headers", Dew.dewConfig.getSecurity().getCors().getAllowHeaders());
        response.addHeader("Access-Control-Max-Age", "3600000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (request.getMethod().equalsIgnoreCase("OPTIONS") || request.getMethod().equalsIgnoreCase("HEAD")) {
            return super.preHandle(request, response, handler);
        }

        String token;
        if (Dew.dewConfig.getSecurity().isTokenInHeader()) {
            token = request.getHeader(Dew.dewConfig.getSecurity().getTokenFlag());
        } else {
            token = request.getParameter(Dew.dewConfig.getSecurity().getTokenFlag());
        }
        if (token != null) {
            token = URLDecoder.decode(token, "UTF-8");
            if (Dew.dewConfig.getSecurity().isTokenHash()) {
                token = $.encrypt.symmetric.encrypt(token, "MD5");
            }
        }
        DewContext context = new DewContext();
        context.setId($.field.createUUID());
        context.setSourceIP(Dew.Util.getRealIP(request));
        context.setRequestUri(request.getRequestURI());
        context.setToken(token);
        DewContext.setContext(context);
        logger.trace("[{}] {}{} from {}", request.getMethod(), request.getRequestURI(), request.getQueryString() == null ? "" : "?" + request.getQueryString(), context.getSourceIP());
        return super.preHandle(request, response, handler);
    }

}

package com.ecfront.dew.gateway.auth;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.Resp;
import com.ecfront.dew.core.Dew;
import com.ecfront.dew.core.dto.OptInfo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private Void filterHit(RequestContext ctx, Resp<?> resp) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody($.json.toJsonString(resp));
        logger.warn("Auth Filter Hit [{}] {}", resp.getCode(), resp.getMessage());
        return null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        ctx.getResponse().setCharacterEncoding("UTF-8");
        String ip = Dew.Util.getRealIP(request);
        String requestPath = request.getRequestURI();
        logger.trace("[{}] {}{} from {}", request.getMethod(), requestPath, request.getQueryString() == null ? "" : "?" + request.getQueryString(), ip);
        requestPath = requestPath.substring(requestPath.substring(1).indexOf("/") + 1);
        if (requestPath.startsWith("/public")) {
            return null;
        }
        String token = request.getParameter(Dew.dewConfig.getSecurity().getTokenFlag());
        if (token == null) {
            return filterHit(ctx, Resp.unAuthorized("Token not exist，Request parameter must include " + Dew.dewConfig.getSecurity().getTokenFlag()));
        }
        Optional<String> bestMathResourceCode = LocalCacheContainer.getBestMathResourceCode(request.getMethod(), request.getRequestURI());
        if (!bestMathResourceCode.isPresent()) {
            // Not found -> The request path Don't require authentication
            return null;
        }
        String optInfoStr = Dew.cluster.cache.get(Dew.Constant.TOKEN_INFO_FLAG + token);
        if (optInfoStr == null) {
            return filterHit(ctx, Resp.unAuthorized("Token not exist"));
        }
        OptInfo optInfo = $.json.toObject(optInfoStr, OptInfo.class);
        boolean authentication = LocalCacheContainer.auth(optInfo.getRoles().stream().map(OptInfo.RoleInfo::getCode).collect(Collectors.toSet()), bestMathResourceCode.get());
        if (authentication) {
            return null;
        } else {
            return filterHit(ctx, Resp.unAuthorized("Account [" + optInfo.getAccountCode() + "] no access to " + request.getMethod() + ":" + request.getRequestURI()));
        }
    }

}

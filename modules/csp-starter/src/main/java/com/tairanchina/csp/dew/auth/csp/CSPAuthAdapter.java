package com.tairanchina.csp.dew.auth.csp;

import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.core.auth.AuthAdapter;
import com.tairanchina.csp.dew.core.dto.OptInfo;

import java.util.Optional;

/**
 * desription: 用户中心适配
 * Created by ding on 2017/10/27.
 */
public class CSPAuthAdapter implements AuthAdapter {

    @Override
    public <E extends OptInfo> Optional<E> getOptInfo(String token) {
        return Dew.context().optInfo();
    }

    @Override
    public void removeOptInfo(String token) {
        Dew.context().setInnerOptInfo(Optional.empty());
    }

    @Override
    public <E extends OptInfo> void setOptInfo(E optInfo) {
        Dew.context().setInnerOptInfo(Optional.of(optInfo));
    }
}

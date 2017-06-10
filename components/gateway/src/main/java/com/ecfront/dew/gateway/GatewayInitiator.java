package com.ecfront.dew.gateway;


import com.ecfront.dew.common.$;
import com.ecfront.dew.core.Dew;
import com.ecfront.dew.gateway.auth.LocalCacheContainer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Component
public class GatewayInitiator {

    @PostConstruct
    public void init() {
        Dew.cluster.mq.subscribe(Dew.Constant.MQ_AUTH_RESOURCE_ADD, message -> {
            JsonNode data = $.json.toJson(message);
            if (data.get("category").asText().isEmpty()) {
                LocalCacheContainer.addResource(data.get("code").asText(), data.get("method").asText(), data.get("uri").asText());
            }
        });
        Dew.cluster.mq.subscribe(Dew.Constant.MQ_AUTH_RESOURCE_REMOVE, LocalCacheContainer::removeResource);
        Dew.cluster.mq.subscribe(Dew.Constant.MQ_AUTH_ROLE_ADD, message -> {
            JsonNode data = $.json.toJson(message);
            LocalCacheContainer.addRole(data.get("code").asText(), new HashSet<>(data.get("resources").findValuesAsText("code")));
        });
        Dew.cluster.mq.subscribe(Dew.Constant.MQ_AUTH_ROLE_REMOVE, LocalCacheContainer::removeRole);
        Dew.cluster.mq.request(Dew.Constant.MQ_AUTH_REFRESH, "");
    }

}

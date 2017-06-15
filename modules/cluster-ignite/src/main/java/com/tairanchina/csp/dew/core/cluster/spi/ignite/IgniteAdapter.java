package com.tairanchina.csp.dew.core.cluster.spi.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnExpression("#{'${dew.cluster.cache}'=='ignite' || '${dew.cluster.mq}'=='ignite' || '${dew.cluster.dist}'=='ignite'}")
public class IgniteAdapter {

    @Autowired
    private IgniteConfig config;

    private Ignite ignite;

    @PostConstruct
    public void init() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        if (!config.getAddresses().isEmpty() || !config.getMulticastGroup().isEmpty()) {
            TcpDiscoverySpi spi = new TcpDiscoverySpi();
            if (!config.getAddresses().isEmpty() && !config.getMulticastGroup().isEmpty()) {
                TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
                ipFinder.setAddresses(config.getAddresses());
                ipFinder.setMulticastGroup(config.getMulticastGroup());
                spi.setIpFinder(ipFinder);
            } else if (!config.getAddresses().isEmpty()) {
                TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
                ipFinder.setAddresses(config.getAddresses());
                spi.setIpFinder(ipFinder);
            } else {
                TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
                ipFinder.setMulticastGroup(config.getMulticastGroup());
                spi.setIpFinder(ipFinder);
            }
            cfg.setDiscoverySpi(spi);
        }
        if (config.isClient()) {
            cfg.setClientMode(true);
        }
        ignite = Ignition.start(cfg);
    }

    public Ignite getIgnite() {
        return ignite;
    }
}

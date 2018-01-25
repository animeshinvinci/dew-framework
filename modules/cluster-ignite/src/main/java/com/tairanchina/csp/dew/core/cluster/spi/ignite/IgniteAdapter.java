package com.tairanchina.csp.dew.core.cluster.spi.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;


import javax.annotation.PostConstruct;

public class IgniteAdapter {

    private IgniteConfig config;

    private Ignite ignite;

    public IgniteAdapter(IgniteConfig config) {
        this.config = config;
    }

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
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("dew-boot");
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cfg.setCacheConfiguration(cacheConfiguration);
        TransactionConfiguration transactionConfiguration = new TransactionConfiguration();
        cfg.setTransactionConfiguration(transactionConfiguration);
        ignite = Ignition.start(cfg);
    }

    public Ignite getIgnite() {
        return ignite;
    }
}

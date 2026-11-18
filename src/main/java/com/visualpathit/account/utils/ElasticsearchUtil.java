
package com.visualpathit.account.utils;

import java.net.InetSocketAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import org.springframework.stereotype.Service;

import com.visualpathit.account.beans.Components;

@Service
public class ElasticsearchUtil {

    private final Components components;

    public ElasticsearchUtil(Components components) {
        this.components = components;
    }

    public TransportClient transportClient() {

        String host = components.getElasticsearchHost();
        String port = components.getElasticsearchPort();
        String cluster = components.getElasticsearchCluster();
        String node = components.getElasticsearchNode();

        if (host == null || port == null ||
            cluster == null || node == null) {
            throw new IllegalStateException(
                "Elasticsearch configuration is missing");
        }

        int portNumber;

        try {
            portNumber = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid Elasticsearch port: " + port, e);
        }

        Settings settings = Settings.builder()
                .put("cluster.name", cluster)
                .put("node.name", node)
                .build();

        return new PreBuiltTransportClient(settings)
                .addTransportAddress(
                    new InetSocketTransportAddress(
                        new InetSocketAddress(host, portNumber)));
    }
}

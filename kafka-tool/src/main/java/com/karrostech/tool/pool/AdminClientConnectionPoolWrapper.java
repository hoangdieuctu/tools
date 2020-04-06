package com.karrostech.tool.pool;

import com.karrostech.tool.model.PartitionData;
import com.karrostech.tool.model.TopicConfigValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class AdminClientConnectionPoolWrapper {

    private BlockingQueue<AdminClient> pool;

    public AdminClientConnectionPoolWrapper(int maxSize) {
        this.pool = new LinkedBlockingQueue<>(maxSize);
    }

    public void close() {
        pool.forEach(connection -> connection.close());
        pool.clear();
    }

    public void offer(AdminClient client) {
        pool.offer(client);
    }


    public ListConsumerGroupsResult listConsumerGroups() {
        try {
            AdminClient connection = pool.take();

            try {
                ListConsumerGroupsResult consumerGroups = connection.listConsumerGroups();
                return consumerGroups;
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return null;
    }

    public DescribeConsumerGroupsResult describeConsumerGroups(Collection<String> groupIds) {
        try {
            AdminClient connection = pool.take();

            try {
                DescribeConsumerGroupsResult describe = connection.describeConsumerGroups(groupIds);
                return describe;
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return null;
    }

    public ListConsumerGroupOffsetsResult listConsumerGroupOffsets(String groupId) {
        try {
            AdminClient connection = pool.take();

            try {
                ListConsumerGroupOffsetsResult offsets = connection.listConsumerGroupOffsets(groupId);
                return offsets;
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return null;
    }

    public void deleteConsumerGroups(Collection<String> groupIds) {
        try {
            AdminClient connection = pool.take();

            try {
                DeleteConsumerGroupsResult result = connection.deleteConsumerGroups(groupIds);
                result.all().get();
            } catch (ExecutionException e) {
                log.warn("Error while executing delete consumer groups", e.getMessage());
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }
    }

    public List<PartitionData> getTopicDetails(String topic) {
        List<PartitionData> results = new ArrayList<>();

        try {
            AdminClient connection = pool.take();

            try {
                DescribeTopicsResult result = connection.describeTopics(Collections.singleton(topic));
                Map<String, TopicDescription> descriptions = result.all().get();

                TopicDescription info = descriptions.get(topic);
                if (info != null) {
                    List<TopicPartitionInfo> partitions = info.partitions();
                    partitions.forEach(p -> {
                        PartitionData obj = new PartitionData();
                        obj.copy(p);

                        results.add(obj);
                    });
                }
            } catch (ExecutionException e) {
                log.warn("Error while executing describe topic", e.getMessage());
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return results;
    }

    public List<TopicConfigValue> describeTopic(String topic) {
        List<TopicConfigValue> results = new ArrayList<>();

        try {
            AdminClient connection = pool.take();

            ConfigResource request = new ConfigResource(ConfigResource.Type.TOPIC, topic);
            Collection<ConfigResource> configResources = Collections.singleton(request);
            DescribeConfigsResult response = connection.describeConfigs(configResources);
            try {
                Map<ConfigResource, Config> resources = response.all().get();
                Config config = resources.get(request);

                if (config != null) {
                    Collection<ConfigEntry> entries = config.entries();
                    entries.forEach(entry -> results.add(new TopicConfigValue(entry)));
                }
            } catch (ExecutionException ex) {
                log.error("Error while getting config resource. {}", ex.getMessage());
            } finally {
                offer(connection);
            }
        } catch (InterruptedException e) {
            log.error("Error while getting connection from pool. {}", e.getMessage());
        }

        return results;
    }
}

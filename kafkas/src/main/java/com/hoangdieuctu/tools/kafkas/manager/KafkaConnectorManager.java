package com.hoangdieuctu.tools.kafkas.manager;

import com.hoangdieuctu.tools.kafkas.constant.Constants;
import com.hoangdieuctu.tools.kafkas.model.EnvConfig;
import com.hoangdieuctu.tools.kafkas.pool.AdminClientConnectionPoolWrapper;
import com.hoangdieuctu.tools.kafkas.pool.ConsumerLagConnectionPoolWrapper;
import com.hoangdieuctu.tools.kafkas.util.EnvironmentHolder;
import com.omarsmak.kafka.consumer.lag.monitoring.client.KafkaConsumerLagClient;
import com.omarsmak.kafka.consumer.lag.monitoring.client.KafkaConsumerLagClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@Slf4j
@Component
public class KafkaConnectorManager {

    private final String CONSUMER_GROUP = Constants.CONSUMER_GROUP_NAME;
    private final String PRODUCER_GROUP = Constants.PRODUCER_GROUP_NAME;

    private Map<EnvConfig, ConsumerLagConnectionPoolWrapper> consumerLagClients = new HashMap<>();
    private Map<EnvConfig, AdminClientConnectionPoolWrapper> clients = new HashMap<>();

    private Map<EnvConfig, KafkaProducer<String, String>> producers = new HashMap<>();

    @Value("${max.kafka.admin.client.connection.pool}")
    private int maxKafkaAdminConnectionPoolSize;

    @Value("${max.kafka.consumer.lag.connection.pool}")
    private int maxConsumerLagConnectionPoolSize;

    @Autowired
    private EnvironmentHolder envsHolder;

    @PostConstruct
    public void init() {
        log.info("Init kafka producers, admin clients and consumer lag clients");

        List<EnvConfig> envs = envsHolder.getConfigs();
        for (EnvConfig env : envs) {
            KafkaProducer<String, String> producer = initKafkaProducer(env.getBootstrapServer(), env.getName());
            producers.put(env, producer);

            AdminClientConnectionPoolWrapper adminClientPool = initAdminClientConnectionPool(env);
            clients.put(env, adminClientPool);

            ConsumerLagConnectionPoolWrapper consumerLagPool = initKafkaConsumerLagConnectionPool(env);
            consumerLagClients.put(env, consumerLagPool);

        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroy kafka producers");
        Set<EnvConfig> producerKeys = producers.keySet();
        producerKeys.forEach(e -> producers.get(e).close());

        log.info("Destroy admin clients");
        Set<EnvConfig> adminClientKeys = clients.keySet();
        adminClientKeys.forEach(a -> clients.get(a).close());

        log.info("Destroy consumer lag clients");
        Set<EnvConfig> lagClients = consumerLagClients.keySet();
        lagClients.forEach(l -> consumerLagClients.get(l).close());
    }

    private ConsumerLagConnectionPoolWrapper initKafkaConsumerLagConnectionPool(EnvConfig env) {
        log.info("Init kafka consumer lag client connection pool ({}): {}", env, maxConsumerLagConnectionPoolSize);
        ConsumerLagConnectionPoolWrapper pool = new ConsumerLagConnectionPoolWrapper(maxConsumerLagConnectionPoolSize);
        for (int i = 0; i < maxConsumerLagConnectionPoolSize; i++) {
            KafkaConsumerLagClient connection = initConsumerLagClient(env.getBootstrapServer());
            pool.offer(connection);
        }

        return pool;
    }

    private AdminClientConnectionPoolWrapper initAdminClientConnectionPool(EnvConfig env) {
        log.info("Init admin client connection pool ({}): {}", env, maxKafkaAdminConnectionPoolSize);
        AdminClientConnectionPoolWrapper pool = new AdminClientConnectionPoolWrapper(maxKafkaAdminConnectionPoolSize);
        for (int i = 0; i < maxKafkaAdminConnectionPoolSize; i++) {
            AdminClient connection = initKafkaAdminClient(env.getBootstrapServer());
            pool.offer(connection);
        }

        return pool;
    }

    public ConsumerLagConnectionPoolWrapper getConsumerLagClient(EnvConfig env) {
        return consumerLagClients.get(env);
    }

    private KafkaConsumerLagClient initConsumerLagClient(String bootstrapServer) {
        Properties properties = new Properties();
        properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        KafkaConsumerLagClient consumerLagClient = KafkaConsumerLagClientFactory.create(properties);
        return consumerLagClient;
    }

    public KafkaConsumer<String, String> getConsumer(EnvConfig env) {
        return initKafkaConsumer(env.getBootstrapServer());
    }

    public KafkaConsumer<String, String> getConsumer(EnvConfig env, String groupId) {
        return initKafkaConsumer(env.getBootstrapServer(), groupId);
    }

    public KafkaProducer<String, String> getProducer(EnvConfig env) {
        return producers.get(env);
    }

    public AdminClientConnectionPoolWrapper getAdminClient(EnvConfig env) {
        return clients.get(env);
    }

    private AdminClient initKafkaAdminClient(String bootstrapServer) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        return AdminClient.create(props);
    }

    private KafkaConsumer<String, String> initKafkaConsumer(String bootstrapServer, String groupId) {
        log.info("Init kafka consumer: {}", bootstrapServer);

        String group = groupId == null ? CONSUMER_GROUP : CONSUMER_GROUP + "-" + groupId;

        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);

        return new KafkaConsumer<>(props);
    }

    private KafkaConsumer<String, String> initKafkaConsumer(String bootstrapServer) {
        return initKafkaConsumer(bootstrapServer, null);
    }

    private KafkaProducer<String, String> initKafkaProducer(String bootstrapServer, String name) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, PRODUCER_GROUP + "-" + name.toLowerCase());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
}

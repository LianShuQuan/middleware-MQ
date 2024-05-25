//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.prodorder.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
//public class MongoConfig extends AbstractMongoClientConfiguration {
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authSource;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private Integer port;

    @Value(value = "${spring.data.mongodb.username}")
    private String username;

    @Value(value = "${spring.data.mongodb.password}")
    private String password;

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

//    @Override
//    protected String getDatabaseName() {
//        return this.database;
//    }

    @Bean
    public MongoClient mongoClient() {
//        String connectStr = String.format("mongodb://%s:%s@%s:%s/?authSource=%s", username, password, host, port, database);
        String connectStr = String.format("mongodb://%s:%s@%s:%s/%s?authSource=%s&retryWrites=false", username, password, host, port, database, database);
        return MongoClients.create(connectStr);
    }

//    @Override
//    public MongoClient mongoClient() {
//        return MongoClients.create(
//                MongoClientSettings.builder()
//                        .retryWrites(false)
//                        // 集群设置
//                        .applyToClusterSettings(builder ->
//                                builder.hosts(List.of(new ServerAddress(host, port)))
//                        )
//                        // 凭据
//                        .credential(
//                                MongoCredential.createCredential(username, database, password.toCharArray())
//                        )
//                        .build());
//    }
}
//package com.ecommerce.user.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//
//@Configuration
//public class MongoConfig extends AbstractMongoClientConfiguration {
//
//    @Override
//    protected String getDatabaseName() {
//        return "userdb";
//    }
//
//    @Override
//    @Bean
//    public MongoClient mongoClient() {
//        ConnectionString connectionString = new ConnectionString(
//            "mongodb+srv://crce9643ce_db_user:8BUlxGIzjfctRyGE@user-data.hba2oh5.mongodb.net/?retryWrites=true&w=majority"
//        );
//
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(connectionString)
//            .build();
//
//        System.out.println("===============================================");
//        System.out.println("✓ Connecting to MongoDB Atlas");
//        System.out.println("✓ Cluster: user-data.hba2oh5.mongodb.net");
//        System.out.println("✓ Database: userdb");
//        System.out.println("===============================================");
//
//        return MongoClients.create(mongoClientSettings);
//    }
//}

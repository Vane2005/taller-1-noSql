package com.noSQL.claseJava.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.domain.Sort;

@Configuration
public class MongoDBIndexConfig {

    @Bean
    public CommandLineRunner createIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("author.fullName", Sort.Direction.ASC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("editorial.name", Sort.Direction.ASC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("categories.name", Sort.Direction.ASC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("available", Sort.Direction.ASC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("title", Sort.Direction.ASC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("publicationYear", Sort.Direction.DESC));

                mongoTemplate.indexOps("books")
                        .ensureIndex(new Index().on("uniqueIdentifier", Sort.Direction.ASC).unique());
            } catch (Exception e) {
                System.err.println("Error creating indexes: " + e.getMessage());
            }
        };
    }
}

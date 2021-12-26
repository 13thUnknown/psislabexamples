//package info.stepanoff.trsis.samples.config;
//
//import info.stepanoff.trsis.samples.config.property.DatabaseProperties;
//import liquibase.Liquibase;
//import liquibase.database.DatabaseFactory;
//import liquibase.exception.LiquibaseException;
//import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
//import liquibase.resource.ClassLoaderResourceAccessor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Profile("!test")
//@Configuration
//@RequiredArgsConstructor
//@EnableConfigurationProperties(DatabaseProperties.class)
//public class LiquibaseConfig {
//
//    private final DatabaseProperties databaseProperties;
//
//    @Value("${spring.liquibase.change-log}")
//    private String changelogFile;
//
//    @Bean
//    public void liquibaseInit() throws LiquibaseException {
//        MongoLiquibaseDatabase database = (MongoLiquibaseDatabase) DatabaseFactory.getInstance().openDatabase(
//                databaseProperties.getUri(),
//                databaseProperties.getUsername(),
//                databaseProperties.getPassword(),
//                null,
//                null);
//        Liquibase liquibase = new Liquibase(changelogFile, new ClassLoaderResourceAccessor(), database);
//        liquibase.update("");
//    }
//}

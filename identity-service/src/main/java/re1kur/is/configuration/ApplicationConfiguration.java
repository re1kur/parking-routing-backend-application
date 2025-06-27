package re1kur.is.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "re1kur.is.repository.sql")
@EnableRedisRepositories(basePackages = "re1kur.is.repository.cache")
public class ApplicationConfiguration {

}

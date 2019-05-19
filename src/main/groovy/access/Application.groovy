package access

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer

@Configuration
@PropertySource(value = "classpath:secrets.properties", ignoreResourceNotFound = false)
@SpringBootApplication
class Application {

	static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application)
		app.run(args)
	}

	@Bean
	static PropertySourcesPlaceholderConfigurer properties() {
		return new PropertySourcesPlaceholderConfigurer()
	}
}
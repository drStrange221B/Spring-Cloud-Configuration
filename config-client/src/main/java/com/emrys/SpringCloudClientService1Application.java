package com.emrys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RefreshScope
@EnableConfigurationProperties(ConfigClientAppConfiguration.class)
public class SpringCloudClientService1Application {

	@Autowired
	@Qualifier(value = "properties")
	private ConfigClientAppConfiguration properties;

	@Value("${some.property}")
	private String property;

	@Value("${some.other.property}")
	private String someOtherProperty;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudClientService1Application.class, args);
	}

	@RequestMapping("/")
	public String getProperty() {

		StringBuilder response = new StringBuilder();
		response.append(property);
		response.append(" || ");
		response.append(someOtherProperty);
		response.append(" || ");
		response.append(properties.getProperty());

		return response.toString();

	}
}

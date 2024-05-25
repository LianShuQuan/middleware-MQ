//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.prodorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core", "cn.edu.xmu.oomall.prodorder"},
		exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories(basePackages = "cn.edu.xmu.oomall.prodorder.mapper")
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}

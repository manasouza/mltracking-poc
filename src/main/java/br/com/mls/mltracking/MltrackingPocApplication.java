package br.com.mls.mltracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class MltrackingPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(MltrackingPocApplication.class, args);
	}
}

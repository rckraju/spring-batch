package com.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.batch.services.UserService;

@SpringBootApplication
public class SpringBatchApplication {
	
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(JobLauncher jobLauncher, Job job) {
	    return args -> {
	        JobParameters params = new JobParametersBuilder()
	            .addLong("timestamp", System.currentTimeMillis())
	            .toJobParameters();

	        jobLauncher.run(job, params);
	        
	        userService.createDefaultUser();
	    };
	}
	
}

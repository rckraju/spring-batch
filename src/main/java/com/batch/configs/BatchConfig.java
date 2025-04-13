package com.batch.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.entities.Transaction;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final EntityManagerFactory emf;

    @Bean
    public FlatFileItemReader<Transaction> reader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();

        reader.setResource(new ClassPathResource("dataSource.txt"));
        reader.setLinesToSkip(1); // Skip header line

        // Skip empty lines
        reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy() {
            @Override
            public boolean isEndOfRecord(String line) {
                if(line == null || line.trim().isEmpty()) {
                	return false;
                }else {
                	return true;
                }
            }

            @Override
            public String postProcess(String record) {
            	if(record == null || record.trim().isEmpty()) {
                	return null;
                }else {
                	return record;
                }
            }
        });

        // Set LineMapper
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer("|") {{
                setNames("accountNumber", "trxAmount", "description", "trxDate", "trxTime", "customerId");
                setStrict(false);
            }});
            setFieldSetMapper(new TransactionFieldSetMapper());
        }});

        return reader;
    }

    @Bean
    public JpaItemWriter<Transaction> writer() {
        JpaItemWriter<Transaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("import-step", jobRepository)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job importJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("import-job", jobRepository)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}


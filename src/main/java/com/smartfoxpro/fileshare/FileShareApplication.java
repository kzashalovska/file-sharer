package com.smartfoxpro.fileshare;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.Option;

@SpringBootApplication
@Slf4j
public class FileShareApplication implements CommandLineRunner {

    @Autowired
    FileSharerRunner runner;

    public static void main(String[] args) {
        SpringApplication.run(FileShareApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine.populateCommand(runner, args);
    }
}

package com.smartfoxpro.fileshare;

import lombok.Getter;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;

@Component
@Getter
public class FileSharerRunner {
    @Option(names = "--com.demo.uploads.directory", required = true)
    private String path;
}

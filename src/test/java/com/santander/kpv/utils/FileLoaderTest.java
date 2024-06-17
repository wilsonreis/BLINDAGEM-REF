package com.santander.kpv.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FileLoaderTest {

    private FileLoader fileLoader;

    @BeforeEach
    public void setUp() {
        fileLoader = new FileLoader();
    }

    @Test
    void testFileLoader() throws Exception {
        // Simulate the ApplicationArguments
        ApplicationArguments args = new DefaultApplicationArguments();

        // Run the file loader
        fileLoader.run(args);

        // Verify the file content
        ClassPathResource resource = new ClassPathResource("file001.txt");
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Replace this with actual assertions based on the expected content of your file
                assertThat(line).isNotEmpty();
            }
        }
    }
}

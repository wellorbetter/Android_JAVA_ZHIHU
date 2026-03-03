package com.example.zhihu.backend.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CrawlerSeedService {

    private static final Path[] CANDIDATES = new Path[]{
            Path.of("crawler", "data", "programmer_seed.json"),
            Path.of("..", "crawler", "data", "programmer_seed.json")
    };

    public String seedJson() {
        for (Path p : CANDIDATES) {
            try {
                if (Files.exists(p)) {
                    return Files.readString(p, StandardCharsets.UTF_8);
                }
            } catch (IOException ignored) {
            }
        }
        return "{\"fetchedAt\":0,\"count\":0,\"items\":[]}";
    }
}

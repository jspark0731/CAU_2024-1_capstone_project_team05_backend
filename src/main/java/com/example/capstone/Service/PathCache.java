package com.example.capstone.Service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PathCache {
        @CachePut(value = "musescorePaths", key = "#token")
        public String storePath(String token, String path) {
                return path; // Store and return the path in cache
        }

        @Cacheable(value = "musescorePaths", key = "#token")
        public String getPath(String token) {
                return null; // The actual path will be provided by Spring Cache
        }

        @CacheEvict(value = "musescorePaths", key = "#token")
        public void removePath(String token) {
                // Remove the path from cache
        }
}

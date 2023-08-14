package com.example.springboot;

import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@MockitoSettings
public abstract class ServiceTestConfig {
}

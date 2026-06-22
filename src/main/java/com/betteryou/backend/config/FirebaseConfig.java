package com.betteryou.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.Optional;

@Configuration
@Profile("!test")
public class FirebaseConfig {

    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public FirebaseApp firebaseApp(
            @Value("${app.firebase.project-id:${GOOGLE_CLOUD_PROJECT:}}") String projectId
    ) throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        FirebaseOptions.Builder optionsBuilder = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault());

        Optional.ofNullable(projectId)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .ifPresent(optionsBuilder::setProjectId);

        return FirebaseApp.initializeApp(optionsBuilder.build());
    }

    @Bean
    @ConditionalOnMissingBean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }
}

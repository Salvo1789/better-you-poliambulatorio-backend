package com.betteryou.backend.repository;

import com.betteryou.backend.model.Specialist;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class SpecialistRepository {

    private final Firestore firestore;
    private final long timeoutSeconds;

    public SpecialistRepository(
            Firestore firestore,
            @Value("${app.firestore.timeout-seconds:10}") long timeoutSeconds
    ) {
        this.firestore = firestore;
        this.timeoutSeconds = timeoutSeconds;
    }

    public List<Specialist> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("specialists").get();
        List<QueryDocumentSnapshot> documents = future.get(timeoutSeconds, TimeUnit.SECONDS).getDocuments();

        return documents.stream()
                .map(this::toSpecialist)
                .toList();
    }

    private Specialist toSpecialist(QueryDocumentSnapshot doc) {
        Specialist specialist = doc.toObject(Specialist.class);
        specialist.setId(doc.getId());
        return specialist;
    }
}

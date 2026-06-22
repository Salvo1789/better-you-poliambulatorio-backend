package com.betteryou.backend.repository;

import com.betteryou.backend.model.ServiceItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class ServiceRepository {

    private final Firestore firestore;
    private final long timeoutSeconds;

    public ServiceRepository(
            Firestore firestore,
            @Value("${app.firestore.timeout-seconds:10}") long timeoutSeconds
    ) {
        this.firestore = firestore;
        this.timeoutSeconds = timeoutSeconds;
    }

    public List<ServiceItem> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("services").get();
        List<QueryDocumentSnapshot> documents = future.get(timeoutSeconds, TimeUnit.SECONDS).getDocuments();

        return documents.stream()
                .map(this::toServiceItem)
                .toList();
    }

    private ServiceItem toServiceItem(QueryDocumentSnapshot doc) {
        ServiceItem service = doc.toObject(ServiceItem.class);
        service.setId(doc.getId());
        return service;
    }
}

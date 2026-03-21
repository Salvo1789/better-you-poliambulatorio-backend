package com.betteryou.backend.repository;

import com.betteryou.backend.model.ServiceItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceRepository {

    private final Firestore firestore;

    public ServiceRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<ServiceItem> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("services").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<ServiceItem> services = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            ServiceItem service = doc.toObject(ServiceItem.class);
            service.setId(doc.getId());
            services.add(service);
        }

        return services;
    }
}
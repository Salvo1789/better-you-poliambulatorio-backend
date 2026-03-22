package com.betteryou.backend.repository;

import com.betteryou.backend.model.Specialist;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SpecialistRepository {

    private final Firestore firestore;

    public SpecialistRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<Specialist> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("specialists").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Specialist> specialists = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            Specialist specialist = doc.toObject(Specialist.class);
            specialist.setId(doc.getId());
            specialists.add(specialist);
        }

        return specialists;
    }
}
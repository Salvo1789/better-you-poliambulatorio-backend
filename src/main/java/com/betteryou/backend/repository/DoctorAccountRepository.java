package com.betteryou.backend.repository;

import com.betteryou.backend.model.DoctorAccount;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class DoctorAccountRepository {

    private final Firestore firestore;
    private final long timeoutSeconds;

    public DoctorAccountRepository(
            Firestore firestore,
            @Value("${app.firestore.timeout-seconds:10}") long timeoutSeconds
    ) {
        this.firestore = firestore;
        this.timeoutSeconds = timeoutSeconds;
    }

    public DoctorAccount save(DoctorAccount account) throws Exception {
        DocumentReference documentReference = firestore.collection("doctorAccounts").document();
        account.setId(documentReference.getId());

        ApiFuture<WriteResult> future = documentReference.set(account);
        future.get(timeoutSeconds, TimeUnit.SECONDS);

        return account;
    }

    public List<DoctorAccount> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("doctorAccounts")
                .orderBy("createdAt")
                .get();
        return future.get(timeoutSeconds, TimeUnit.SECONDS).getDocuments().stream()
                .map(this::toDoctorAccount)
                .toList();
    }

    public Optional<DoctorAccount> findByEmail(String email) throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("doctorAccounts")
                .whereEqualTo("email", email.toLowerCase())
                .limit(1)
                .get();

        return future.get(timeoutSeconds, TimeUnit.SECONDS).getDocuments().stream()
                .findFirst()
                .map(this::toDoctorAccount);
    }

    private DoctorAccount toDoctorAccount(QueryDocumentSnapshot doc) {
        DoctorAccount account = doc.toObject(DoctorAccount.class);
        account.setId(doc.getId());
        return account;
    }
}

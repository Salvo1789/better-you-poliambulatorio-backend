package com.betteryou.backend.repository;

import com.betteryou.backend.model.Booking;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class BookingRepository {

    private final Firestore firestore;
    private final long timeoutSeconds;

    public BookingRepository(
            Firestore firestore,
            @Value("${app.firestore.timeout-seconds:10}") long timeoutSeconds
    ) {
        this.firestore = firestore;
        this.timeoutSeconds = timeoutSeconds;
    }

    public String save(Booking booking) throws Exception {
        DocumentReference documentReference = firestore.collection("bookings").document();
        booking.setId(documentReference.getId());

        ApiFuture<WriteResult> future = documentReference.set(booking);
        future.get(timeoutSeconds, TimeUnit.SECONDS);

        return documentReference.getId();
    }

    public List<Booking> findAll() throws Exception {
        ApiFuture<QuerySnapshot> future = firestore.collection("bookings")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();
        List<QueryDocumentSnapshot> documents = future.get(timeoutSeconds, TimeUnit.SECONDS).getDocuments();

        return documents.stream()
                .map(this::toBooking)
                .toList();
    }

    public void updateStatus(String bookingId, String status) throws Exception {
        ApiFuture<WriteResult> future = firestore.collection("bookings")
                .document(bookingId)
                .update("status", status, "updatedAt", Instant.now().toString());
        future.get(timeoutSeconds, TimeUnit.SECONDS);
    }

    private Booking toBooking(QueryDocumentSnapshot doc) {
        Booking booking = doc.toObject(Booking.class);
        booking.setId(doc.getId());
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("NEW");
        }
        if (booking.getUpdatedAt() == null || booking.getUpdatedAt().isBlank()) {
            booking.setUpdatedAt(booking.getCreatedAt());
        }
        return booking;
    }
}

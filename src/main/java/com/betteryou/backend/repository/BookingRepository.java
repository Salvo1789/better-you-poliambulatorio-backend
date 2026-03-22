package com.betteryou.backend.repository;

import com.betteryou.backend.model.Booking;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Repository;

@Repository
public class BookingRepository {

    private final Firestore firestore;

    public BookingRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public String save(Booking booking) throws Exception {
        DocumentReference documentReference = firestore.collection("bookings").document();
        booking.setId(documentReference.getId());

        ApiFuture<WriteResult> future = documentReference.set(booking);
        future.get();

        return documentReference.getId();
    }
}
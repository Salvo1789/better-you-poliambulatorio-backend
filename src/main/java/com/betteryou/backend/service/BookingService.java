package com.betteryou.backend.service;

import com.betteryou.backend.dto.BookingRequest;
import com.betteryou.backend.model.Booking;
import com.betteryou.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String createBooking(BookingRequest request) throws Exception {
        Booking booking = new Booking();
        booking.setName(request.getName());
        booking.setEmail(request.getEmail());
        booking.setPhone(request.getPhone());
        booking.setService(request.getService());
        booking.setMessage(request.getMessage());
        booking.setCreatedAt(Instant.now().toString());

        return bookingRepository.save(booking);
    }
}
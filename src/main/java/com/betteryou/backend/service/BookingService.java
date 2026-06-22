package com.betteryou.backend.service;

import com.betteryou.backend.dto.BookingRequest;
import com.betteryou.backend.model.Booking;
import com.betteryou.backend.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String createBooking(BookingRequest request) throws Exception {
        String now = Instant.now().toString();

        Booking booking = new Booking();
        booking.setName(request.getName().trim());
        booking.setEmail(request.getEmail().trim());
        booking.setPhone(request.getPhone().trim());
        booking.setService(request.getService().trim());
        booking.setMessage(request.getMessage() == null ? null : request.getMessage().trim());
        booking.setStatus("NEW");
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() throws Exception {
        return bookingRepository.findAll();
    }

    public void updateBookingStatus(String bookingId, String status) throws Exception {
        bookingRepository.updateStatus(bookingId, status);
    }
}

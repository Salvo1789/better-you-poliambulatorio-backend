package com.betteryou.backend.controller;

import com.betteryou.backend.dto.BookingRequest;
import com.betteryou.backend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> create(@Valid @RequestBody BookingRequest request) throws Exception {
        String bookingId = bookingService.createBooking(request);

        return Map.of(
                "message", "Booking created successfully",
                "id", bookingId
        );
    }
}
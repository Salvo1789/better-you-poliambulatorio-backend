package com.betteryou.backend.controller;

import com.betteryou.backend.dto.BookingStatusUpdateRequest;
import com.betteryou.backend.model.Booking;
import com.betteryou.backend.service.AdminAuthService;
import com.betteryou.backend.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingService bookingService;
    private final AdminAuthService adminAuthService;

    public AdminBookingController(BookingService bookingService, AdminAuthService adminAuthService) {
        this.bookingService = bookingService;
        this.adminAuthService = adminAuthService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Booking> getAll(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        adminAuthService.requireValidToken(authorizationHeader);
        return bookingService.getAllBookings();
    }

    @PatchMapping(
            path = "/{bookingId}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> updateStatus(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable String bookingId,
            @Valid @RequestBody BookingStatusUpdateRequest request
    ) throws Exception {
        adminAuthService.requireValidToken(authorizationHeader);
        bookingService.updateBookingStatus(bookingId, request.getStatus());
        return Map.of("message", "Booking status updated");
    }
}

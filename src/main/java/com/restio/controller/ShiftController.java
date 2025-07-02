package com.restio.controller;

import com.restio.model.Shift;
import com.restio.service.ShiftService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/current")
    public ResponseEntity<Shift> getCurrentShift() {
        Optional<Shift> shift = shiftService.getCurrentActiveShift();
        return shift.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/start")
    public ResponseEntity<Shift> startNewShift() {
        Shift newShift = shiftService.createNewShift();
        return ResponseEntity.status(HttpStatus.CREATED).body(newShift);
    }

    @PostMapping("/close")
    public ResponseEntity<Shift> closeCurrentShift() {
        Optional<Shift> closedShift = shiftService.closeCurrentShift();
        return closedShift.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<Shift> getOrCreateActiveShift() {
        Shift activeShift = shiftService.getOrCreateActiveShift();
        return ResponseEntity.ok(activeShift);
    }
}
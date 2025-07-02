package com.restio.service;

import com.restio.model.Shift;
import com.restio.model.ShiftStatus;
import com.restio.repository.ShiftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ShiftService {
    private final ShiftRepository shiftRepository;

    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    /**
     * Получить активную смену или создать новую
     */
    @Transactional
    public Shift getOrCreateActiveShift() {
        Optional<Shift> activeShift = shiftRepository.findActiveShift();

        if (activeShift.isPresent()) {
            return activeShift.get();
        }

        // Создаем новую смену
        Shift newShift = new Shift();
        return shiftRepository.save(newShift);
    }

    /**
     * Получить следующий номер заказа и обновить счетчик
     */
    @Transactional
    public synchronized Integer getNextOrderNumber() {
        Shift activeShift = getOrCreateActiveShift();

        // Увеличиваем номер заказа
        activeShift.setCurrentOrderNumber(activeShift.getCurrentOrderNumber() + 1);

        // Сохраняем обновленную смену
        shiftRepository.save(activeShift);

        return activeShift.getCurrentOrderNumber();
    }

    /**
     * Закрыть текущую смену
     */
    @Transactional
    public Optional<Shift> closeCurrentShift() {
        Optional<Shift> activeShift = shiftRepository.findActiveShift();

        if (activeShift.isPresent()) {
            Shift shift = activeShift.get();
            shift.setStatus(ShiftStatus.CLOSED);
            shift.setEndDate(LocalDateTime.now());
            return Optional.of(shiftRepository.save(shift));
        }

        return Optional.empty();
    }

    /**
     * Получить текущую активную смену
     */
    public Optional<Shift> getCurrentActiveShift() {
        return shiftRepository.findActiveShift();
    }

    /**
     * Создать новую смену
     */
    @Transactional
    public Shift createNewShift() {
        // Закрываем текущую смену если есть
        closeCurrentShift();

        // Создаем новую смену
        Shift newShift = new Shift();
        return shiftRepository.save(newShift);
    }
}
package mn.icode.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Schedule date must be today or in the future")
    @Column(nullable = false)
    private LocalDate availableDate;

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean available = true;

    public Schedule() {
    }

    public Schedule(Doctor doctor, LocalDate availableDate, LocalTime startTime, LocalTime endTime, boolean available) {
        this.doctor = doctor;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
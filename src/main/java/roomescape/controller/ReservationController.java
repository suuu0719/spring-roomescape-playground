package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.exception.NotFoundReservationException;
import roomescape.Reservation;
import roomescape.dto.ReservationDto;

@Controller
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    // 예약 추가
    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> addReservation(@RequestBody @Valid ReservationDto request) {
        Reservation reservation = new Reservation(counter.incrementAndGet(), request.name, request.date, request.time);
        reservations.add(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/reservations/"+reservation.getId())).body(reservation);
    }

    @GetMapping("/reservation")
    public String reservation(Model model) {
        model.addAttribute("reservations", reservations);
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") long id) {
        Optional<Reservation> reservation = reservations.stream().filter(r -> r.getId() == id).findAny();
        if (reservation.isPresent()) {
            reservations.remove(reservation.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new NotFoundReservationException("Reservation not found");
        }
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<String> handleException(NotFoundReservationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
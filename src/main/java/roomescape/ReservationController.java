package roomescape;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReservationController {

    private List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public ReservationController() {
        // 초기 데이터
        reservations.add(new Reservation(counter.incrementAndGet(), "브라운", "2023-01-01", "10:00"));
        reservations.add(new Reservation(counter.incrementAndGet(), "브라운", "2023-01-02", "11:00"));
    }


    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @PostMapping("/reservations")
    @ResponseBody
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationDto request) {
        Reservation reservation = new Reservation(counter.incrementAndGet(), request.name, request.date, request.time);
        if (reservation.name.isEmpty() || reservation.date.isEmpty()|| reservation.time.isEmpty()
        || request.name == null || request.date == null || request.time == null) {
            throw new NotFoundReservationException("Reservation not found");
        }
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
        boolean removed = reservations.removeIf(reservation -> reservation.getId() == id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            throw new NotFoundReservationException("Reservation not found");
        }
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<String> handleException(NotFoundReservationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}

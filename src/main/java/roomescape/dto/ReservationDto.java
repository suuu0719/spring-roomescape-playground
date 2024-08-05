package roomescape.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReservationDto {

    @NotBlank
    public String name;
    @NotBlank
    public String date;
    @NotBlank
    public String time;

    public ReservationDto(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }
}
package roomescape.dto;

import jakarta.validation.constraints.NotBlank;

public class ReservationCreateRequestDto {

    @NotBlank
    public String name;
    @NotBlank
    public String date;
    @NotBlank
    public String time;

    public ReservationCreateRequestDto(String name, String date, String time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
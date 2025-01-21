package com.example.cafeapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NominatimResponse {
    private String lat;
    private String lon;
    private String display_name;
}

package com.boricori.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GpsSignal {
    Long gameId;
    String host;
    String lat;
    String lng;
}

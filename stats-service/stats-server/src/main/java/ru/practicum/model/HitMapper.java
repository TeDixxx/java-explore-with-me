package ru.practicum.model;

public class HitMapper {

    public static EndpointHitDto toHitDto(EndpointHit endpointHit){
        return EndpointHitDto.builder()
                .id(endpointHit.getId())
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();

    }

    public static EndpointHit fromHitDto(EndpointHitDto endpointHitDto){
        return EndpointHit.builder()
                .id(endpointHitDto.getId())
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }
}

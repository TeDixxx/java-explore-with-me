package ru.practicum.service;


import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStatsDto;


import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {

    EndpointHit save(EndpointHit endpointHit);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}

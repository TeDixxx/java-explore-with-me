package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatsRepository repository;

    @Override
    public EndpointHit save(EndpointHit endpointHit) {
        return repository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return repository.getStatsUnique(start, end);
            } else {
                return repository.getStats(start, end);
            }
        } else {
            if (unique) {
                return repository.getStatsUniqueUri(start, end, uris);
            } else {
                return repository.getStatsUri(start, end, uris);
            }
        }
    }
}

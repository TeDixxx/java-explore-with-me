package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.model.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp >= ?1 " +
            "AND e.timestamp <= ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp >= ?1 " +
            "AND e.timestamp <= ?2 " +
            "AND e.uri IN ?3 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStatsDto> getStatsUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.model.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp >= ?1 " +
            "AND e.timestamp <= ?2 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.model.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp >= ?1 " +
            "AND e.timestamp <= ?2 " +
            "AND e.uri IN ?3 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.ip) DESC")
    List<ViewStatsDto> getStatsUniqueUri(LocalDateTime start, LocalDateTime end, List<String> uris);
}

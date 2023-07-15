package ru.practicum.compilation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.interfaces.AdminCompilationService;
import ru.practicum.compilation.interfaces.CompilationRepository;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.event.interfaces.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository repository;

    private final EventRepository eventRepository;


    @Override
    public CompilationDto create(NewCompilationDto dto) {

        if (dto.getPinned() == null) {
            dto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(dto);
        List<Event> events = new ArrayList<>();

        if (dto.getEvents() != null) {
            events = eventRepository.findAllById(dto.getEvents());
        }

        compilation.setEvents(events);


        return CompilationMapper.toDto(repository.save(compilation));
    }

    @Override
    public CompilationDto update(UpdateCompilationDto dto, Long compId) {
        Compilation compilation = repository.findById(compId).orElseThrow(()
                -> new NotFoundException("Compilation not found"));

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }

        if (dto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(dto.getEvents());
            compilation.setEvents(events);
        }

        return CompilationMapper.toDto(repository.save(compilation));
    }

    @Override
    public void delete(Long compId) {
        repository.findById(compId).orElseThrow(() -> new NotFoundException("Not found"));
        repository.deleteById(compId);
    }
}

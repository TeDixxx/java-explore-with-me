package ru.practicum.compilation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.interfaces.CompilationRepository;
import ru.practicum.compilation.interfaces.PublicCompilationService;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository repository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = repository.findByPinned(pinned,pageable);

        return compilations.stream()
                .map(CompilationMapper ::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = repository.findById(compId).orElseThrow(()
                -> new NotFoundException("Compilation not found"));


        return CompilationMapper.toDto(compilation);
    }
}

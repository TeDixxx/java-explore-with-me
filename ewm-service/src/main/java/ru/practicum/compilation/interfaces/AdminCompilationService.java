package ru.practicum.compilation.interfaces;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;

public interface AdminCompilationService {


    CompilationDto create(NewCompilationDto dto);

    CompilationDto update(UpdateCompilationDto dto, Long compId);

    void delete(Long compId);
}

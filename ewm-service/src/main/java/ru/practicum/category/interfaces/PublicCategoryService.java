package ru.practicum.category.interfaces;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long id);
}

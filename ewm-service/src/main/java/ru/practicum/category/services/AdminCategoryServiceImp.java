package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.interfaces.AdminCategoryService;
import ru.practicum.category.interfaces.CategoryRepository;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.exceptions.NotFoundException;


@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImp implements AdminCategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public CategoryDto update(CategoryDto dto, Long id) {
        repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        dto.setId(id);

        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.fromDto(dto)));

    }

    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        repository.deleteById(id);

    }
}

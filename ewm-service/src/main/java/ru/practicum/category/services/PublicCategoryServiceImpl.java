package ru.practicum.category.services;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.interfaces.CategoryRepository;
import ru.practicum.category.interfaces.PublicCategoryService;
import ru.practicum.category.model.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository repository;

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from, size))
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return CategoryMapper.toCategoryDto(repository.findById(id).orElseThrow(()
                -> new NotFoundException("Category not found")));
    }
}

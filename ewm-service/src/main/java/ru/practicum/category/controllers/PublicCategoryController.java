package ru.practicum.category.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.interfaces.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final PublicCategoryService service;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                    @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto findById(@PathVariable Long catId) {
        return service.getById(catId);
    }
}

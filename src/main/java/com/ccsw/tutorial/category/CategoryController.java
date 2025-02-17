package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Category", description = "API of Category")
@RequestMapping(value = "/category")
@RestController
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    ModelMapper mapper;

    /**
     * Metodo para recuperar todas las categorias
     *
     * @return {@link List} de {@link CategoryDto}
     */

    @Operation(summary = "Find", description = "Method that return a list of Categories")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CategoryDto> findAll() {
        List<Category> categories = this.categoryService.findAll();
        return categories.stream().map(e -> mapper.map(e, CategoryDto.class)).collect(Collectors.toList());
    }

    /**
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */

    @Operation(summary = "Save or Update", description = "Method that saves or updates a Category")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody CategoryDto dto) {
        this.categoryService.save(id, dto);
    }

    /**
     * Metodo para borrar una categoria
     *
     * @param id Pk de la entidad
     */

    @Operation(summary = "Delete", description = "Method that deletes a category")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {
        this.categoryService.delete(id);
    }
}
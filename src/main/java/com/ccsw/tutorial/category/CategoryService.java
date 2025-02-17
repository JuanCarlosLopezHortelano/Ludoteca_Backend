package com.ccsw.tutorial.category;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;

import java.util.List;

public interface CategoryService {
    /**
     * Metodo para recuperar todas las {@link Category}
     * @return {@link List} de {@link Category}
     */

    List<Category> findAll();

    /**
     * Metodo para crear o actualizar una {@link Category}
     * @param id PK de la entidad
     * @param dto datos de la entidad
     *
     */
    void save(Long id, CategoryDto dto);

    /**
     * Metodo para borrar una {@link Category}
     * @param id PK de la entidad
     */

    void delete(Long id) throws Exception;
}

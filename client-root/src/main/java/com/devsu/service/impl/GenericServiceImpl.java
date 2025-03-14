package com.devsu.service.impl;

import com.devsu.exception.ModelNotFoundException;
import com.devsu.repository.IGenericRepository;
import com.devsu.service.IGenericService;

import java.util.List;

public abstract class GenericServiceImpl<T, I> implements IGenericService<T, I> {

    protected abstract IGenericRepository<T, I> getRepository();

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T findById(I id) {
        return getRepository().findById(id).orElseThrow(() -> new ModelNotFoundException("ID not found: " + id));
    }
}

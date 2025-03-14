package com.devsu.service;

import java.util.List;

public interface IGenericService <T, I> {
    List<T> findAll();
    T findById(I id);
}

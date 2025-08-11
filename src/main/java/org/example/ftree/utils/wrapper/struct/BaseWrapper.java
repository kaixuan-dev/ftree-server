package org.example.ftree.utils.wrapper.struct;


import lombok.Getter;

@Getter
public class BaseWrapper <E> {
    protected final Class<E> entityClass;

    protected SimpleQueryMapper mapper;
    protected final String tableName;
    protected final Example<E> example;
    protected final Boolean annotationMapping;

    public BaseWrapper(Class<E> entityClass, String tableName, Boolean annotationMapping) {
        this.entityClass = entityClass;
        this.tableName = tableName;
        this.example = new Example<>(entityClass, annotationMapping);
        this.annotationMapping = annotationMapping;
    }
}

package org.example.ftree.utils.wrapper;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ftree.utils.wrapper.meta.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * @Author kx
 * @Date 2024/1/26 15:39
 */
@Setter
@Getter
public class Example<E> {
    protected List<Criteria<E>> oredCriteria;
    private final Class<E> entityClass;
    protected final Boolean annotationMapping;

    public Example(Class<E> entityClass, Boolean annotationMapping) {
        this.oredCriteria = new ArrayList<>();
        this.entityClass = entityClass;
        this.annotationMapping = annotationMapping;
    }

    public Criteria<E> createCriteria() {
        Criteria<E> criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria<E> createCriteriaInternal() {
        return new Criteria<>(this.entityClass, this.annotationMapping);
    }

    public void or(Criteria<E> criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria<E> or() {
        Criteria<E> criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Criteria<E> {
        protected List<Criterion> criteria;
        protected Class<E> entityClass;

        protected Boolean annotationMapping;


        Criteria(Class<E> entityClass, Boolean annotationMapping) {
            this.entityClass = entityClass;
            this.criteria = new CopyOnWriteArrayList<>();
            this.annotationMapping = annotationMapping;
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected String getColumnName(Entity.ConditionFunction<E, ?> fn) {
            return Reflects.fnMapping(this.annotationMapping, this.entityClass, fn).getColumnName();
        }

        private Criteria<E> and0(Entity.ConditionFunction<E, ?> fn, Entity.DB_OPERATE dbOperate) {
            String columnName = getColumnName(fn);
            addCriterion(String.format("%s %s", columnName, dbOperate.value));
            return this;
        }

        private Criteria<E> and1(Entity.ConditionFunction<E, ?> fn, Entity.DB_OPERATE dbOperate, Object value) {
            String columnName = getColumnName(fn);
            addCriterion(String.format("%s %s", columnName, dbOperate.value), value, columnName);
            return this;
        }

        private Criteria<E> and2(Entity.ConditionFunction<E, ?> fn, Entity.DB_OPERATE dbOperate, Object value1, Object value2) {
            String columnName = getColumnName(fn);
            addCriterion(String.format("%s %s", columnName, dbOperate.value), value1, value2, columnName);
            return this;
        }

        public Criteria<E> addCriterion(Class<E> clazz, String condition) {
            criteria.add(new Criterion(condition));
            return this;
        }

        public Criteria<E> andIsNull(Entity.ConditionFunction<E, ?> fn) {
            return this.and0(fn, Entity.DB_OPERATE.IS_NULL);
        }

        public Criteria<E> andIsNotNull(Entity.ConditionFunction<E, ?> fn) {
            return this.and0(fn, Entity.DB_OPERATE.IS_NOT_NULL);
        }

        public Criteria<E> andEqualTo(Entity.ConditionFunction<E, ?> fn, Object value) {
            return this.and1(fn, Entity.DB_OPERATE.EQUAL, value);
        }

        public Criteria<E> andEqualTo(Entity.ConditionFunction<E, ?> fn, Object value, Predicate<Object> valuePredicate) {
            if (valuePredicate.test(value)) {
                return this.andEqualTo(fn, value);
            }
            return this;
        }

        public Criteria<E> andNotEqualTo(Entity.ConditionFunction<E, ?> fn, Object value) {
            return this.and1(fn, Entity.DB_OPERATE.NOT_EQUAL, value);
        }

        public Criteria<E> andLike(Entity.ConditionFunction<E, ?> fn, Object value, Predicate<Object> valuePredicate) {
            if (valuePredicate.test(value)) {
                return andLike(fn, value);
            }
            return this;
        }

        public Criteria<E> andLike(Entity.ConditionFunction<E, ?> fn, Object value) {
            return this.and1(fn, Entity.DB_OPERATE.LIKE, value);
        }

        public Criteria<E> andNotLike(Entity.ConditionFunction<E, ?> fn, Object value) {
            return this.and1(fn, Entity.DB_OPERATE.NOT_LIKE, value);
        }

        public <T> Criteria<E> andIn(Entity.ConditionFunction<E, ?> fn, List<T> values) {
            return this.and1(fn, Entity.DB_OPERATE.IN, values);
        }

        public <T> Criteria<E> andNotIn(Entity.ConditionFunction<E, ?> fn, List<T> values) {
            return this.and1(fn, Entity.DB_OPERATE.NOT_IN, values);
        }

        public Criteria<E> andBetween(Entity.ConditionFunction<E, ?> fn, Object value1, Object value2) {
            return this.and2(fn, Entity.DB_OPERATE.BETWEEN, value1, value2);
        }

        public Criteria<E> andNotBetween(Entity.ConditionFunction<E, ?> fn, Object value1, Object value2) {
            return this.and2(fn, Entity.DB_OPERATE.NOT_BETWEEN, value1, value2);
        }
    }


    @Setter
    @Getter
    @NoArgsConstructor
    public static class Criterion {
        private String condition;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }

}

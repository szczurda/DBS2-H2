package com.projekt.dbs.tools;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DoubleToIntConverter implements Converter<Double, Integer> {

    @Override
    public Result<Integer> convertToModel(Double value, ValueContext context) {
        if (value == null) {
            return Result.ok(null);
        } else {
            return Result.ok(value.intValue());
        }
    }

    @Override
    public Double convertToPresentation(Integer value, ValueContext context) {
        if (value == null) {
            return null;
        } else {
            return Double.valueOf(value);
        }
    }
}

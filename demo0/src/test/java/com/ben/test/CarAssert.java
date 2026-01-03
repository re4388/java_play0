package com.ben.test;

import com.ben.Car;
import org.assertj.core.api.AbstractAssert;

public class CarAssert extends AbstractAssert<CarAssert, Car> {
    public CarAssert(Car actual) {
        super(actual, CarAssert.class);
    }
    public CarAssert hasMake(String make) {

        isNotNull();

        if (!actual.getMake().equals(make)) failWithMessage(
                "Expected car make to be <%s> but was <%s>",
                make, actual.getMake()
        );

        return this;
    }
}

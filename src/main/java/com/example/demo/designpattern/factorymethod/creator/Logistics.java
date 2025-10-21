package com.example.demo.designpattern.factorymethod.creator;

import com.example.demo.designpattern.factorymethod.product.Transport;

public abstract class Logistics {
    abstract Transport createTransport();

    public double getPrice() {
        return createTransport().getPrice();
    }
}

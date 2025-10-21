package com.example.demo.designpattern.factorymethod.product;

import org.springframework.stereotype.Component;

@TransportQualifier(TransportType.SHIP)
@Component
public class Ship implements Transport {
    @Override
    public double getPrice() {
        return 20;
    }
}

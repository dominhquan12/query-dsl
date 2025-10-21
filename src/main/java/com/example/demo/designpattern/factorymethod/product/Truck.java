package com.example.demo.designpattern.factorymethod.product;

import org.springframework.stereotype.Component;

@TransportQualifier(TransportType.TRUCK)
@Component
public class Truck implements Transport {
    @Override
    public double getPrice() {
        return 10;
    }
}

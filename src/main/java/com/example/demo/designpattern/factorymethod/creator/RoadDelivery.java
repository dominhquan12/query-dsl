package com.example.demo.designpattern.factorymethod.creator;

import com.example.demo.designpattern.factorymethod.product.Transport;
import com.example.demo.designpattern.factorymethod.product.TransportQualifier;
import com.example.demo.designpattern.factorymethod.product.TransportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@LogisticQualifier(LogisticType.ROAD)
public class RoadDelivery extends Logistics {

    private final Transport transport;

    @Autowired
    public RoadDelivery(@TransportQualifier(TransportType.TRUCK) Transport transport) {
        this.transport = transport;
    }

    @Override
    Transport createTransport() {
        return transport;
    }
}

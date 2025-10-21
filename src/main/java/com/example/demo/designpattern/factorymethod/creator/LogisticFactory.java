package com.example.demo.designpattern.factorymethod.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class LogisticFactory {

    private final Map<LogisticType, Logistics> creatorMap = new EnumMap<>(LogisticType.class);

    @Autowired
    public LogisticFactory(List<Logistics> logisticsList) {
        for (Logistics logistics : logisticsList) {
            LogisticQualifier annotation = logistics.getClass().getAnnotation(LogisticQualifier.class);
            if (annotation != null) {
                creatorMap.put(annotation.value(), logistics);
            }
        }
    }

    public Logistics getLogistics(LogisticType type) {
        Logistics creator = creatorMap.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("No factory found for type: " + type);
        }
        return creator;
    }
}


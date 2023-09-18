package com.task.three.one;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

// Класс Датчик температуры
class TemperatureSensor {
    private final PublishSubject<Integer> temperatureSubject = PublishSubject.create();

    public Observable<Integer> getTemperatureStream() {
        return temperatureSubject;
    }

    // Эмуляция чтения данных с датчика и публикации в потоке
    public void readTemperature() {
        int temperature = (int) (Math.random() * 16) + 15; // Генерация случайной температуры
        temperatureSubject.onNext(temperature);
    }
}

// Класс Датчик CO2
class Co2Sensor {
    private final PublishSubject<Integer> co2Subject = PublishSubject.create();

    public Observable<Integer> getCo2Stream() {
        return co2Subject;
    }

    // Эмуляция чтения данных с датчика и публикации в потоке
    public void readCo2Level() {
        int co2Level = (int) (Math.random() * 71) + 30; // Генерация случайного уровня CO2
        co2Subject.onNext(co2Level);
    }
}

// Класс Сигнализация
class Alarm {
    public Alarm(Observable<Integer> temperatureStream, Observable<Integer> co2Stream) {
        Observable.combineLatest(temperatureStream, co2Stream,
                (temperature, co2) -> new SensorData(temperature, co2))
                .subscribe(this::handleData);
    }

    private void handleData(SensorData sensorData) {
        int temperature = sensorData.getTemperature();
        int co2Level = sensorData.getCo2Level();

        if (temperature > 25 || co2Level > 70) {
            System.out.println("Warning: Temperature is " + temperature + " and CO2 level is " + co2Level);
            if (temperature > 25 && co2Level > 70) {
                System.out.println("ALARM!!!");
            }
        }
    }
}

// Класс для хранения данных от датчиков
class SensorData {
    private final int temperature;
    private final int co2Level;

    public SensorData(int temperature, int co2Level) {
        this.temperature = temperature;
        this.co2Level = co2Level;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getCo2Level() {
        return co2Level;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        Co2Sensor co2Sensor = new Co2Sensor();

        Alarm alarm = new Alarm(temperatureSensor.getTemperatureStream(), co2Sensor.getCo2Stream());

        // Генерация данных от датчиков каждую секунду
        while (true) {
            temperatureSensor.readTemperature();
            co2Sensor.readCo2Level();
            Thread.sleep(1000);
        }
    }
}
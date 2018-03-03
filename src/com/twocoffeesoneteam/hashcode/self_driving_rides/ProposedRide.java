package com.twocoffeesoneteam.hashcode.self_driving_rides;

public class ProposedRide {
    Car car;
    Ride ride;
    int timeFinish;

    public ProposedRide(Car car, Ride ride, int timeFinish) {
        this.car = car;
        this.ride = ride;
        this.timeFinish = timeFinish;
    }
}

package com.twocoffeesoneteam.hashcode.self_driving_rides;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private int row;
    private int coloumn;
    private int veichles;
    private int ridesNumber;
    private int bonus;
    private int steps;

    private ArrayList<Ride> rides = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();


    public static void main(String[] args) {

        new Main().run();
    }

    private void run(){
        try {

            // Parse first line from input file
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/paolorotolo/Downloads/b_should_be_easy.in"));
            String firstLine = bufferedReader.readLine();

            Scanner scanner = new Scanner(firstLine);

            row = scanner.nextInt();
            coloumn = scanner.nextInt();
            veichles = scanner.nextInt();
            ridesNumber = scanner.nextInt();
            bonus = scanner.nextInt();
            steps = scanner.nextInt();


            // Parse all rides
            for (int i=0; i<ridesNumber; i++){
                Ride ride = new Ride();

                String rideString = bufferedReader.readLine();
                scanner = new Scanner(rideString);

                int a = scanner.nextInt();
                int b = scanner.nextInt();
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int s = scanner.nextInt();
                int f = scanner.nextInt();

                ride.uid = i;

                ride.rowStart = a;
                ride.coloumnStart = b;
                ride.rowFinish = x;
                ride.coloumnFinish = y;
                ride.earliestStart = s;
                ride.latestFinish = f;

                rides.add(ride);
            }

            rides.sort(new earliestStartComparator());


            // Create cars
            for (int i=0;i<veichles; i++ ){
                cars.add(new Car());
            }

            // Run simulation
            for (int i=0;i<steps; i++ ){

                for (int h=0; h<veichles; h++) {
                    Car car = cars.get(h);

                    if (car.rideCounter > 0) {
                        car.rideCounter--;
                    } else {
                        car.isRiding = false;
                    }
                }

                for (Ride ride : rides) {
                    if (!ride.isDone) {
                        for (int z = 0; z < veichles; z++) {
                            Car car = cars.get(z);
                            if (!car.isRiding) {
                                if (!ride.isDone) {
                                    int stepsToRide = getFinalSteps(car, ride.rowStart, ride.coloumnStart, ride.rowFinish, ride.coloumnFinish,
                                            i, ride.earliestStart);

                                    if ((i + stepsToRide) < ride.latestFinish) {
                                        car.isRiding = true;
                                        car.rideCounter = stepsToRide;
                                        ride.isDone = true;
                                        car.ridesDone.add(ride.uid);

                                        car.currentX = ride.rowFinish;
                                        car.currentY = ride.coloumnFinish;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/home/paolorotolo/Downloads/output.txt"));
            for (int i = 0; i<veichles; i++) {
                Car car = cars.get(i);
                writer.write(car.ridesDone.size() + " " + printRides(car.ridesDone));
                writer.write("\n");
            }
            writer.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String printRides(ArrayList<Integer> rides){
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer ride : rides) {
            stringBuilder.append(ride).append(" ");
        }

        return stringBuilder.toString();
    }

    private int getFinalSteps(Car car, int startX, int startY, int finishX, int finishY, int startTime, int earlyTime){
        int startSteps = getSteps(car.currentX, car.currentY, startX, startY);
        int rideSteps = getSteps(startX, startY, finishX, finishY);

        int totalTile = startSteps + rideSteps;

        if (earlyTime > startTime) {
            return totalTile + earlyTime - startTime;
        } else {
            return  totalTile;
        }
    }


    private int getSteps(int x, int y, int a, int b){
        return Math.abs(a-x)+Math.abs(b-y);
    }



}


class earliestStartComparator implements Comparator<Ride>{

    @Override
    public int compare(Ride r1, Ride r2) {
        return Integer.compare(r2.earliestStart,r1.earliestStart);
    }
}

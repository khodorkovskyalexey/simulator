import java.util.ArrayList;

public class Road {
    private int pointA;
    private int pointB;
    public double length;
    private RoadType type;
    private ArrayList<Car> list = new ArrayList<>();

    public Road(int pointA, int pointB, RoadType type, double length) {
        this.type = type;
        this.pointA = pointA;
        this.pointB = pointB;
        this.length = length;
    }

    public int getPointA() {
        return pointA;
    }

    public int getPointB() {
        return pointB;
    }

    public ArrayList<Car> getList() {
        return list;
    }

    public ArrayList<Double> getKm() {
        ArrayList<Double> km = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            km.add(list.get(i).getKm());
        }
        return km;
    }

    public void addCar(int index, Car car) {
        list.add(index, car);
    }

    public RoadType getType() {
        return type;
    }
}

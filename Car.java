import java.util.ArrayList;

public class Car {
    private int now;
    private double km;
    private int index;
    private ArrayList<Integer> way;
    //скорость машины
    //условные единицы расстояния/условные единицы времени
    public final int speed = 100;

    public Car(RoadSystem rs, int index) {
        this.index = index;
        double rand = Math.random();
        rand *= rs.getSpawn().size();
        int start;
        start = (int) Math.floor(rand);
        start += rs.tlNUM;
        int finish = start;
        while(start == finish) {
            rand = Math.random();
            rand *= (rs.getSpawn().size());
            finish = (int) Math.floor(rand);
            finish += rs.tlNUM;
        }
        km = 0;
        way = new Solution(rs.getSpawns().length, rs.getSpawn().get(start - rs.tlNUM).getNexttl(), rs.getSpawn().get(finish - rs.tlNUM).getNexttl(), rs.getAdj(), rs.getWeight()).getWay();
        way.add(0, start);
        way.add(finish);
        now = way.get(0);
    }

    public Car(RoadSystem rs, int index, int start) {
        this.index = index;
        int finish = start;
        //finish = rs.getRoad(start).get(0).getPointB();
        while(start == finish) {
            double rand = Math.random();
            rand *= (rs.getSpawn().size());
            finish = (int) Math.floor(rand);
            finish += rs.tlNUM;
        }
        km = 0;
        //way = new ArrayList<>();
        way = new Solution(rs.getSpawns().length, rs.getSpawn().get(start - rs.tlNUM).getNexttl(), rs.getSpawn().get(finish - rs.tlNUM).getNexttl(), rs.getAdj(), rs.getWeight()).getWay();
        way.add(0, start);
        way.add(finish);
        now = way.get(0);
    }

    public void reset() {
        now = way.get(0);
        km = 0;
    }

    public void next() {
        now = getNext();
        km = 0;
    }

    public ArrayList<Integer> getWay() {
        return way;
    }

    public double getKm() {
        return km;
    }

    public void addKm(double value) {
        km += value;
    }

    public int getNext() {
        return way.get(way.indexOf(now) + 1);
    }

    public int getFinish() {
        return way.get(way.size() - 1);
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getNow() {
        return now;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public int getIndex() {
        return index;
    }

    public int getStart() {
        return way.get(0);
    }
}

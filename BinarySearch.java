import java.util.ArrayList;

public class BinarySearch {
    public static int search(Car car, ArrayList<Road> road) {
        if(road.size() == 1) {
            return 0;
        }
        ArrayList<ArrayList<Double>> roads = new ArrayList<>();
        for (int i = 0; i < road.size(); i++) {
            if(road.get(i).getKm().size() > 0) {
                ArrayList<Double> arrlist = new ArrayList<>();
                for (int j = 0; j < road.get(i).getKm().size(); j++) {
                    arrlist.add(road.get(i).getKm().get(j));
                }
                roads.add(arrlist);
            } else {
                return i;
            }
        }
        int index = 0;
        int top_possition = -1;
        for (int i = 0; i < roads.size(); i++) {
            int a = one_step(car.getKm(), roads.get(i));
            if(a >= 1) {
                if(roads.get(i).get(a) - car.getKm() > top_possition) {
                    index = i;
                    top_possition = a;
                }
            } else {
                return i;
            }
        }
        return index;
    }

    private static int one_step(double pos, ArrayList<Double> list) {  // 10 5 1
        if(list.size() == 0) {
            return 0;
        }
        if(pos > list.get(0)) {
            return 0;
        }
        if(pos < list.get(list.size() - 1)) {
            return list.size() - 1;
        }
        int a = 0;
        int b = list.size() - 1;
        if (pos == list.get(a)) {
            int index = a;
            for (int i = a; i < list.size(); i++) {
                if(pos == list.get(i)) {
                    index = i;
                }
            }
            return index;
        }
        if (pos == list.get(b)) {
            return b;
        }
        int res = (b-a) / 2 + a;
        while(Math.abs(b - a) > 1) {
            if(pos > list.get(res)) {
                b = res;
                res = (b - a) / 2 + a;
            } else {
                if(pos < list.get(res)) {
                    a = res;
                    res = (b - a) / 2 + a;
                } else {
                    if (pos == list.get(res)) {
                        return res + 1;
                    }
                }
            }
        }
        return a;
    }
}

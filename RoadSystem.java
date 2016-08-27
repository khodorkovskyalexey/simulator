import java.io.IOException;
import java.util.ArrayList;

public class RoadSystem {

    public final int length_to_spawn = 100;
    //колво светофоров
    public int tlNUM;
    // список всех точек (светофоры и спавны)
    private ArrayList<PointType> pt = new ArrayList<>();
    // список спавнов ОНЛИ
    // нулевой элемент в этом списке = tlNum элементу в списке выше (pt) => 1 элемент = tlNum + 1
    private ArrayList<Spawn> spawn = new ArrayList<>();
    // список светофоров ОНЛИ 0 и 1 элемент = 0 и 1 в списке всех точек
    private ArrayList<Trafficlight> tl = new ArrayList<>();
    // список дорог тоже ОНЛИ
    private ArrayList<Road> roads = new ArrayList<>();

    private ArrayList<Integer>[] spawns;

    private ArrayList<Integer>[] adj;
    private ArrayList<Integer>[] weight;

    public RoadSystem(ArrayList<Integer>[] adj, ArrayList<Integer>[] weight, ArrayList<Integer>[] spawns, ArrayList<Integer[]> road_type) throws IOException {
        this.adj = adj;
        this.weight = weight;
        this.spawns = spawns;
        for (int i = 0; i < adj.length; i++) {
            for (int j = 0; j < adj[i].size(); j++) {
                RoadType rt = RoadType.Horizont;
                if (getRoadType(road_type, i, adj[i].get(j)).equals(1)) {
                    rt = RoadType.Vertical;
                }
                roads.add(new Road(i, adj[i].get(j), rt, weight[i].get(1)));
            }
        }
        tlNUM = spawns.length;

        ArrayList<String> arr = new TaskFileIO("tl_times.txt").read();

        for (int i = 0; i < tlNUM; i++) {
            String s = arr.get(i);
            if(s.indexOf("//") == 0) {
                arr.remove(i);
                return;
                //либо берем строку до комментария
            } else {
                String[] s1 = s.split("//");
                s = s1[0];
            }
            String[] s1 = s.split(" ");
            tl.add(new Trafficlight(Integer.parseInt(s1[0]), Integer.parseInt(s1[1])));
            pt.add(PointType.Trafficlight);
        }
        for (int i = 0; i < tlNUM; i++) {
            switch (spawns[i].get(0)) {
                case 0: spawn.add(new Spawn(RoadType.Horizont, i));
                    pt.add(PointType.Spawn);
                    roads.add(new Road(pt.size() - 1, i, RoadType.Horizont, length_to_spawn));
                    roads.add(new Road(i, pt.size() - 1, RoadType.Horizont, length_to_spawn));
                break;
                case 1: spawn.add(new Spawn(RoadType.Vertical, i));
                    pt.add(PointType.Spawn);
                    roads.add(new Road(pt.size() - 1, i, RoadType.Vertical, length_to_spawn));
                    roads.add(new Road(i, pt.size() - 1, RoadType.Vertical, length_to_spawn));
                break;
                case 2: spawn.add(new Spawn(RoadType.Horizont, i));
                    pt.add(PointType.Spawn);
                    roads.add(new Road(pt.size() - 1, i, RoadType.Horizont, length_to_spawn));
                    roads.add(new Road(i, pt.size() - 1, RoadType.Horizont, length_to_spawn));
                    spawn.add(new Spawn(RoadType.Vertical, i));
                    pt.add(PointType.Spawn);
                    roads.add(new Road(pt.size() - 1, i, RoadType.Vertical, length_to_spawn));
                    roads.add(new Road(i, pt.size() - 1, RoadType.Vertical, length_to_spawn));
                break;
            }
        }
    }

    public void set_car(Car car, int pos, int road_index) {
        roads.get(road_index).addCar(pos, car);
    }

    public ArrayList<PointType> getPt() {
        return pt;
    }

    public ArrayList<Spawn> getSpawn() {
        return spawn;
    }

    public ArrayList<Trafficlight> getTl() {
        return tl;
    }

    public ArrayList<Road> getRoad(int pointA, int pointB) {
        ArrayList<Road> trueroad = new ArrayList<>();
        for (Road road : roads) {
            if(road.getPointA() == pointA) {
                if(road.getPointB() == pointB) {
                    trueroad.add(road);
                }
            }
        }
        return trueroad;
    }

    public ArrayList<Road> getRoad(int pointA) {
        ArrayList<Road> list = new ArrayList<>();
        for (Road road : roads) {
            if(road.getPointA() == pointA) {
                list.add(road);
            }
        }
        return list;
    }

    public ArrayList<Integer>[] getSpawns() {
        return spawns;
    }

    public void newTl(int index, int firstTime, int secondTime) {
        tl.set(index, new Trafficlight(firstTime, secondTime));
    }

    public ArrayList<Integer>[] getAdj() {
        return adj;
    }

    public ArrayList<Integer>[] getWeight() {
        return weight;
    }

    private Integer getRoadType(ArrayList<Integer[]> road_type, int x, int y) {
        for (Integer[] rt : road_type) {
            if((rt[0].equals(x))&&(rt[1].equals(y))) {
                return rt[2];
            }
        }
        return road_type.get(0)[2];
    }

    public int get_spawns_size() {
        return spawn.size();
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }
}

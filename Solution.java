import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Solution { // НЕ ТРОГАТЬ!!!

    private static int INF = Integer.MAX_VALUE / 2;

    private int n; //количество вершин в орграфе
    private ArrayList<Integer> adj[]; //список смежности
    private ArrayList<Integer> weight[]; //вес ребра в орграфе (длинна дороги)
    private boolean used[]; //массив для хранения информации о пройденных и не пройденных вершинах
    private int dist[]; //массив для хранения расстояния от стартовой вершины
    //массив предков, необходимых для восстановления кратчайшего пути из стартовой вершины
    private int pred[];
    ArrayList<Integer> way = new ArrayList<>();
    int start; //стартовая вершина, от которой ищется расстояние до всех других
    int end; //конечная вершина

    public Solution(int n, int start, int end, ArrayList<Integer> adj[], ArrayList<Integer> weight[]) {
        this.n = n;
        this.start = start;
        this.end = end;
        this.adj = adj;
        this.weight = weight;
        used = new boolean[n];
        Arrays.fill(used, false);
        pred = new int[n];
        Arrays.fill(pred, -1);
        dist = new int[n];
        Arrays.fill(dist, INF);
        dejkstra(start);
    }

    //процедура запуска алгоритма Дейкстры из стартовой вершины
    private void dejkstra(int s) {
        dist[s] = 0; //кратчайшее расстояние до стартовой вершины равно 0
        for (int iter = 0; iter < n; ++iter) {
            int v = -1;
            int distV = INF;
            //выбираем вершину, кратчайшее расстояние до которого еще не найдено
            for (int i = 0; i < n; ++i) {
                if (used[i]) {
                    continue;
                }
                if (distV < dist[i]) {
                    continue;
                }
                v = i;
                distV = dist[i];
            }
            //рассматриваем все дуги, исходящие из найденной вершины
            for (int i = 0; i < adj[v].size(); ++i) {
                int u = adj[v].get(i);
                int weightU = weight[v].get(i);
                //релаксация вершины
                if (dist[v] + weightU < dist[u]) {
                    dist[u] = dist[v] + weightU;
                    pred[u] = v;
                }
            }
            //помечаем вершину v просмотренной, до нее найдено кратчайшее расстояние
            used[v] = true;
        }
    }

    //процедура восстановления кратчайшего пути по массиву предком
    void way(int v) {
        if (v == -1) {
            return;
        }
        way(pred[v]);
        way.add(v);
    }

    public ArrayList<Integer> getWay() {
        way(end);
        return way;
    }
}
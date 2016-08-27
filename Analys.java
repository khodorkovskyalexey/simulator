import java.io.IOException;
import java.util.ArrayList;

public class Analys {
    //список смежностей
    private ArrayList<Integer[]> adj;
    //длинна дорог
    private ArrayList<Integer[]> weight;
    // 0 - horizont; 1 - vertical (расположение дороги(горизонтально или вертикально))
    private ArrayList<Integer[]> road_type;
    //считанные данные
    private ArrayList<String> list;
    //количество спавнов привязанных к точке
    // 0 - горизонтальный, 1 - вертикальный, 2 - оба
    private ArrayList<Integer[]> spawn = new ArrayList<>();
    //путь к файлу с данными
    private final String filepath = "struct.txt";

    public Analys() throws IOException {
        list = new TaskFileIO(filepath).read();
        adj = new ArrayList<>();
        weight = new ArrayList<>();
        road_type = new ArrayList<>();
        while (!list.isEmpty()) {
            oneString(list.get(0));
        }
    }
    //метод обработки одной строки
    private void oneString(String s) {
        //разбиваем строку
        if(s.indexOf("//") == 0) {
            list.remove(list.indexOf(s));
            return;
        //либо берем строку до комментария
        } else {
            String[] s1 = s.split("//");
            s = s1[0];
        }
        String[] s1 = s.split(" ");
        //если не введины все данные удаляем строку
        if(s1.length < 4) {
            list.remove(list.indexOf(s));
            return;
        }
        //разбитие инфы по массивам
        int point_index = Integer.parseInt(s1[0]);
        Integer[] arr = new Integer[2];
        //список смежностей
        arr[0] = point_index;
        arr[1] = Integer.parseInt(s1[1]);
        adj.add(arr);
        //длинна дороги
        arr = new Integer[3];
        arr[0] = point_index;
        arr[1] = Integer.parseInt(s1[1]);
        arr[2] = Integer.parseInt(s1[2]);
        weight.add(arr);
        //тип дороги
        arr = new Integer[3];
        arr[0] = point_index;
        arr[1] = Integer.parseInt(s1[1]);
        arr[2] = Integer.parseInt(s1[3]);
        road_type.add(arr);
        //количество спавнов
        if(s1.length > 4) {
            arr = new Integer[2];
            arr[0] = point_index;
            arr[1] = Integer.parseInt(s1[4]);
            spawn.add(arr);
        } else {
            boolean bool = false;
            for (Integer[] i : spawn) {
                if(i[0] == point_index) {
                    bool = false;
                }
            }
            if(bool) {
                arr = new Integer[2];
                arr[0] = point_index;
                arr[1] = 2;
                spawn.add(arr);
            }
        }
        //удаление строки
        list.remove(list.indexOf(s));
    }
    /*метод конвертирующий данные в нужный для класса Solution вид.
        работает коряво, но работает
        index - элемент, индекс элемента, который нужно вставить
        (из файла struct)
    */
    private ArrayList<Integer>[] optimization(ArrayList<Integer[]> arr, int index) {
        //создание нового массива
        ArrayList<Integer>[] optim_arr = new ArrayList[arr.get(arr.size() - 1)[0] + 1];
        for (int i = 0; i < optim_arr.length; i++) {
            optim_arr[i] = new ArrayList<>();
        }
        //конвертирование данных
        for (int i = 0; i < arr.size(); i++) {
            optim_arr[arr.get(i)[0]].add(arr.get(i)[index]);
        }
        return optim_arr;
    }

    public ArrayList<Integer>[] getSpawn() {
        return optimization(spawn, 1);
    }

    public ArrayList<Integer>[] getAdj() {
        return optimization(adj, 1);
    }

    public ArrayList<Integer>[] getWeight() {
        return optimization(weight, 2);
    }

    public ArrayList<Integer[]> getRoad_type() {
        return road_type;
    }
}

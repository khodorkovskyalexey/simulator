import java.io.IOException;
import java.util.ArrayList;

public class Main {
    //константа, обозначающая единичный отрезок времени
    public final int time_const = 1;
    //время между новыми машинами
    public final int time_for_new_cars = 1;
    //начальное количество машин на спавне
    public final int start_cars_on_spawn = 3;
    //машин всего
    public final int car_count = 25;
    //машин в очереди (которые еще не выйхали)
    private ArrayList<Car> cars_queue = new ArrayList<>();
    //машины, которые в пути
    private ArrayList<Car> car_now = new ArrayList<>();
    //список, элементы которого отображают : ехали ли машины и на данном шаге
    private ArrayList<Boolean> is_it_car_was;
    //расстояние, которое машина еще может пройти. если 0, то в списке -1. если полное(скорость * на время), то 0. если остальное, то соответствующее число
    private ArrayList<Double> car_half_length;
    //дорожная система
    private RoadSystem rs;
    //суммарное время
    private double time;

    public Main() throws IOException {
        //общее время = 0
        time = 0;
        //создаем все необходимое
        Analys data = new Analys();
        rs = new RoadSystem(data.getAdj(), data.getWeight(), data.getSpawn(), data.getRoad_type());
        //переменная, отвечающая за индексы машин, которые создаются сразу для спавнов
        int car_index = 0;
        //создаем машины для спавнов
        //для раномерности создаем по одной машине на спавн start_cars_on_spawn раз
        for (int i = 0; i < start_cars_on_spawn; i++) {
            for (int j = 0; j < rs.get_spawns_size(); j++) {
                //создаем саму машину
                Car car1 = new Car(rs, car_index, j+ rs.tlNUM);
                //ставим ее на дрогу
                set_true_road(car1);
                //добавляем в список машин на дороге
                car_now.add(car1);
                //увеличиваем индекс
                car_index++;
            }
        }
        //создаем остальные машины
        for (int i = car_index; i < car_count; i++) {
            cars_queue.add(new Car(rs, i));
        }
        //счетчик, отвечающий за время (для выпуска новой волны машин)
        int time_index = 0;
        //пока еще остались машины перемещаем их
        while (how_many_cars_left() > 0) {
            //если пришло время перемещать машины, то сопственно делаем это
            if(time_index == time_for_new_cars) {
                add_cars();
                //ставим счетчик на 0
                time_index = 0;
            } else {
                time_index++;
            }
            //обновляем списоки
            is_it_car_was_reset();
            car_half_length_reset();
            //запускаем поочереди каждую машину в этом списке
            for (int i = 0; i < car_now.size(); i++) {
                one_step(car_now.get(i));
            }
            //список машин, которые еще не ехали
            ArrayList<Car> car_not_drive = new ArrayList<>();
            //добавляем машины
            for (int i = 0; i < car_now.size(); i++) {
                if(is_it_car_was.get(i) == false) {
                    car_not_drive.add(car_now.get(i));
                }
            }
            //если есть хоть одна машина
            int car_not_drive_length = car_not_drive.size();
            if(car_not_drive_length > 0) {
                //даем ей возможность таки проехать
                for (int i = 0; i < car_not_drive_length; i++) {
                    one_step(car_not_drive.get(i));
                }
                //обновляем список
                car_not_drive = new ArrayList<>();
                for (int i = 0; i < car_now.size(); i++) {
                    if(is_it_car_was.get(i) == false) {
                        car_not_drive.add(car_now.get(i));
                    }
                }
            } else {
                time++;
                continue;
            }
            //ну и если/пока ничего не изменилось повторяем действия выше
            while (car_not_drive_length != car_not_drive.size()) {
                car_not_drive_length = car_not_drive.size();
                for (int i = 0; i < car_not_drive.size(); i++) {
                    one_step(car_not_drive.get(i));
                }
            }
            //прибавляем время
        }
        System.out.println(time + " " + how_many_cars_left());
    }
    //метод двигающий конкретную машину по дороге
    private void one_step(Car car) {
        //если места перед машиной больше чем расстояние, которое она может проехать(физически)
        if(free_road(car) >= car_distance(car)) {
            //то двигаем машину
            //добавляем километраж
            //ставим на нужную полосу
            car.addKm(car_distance(car));
            set_true_road(car);
            //обозначаем, что она уже ехала в двух списках
            is_it_car_was.set(car_now.indexOf(car), true);
            car_half_length.set(car_now.indexOf(car), -1.0);
        } else {
            // если места перед машиной меньше чем расстояние, которое она может проехать(физически) (по усл выше), но при этом > 0
            if(free_road(car) > 0) {
                double half_length = free_road(car);
                //двигаем машину
                car.addKm(half_length);
                set_true_road(car);
                //отмечаем сколько она еще может проехать
                car_half_length.add(car_now.indexOf(car), car_distance(car) - half_length);
            } else {
                //если сдвинуться с места нельзя (по усл выше), но машина находится на перекрестке/светофоре
                if(car.getKm() == true_road(car).length) {
                    //и если она приехала
                    if(car.getNext() == car.getFinish()) {
                        //удаляем машину
                        //удаляем с дорог
                        //из всех списков
                        //обновляем данные
                        delete_on_roads(car);
                        car_half_length.remove(car_now.indexOf(car));
                        is_it_car_was.remove(car_now.indexOf(car));
                        car_now.remove(car);
                        car.reset();
                    } else {
                        //если следующая точка - светофор
                        if(rs.getPt().get(car.getNext()).equals(PointType.Trafficlight)) {
                            //и он показывает красный свет
                            if(!rs.getTl().get(car.getNext()).color(time).equals(true_road(car).getType())) {
                                //то отмечаем машины, как ехавшую и переходим к следущей
                                is_it_car_was.set(car_now.indexOf(car), true);
                                car_half_length.set(car_now.indexOf(car), -1.0);
                                return;
                            }
                        }
                        //но если светофор таки показал зеленый свет, то переезжаем на новую дорогу
                        car.setKm(0);
                        car.next();
                        set_true_road(car);
                        //и едем по ней
                        one_step(car);
                    }
                }
            }
        }
    }
    //сколько машин осталось (всего)
    private int how_many_cars_left() {
        return car_now.size() + cars_queue.size();
    }
    //сколько машин в движении
    private int how_many_cars_wait() {
        return cars_queue.size();
    }
    //обновление is_it_car
    private void is_it_car_was_reset() {
        //обнуление списка
        is_it_car_was = new ArrayList<>();
        //если нет машин, то добавляем новые
        if(car_now.size() < 1) {
            add_cars();
        }
        //добавление машин на дороге в список с меткой "еще не ехала"
        for (int i = 0; i < car_now.size(); i++) {
            is_it_car_was.add(false);
        }
    }

    private void car_half_length_reset() {
        car_half_length = new ArrayList<>();
        if (car_now.size() < 1) {
            add_cars();
        }
        for (int i = 0; i < car_now.size(); i++) {
            car_half_length.add(0.0);
        }
    }
    //метод запуска следущего потока
    private void add_cars() {
        for (int i = 0; i < rs.get_spawns_size(); i++) {
            //проверяем, есть ли еще машины
            if (how_many_cars_wait() >= 1) {
                Car car = cars_queue.get(0);
                //удоляем машину из спика ожидания
                cars_queue.remove(0);
                //ставим на нужную дорогу
                set_true_road(car);
                //добавляем в списоки
                car_now.add(car);
                is_it_car_was.add(false);
                car_half_length.add(0.0);
            }
        }
    }
    //метод определяющий оптимальную дорогу/полосу для машины и, заодно, стаящий машину на нее
    private Road true_road(Car car) {
        ArrayList<Road> road = rs.getRoad(car.getNow(),car.getNext());
        int road_index = BinarySearch.search(car, road);
        Road true_road = road.get(road_index);
        return true_road;
    }
    private void set_true_road(Car car) {
        delete_on_roads(car);
        ArrayList<Road> road = rs.getRoad(car.getNow(),car.getNext());
        int road_index = BinarySearch.search(car, road);
        Road true_road = road.get(road_index);
        rs.set_car(car, road_index, rs.getRoads().indexOf(true_road));
    }

    public double free_road(Car car) {
        Road true_road = true_road(car);
        int possition = true_road.getList().indexOf(car);
        if (possition >= 1) {
            return true_road.getKm().get(possition - 1) - car.getKm();
        } else {
            return true_road.length - car.getKm();
        }
    }

    private void delete_on_roads(Car car) {
        for (int i = 0; i < rs.getRoad(car.getNow(), car.getNext()).size(); i++) {
            int index = rs.getRoad(car.getNow(), car.getNext()).get(i).getList().indexOf(car);
            if (index != -1) {
                rs.getRoad(car.getNow(), car.getNext()).get(i).getList().remove(index);
            }
        }
    }

    private double car_distance(Car car) {
        if(car_half_length.get(car_now.indexOf(car)) == 0.0) {
            return car.speed * time_const;
        } else {
            if(car_half_length.get(car_now.indexOf(car)) == -1.0) {
                return 0;
            } else {
                return car_half_length.get(car_now.indexOf(car));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }
}

public class Trafficlight {

    private int green;
    private int red;

    public Trafficlight(int green, int red) {
        this.green = green;
        this.red = red;
    }

    public RoadType color(double time) {
        if (time / summ() >= 1) {
            time -= summ() * (int)(time / summ());
        }
        if (time < green) {
            return RoadType.Horizont;
        } else {
            return RoadType.Vertical;
        }
    }

    private int summ() {
        return green + red;
    }

    public int getGreen() {
        return green;
    }

    public int getRed() {
        return red;
    }
}

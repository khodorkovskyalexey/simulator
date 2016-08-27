import java.util.ArrayList;

public class Spawn {
    private int nexttl;
    private RoadType type;

    public Spawn(RoadType type, int tlobj) {
        nexttl = tlobj;
        this.type = type;
    }

    public int getNexttl() {
        return nexttl;
    }

    public RoadType getType() {
        return type;
    }
}

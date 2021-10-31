package dz.isolation.model;

public class Team {
    String id;
    String color;
    String points;

    public Team(String id, String color, String points) {
        this.id = id;
        this.color = color;
        this.points = points;
    }

    public Team(String color, String points) {
        this(null, color, points);
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getPoints() {
        return points;
    }
}

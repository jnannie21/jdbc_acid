package dz.isolation.model;

public class Team {
    int id;
    String color;
    int points;

    public Team(int id, String color, int points) {
        this.id = id;
        this.color = color;
        this.points = points;
    }

    public Team(String color, int points) {
        this.color = color;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

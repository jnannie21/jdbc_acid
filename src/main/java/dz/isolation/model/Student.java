package dz.isolation.model;

/**
 * Student's table entity.
 */
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private int points;
    private int teamId;

    public Student(int id,
                   String firstName,
                   String lastName,
                   int age,
                   int points,
                   int teamId
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.points = points;
        this.teamId = teamId;
    }

    public Student(String firstName,
                   String lastName,
                   int age,
                   int points,
                   int teamId
    ) {
        this(0, firstName, lastName, age, points, teamId);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public int getPoints() {
        return points;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}

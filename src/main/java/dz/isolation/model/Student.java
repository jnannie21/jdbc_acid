package dz.isolation.model;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String age;
    private String points;
    private String teamId;

    public Student(String id,
                   String firstName,
                   String lastName,
                   String age,
                   String points,
                   String teamId
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
                   String age,
                   String points,
                   String teamId
    ) {
        this(null, firstName, lastName, age, points, teamId);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getPoints() {
        return points;
    }

    public String getTeamId() {
        return teamId;
    }
}

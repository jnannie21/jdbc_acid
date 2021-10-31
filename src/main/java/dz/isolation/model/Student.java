package dz.isolation.model;

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String age;
    private String points;

    public Student(String id,
                   String firstName,
                   String lastName,
                   String age,
                   String points
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.points = points;
    }

    public Student(String firstName,
                   String lastName,
                   String age,
                   String points
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.points = points;
    }

//    public Student(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

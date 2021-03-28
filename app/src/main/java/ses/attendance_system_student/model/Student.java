package ses.attendance_system_student.model;

public class Student {
    private String student_id;
    private String UID;
    private String student_name;
    private String student_email;

    public Student() {
    }

    public String getId() {
        return student_id;
    }

    public void setId(String id) {
        this.student_id = id;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return student_name;
    }

    public void setName(String name) {
        this.student_name = name;
    }

    public String getEmail() {
        return student_email;
    }

    public void setEmail(String email) {
        this.student_email = email;
    }

    public Student(String student_id, String UID, String student_name, String student_email) {
        this.student_id = student_id;
        this.UID = UID;
        this.student_name = student_name;
        this.student_email = student_email;
    }
}

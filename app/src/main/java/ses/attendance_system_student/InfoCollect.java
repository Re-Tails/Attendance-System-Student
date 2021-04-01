package ses.attendance_system_student;

public class InfoCollect {
    private String UID;
    private String studentEmail;
    private String studentName;
    private String studentSubject1;
    private String studentSubject2;
    private String studentSubject3;
    private String studentSubject4;
    private String studentID;

    public InfoCollect(String UID, String studentEmail, String studentName, String studentSubject1, String studentSubject2, String studentSubject3, String studentSubject4, String studentID) {
        this.UID = UID;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.studentSubject1 = studentSubject1;
        this.studentSubject2 = studentSubject2;
        this.studentSubject3 = studentSubject3;
        this.studentSubject4 = studentSubject4;
        this.studentID = studentID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSubject1() {
        return studentSubject1;
    }

    public void setStudentSubject1(String studentSubject1) {
        this.studentSubject1 = studentSubject1;
    }

    public String getStudentSubject2() {
        return studentSubject2;
    }

    public void setStudentSubject2(String studentSubject2) {
        this.studentSubject2 = studentSubject2;
    }

    public String getStudentSubject3() {
        return studentSubject3;
    }

    public void setStudentSubject3(String studentSubject3) {
        this.studentSubject3 = studentSubject3;
    }

    public String getStudentSubject4() {
        return studentSubject4;
    }

    public void setStudentSubject4(String studentSubject4) {
        this.studentSubject4 = studentSubject4;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}

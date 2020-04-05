package enteties;

public class User {

    private String id;
    private String login;
    private String password;
    private String addres;
    private String phone;

    public User (String id, String login, String password, String addres, String phone) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.addres = addres;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getAddres() {
        return addres;
    }

    public String getPhone() {
        return phone;
    }

}
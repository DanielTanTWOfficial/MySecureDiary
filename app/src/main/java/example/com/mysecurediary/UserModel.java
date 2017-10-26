package example.com.mysecurediary;

/**
 * Created by teckw on 10/3/2017.
 */

public class UserModel {
    private String username;
    private String password;
    private String salt;

    public UserModel(String username, String password, String salt){
        setUsername(username);
        setPassword(password);
        setSalt(salt);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() { return salt; }

    public void setSalt(String salt) { this.salt = salt; }

    @Override
    public String toString(){
        return this.username + "/n" + this.password + "\n" + this.salt;
    }
}

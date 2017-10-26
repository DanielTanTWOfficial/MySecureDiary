package example.com.mysecurediary;

/**
 * Created by teckw on 10/3/2017.
 */

public class EntryModel {
    private String username;
    private String date;
    private String content;

    public EntryModel(String username, String date, String content){
        setUsername(username);
        setDate(date);
        setContent(content);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return this.date + "\n" + this.content + "\n";
    }
}

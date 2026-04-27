package FinalProjectBeta;

// File: User.java
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int score;
    // other fields, such as password

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", score=" + score +
                '}';
    }


}


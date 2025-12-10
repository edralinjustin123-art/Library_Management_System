package Library;

import java.io.*;

public class UserDAO {

    private static final String FILE_PATH = "users.txt";

    // Login method
    public User login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                int id = Integer.parseInt(parts[0]); // fake ID
                String u = parts[1];
                String p = parts[2];

                if (u.equals(username) && p.equals(password)) {
                    User user = new User(u, p);
                    user.setId(id);
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // login failed
    }

    // Register method
    public boolean register(User user) {
        try {
            // Check if username already exists
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    if (parts[1].equals(user.getUsername())) return false;
                }
            }

            // Assign fake ID: last ID + 1
            int lastId = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    lastId = Math.max(lastId, Integer.parseInt(parts[0]));
                }
            }

            int newId = lastId + 1;
            user.setId(newId);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
                bw.write(user.getId() + "," + user.getUsername() + "," + user.getPassword());
                bw.newLine();
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

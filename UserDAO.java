package Library;

import java.io.*;

public class UserDAO {

    private static final String FILE_PATH = "users.txt";

    // Login reads from the file
    public User login(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                int id = Integer.parseInt(parts[0].trim());
                String u = parts[1].trim();
                String p = parts[2].trim();

                if (u.equals(username) && p.equals(password)) {
                    User user = new User(u, p);
                    user.setId(id);
                    return user;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Register new user: writes to users.txt and sets the User.id
    public boolean register(User user) {
        try {
            // Check if username exists
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    if (parts[1].trim().equals(user.getUsername())) return false;
                }
            }

            // Get last ID
            int lastId = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    lastId = Math.max(lastId, Integer.parseInt(parts[0].trim()));
                }
            }

            int newId = lastId + 1;
            user.setId(newId);

            // Write new user to file
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

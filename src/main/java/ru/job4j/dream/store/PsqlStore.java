package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.job4j.dream.model.User;

public class PsqlStore implements Store {

    private static final Logger LOGGER = LogManager.getLogger();
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        PsqlStore.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract all posts from database", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"), it.getInt("cityid"),
                            it.getDate("registered").toLocalDate()));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract all candidates from database", e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO post(name,created) VALUES (?, ?)",
                             PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setObject(2, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to create a post in the database", e);
        }
        return post;
    }

    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO candidate(name,cityid,registered) VALUES (?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setObject(3, candidate.getRegistered());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to create a candidate in the database", e);
        }
        return candidate;
    }

    private void updatePost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("UPDATE post SET name = ? WHERE id = ?")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.warn("Failed to update a post in the database", e);
        }
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("UPDATE candidate SET name = ? cityid = ?, registered = ? WHERE id = ?")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setObject(3, candidate.getRegistered());
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.warn("Failed to update a candidate in the database", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    post = new Post(id, it.getString("name"));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract post by id from database", e);
        }
        return post;
    }

    @Override
    public Candidate findCandidateById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = new Candidate(id, it.getString("name"), it.getInt("cityid"),
                            it.getDate("registered").toLocalDate());
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract candidate by id from database", e);
        }
        return candidate;
    }

    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOGGER.warn("Failed to delete a candidate by id from database", e);
        }
    }

    public void truncateTable(String tableName) {
        if (!"post".equals(tableName) && !"candidate".equals(tableName) && !"users".equals(tableName)) {
            throw new IllegalArgumentException("Illegal table name for truncating");
        }
        try (Connection connection = pool.getConnection();
             Statement statement = connection.createStatement()) {
           statement.executeUpdate("truncate table " + tableName);
           statement.executeUpdate("ALTER SEQUENCE " + tableName + "_id_seq RESTART WITH 1");
        } catch (SQLException e) {
            LOGGER.warn("Failed to truncate " + tableName, e);
        }

    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(it.getInt("id"), it.getString("name"), it.getString("email"), it.getString("password")));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract all users from database", e);
        }
        return users;
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == 0) {
            createUser(user);
        } else {
            updateUser(user);
        }
    }

    private void updateUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE users SET name = ? , email = ? , password = ? WHERE id = ?")
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        } catch (Exception e) {
            LOGGER.warn("Failed to update a user in the database", e);
        }
    }

    private User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to create a user in the database", e);
        }
        return user;
    }

    @Override
    public User findUserById(int id) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users WHERE id = ?")
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    user = new User(id, it.getString("name"), it.getString("email"), it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract user by id from database", e);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users WHERE email = ?")
        ) {
            ps.setString(1, email);
            ps.execute();
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    user = new User(it.getInt("id"), it.getString("name"), email, it.getString("password"));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract user by email from database", e);
        }
        return user;
    }

    @Override
    public Collection<Candidate> findLastDayCandidates() {
        List<Candidate> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT * FROM Candidate WHERE registered BETWEEN current_timestamp - interval '1 day' AND current_timestamp")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getInt("cityid"),
                            it.getDate("registered").toLocalDate()));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract last day candidates from database", e);
        }
        return rsl;
    }

    @Override
    public Collection<Post> findLastDayPosts() {
        List<Post> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "SELECT * FROM post WHERE created BETWEEN current_timestamp - interval '1 day' AND current_timestamp")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(new Post(it.getInt("id"),
                            it.getString("name"),
                            it.getDate("created").toLocalDate()));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract last day posts from database", e);
        }
        return rsl;
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> rsl = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM city")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    rsl.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to extract all cities from database", e);
        }
        return rsl;
    }

}
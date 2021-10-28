package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DbStoreTest {

    @Test
    public void whenFindAllPosts() {
        Store store = PsqlStore.instOf();
        store.truncateTable("post");
        Post post1 = new Post(0, "1 Java Job");
        Post post2 = new Post(0, "2 Java Job");
        Post post3 = new Post(0, "3 Java Job");
        store.savePost(post1);
        store.savePost(post2);
        store.savePost(post3);
        assertThat(store.findAllPosts(), is(List.of(post1, post2, post3)));
    }

    @Test
    public void whenFindAllUsers() {
        Store store = PsqlStore.instOf();
        store.truncateTable("users");
        User user1 = new User(0, "1 User", "1 Email", "1 Password");
        User user2 = new User(0, "2 User", "2 Email", "2 Password");
        User user3 = new User(0, "3 User", "3 Email", "3 Password");
        store.saveUser(user1);
        store.saveUser(user2);
        store.saveUser(user3);
        assertThat(store.findAllUsers(), is(List.of(user1, user2, user3)));
    }

    @Test
    public void whenFindAllCandidates() {
        Store store = PsqlStore.instOf();
        store.truncateTable("candidate");
        Candidate candidate1 = new Candidate(0, "1 Java Developer");
        Candidate candidate2 = new Candidate(0, "2 Java Developer");
        Candidate candidate3 = new Candidate(0, "3 Java Developer");
        store.saveCandidate(candidate1);
        store.saveCandidate(candidate2);
        store.saveCandidate(candidate3);
        assertThat(store.findAllCandidates(), is(List.of(candidate1, candidate2, candidate3)));
    }

    @Test
    public void whenCreateCandidate() {
        Store store = PsqlStore.instOf();
        store.truncateTable("candidate");
        Candidate candidate = new Candidate(0, "Java Developer");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidateInDb.getName()));
    }

    @Test
    public void whenCreateUser() {
        Store store = PsqlStore.instOf();
        store.truncateTable("users");
        User user = new User(0, "User", "Email", "Password");
        store.saveUser(user);
        User userInDb = store.findUserById(user.getId());
        assertThat(userInDb.getName(), is(userInDb.getName()));
    }

    @Test
    public void whenCreatePost() {
        Store store = PsqlStore.instOf();
        store.truncateTable("post");
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void deleteCandidate() {
        Store store = PsqlStore.instOf();
        store.truncateTable("candidate");
        Candidate candidate1 = new Candidate(0, "1 Java Developer");
        Candidate candidate2 = new Candidate(0, "2 Java Developer");
        store.saveCandidate(candidate1);
        store.saveCandidate(candidate2);
        assertThat(store.findAllCandidates(), is(List.of(candidate1, candidate2)));
        store.deleteCandidate(1);
        assertThat(store.findAllCandidates(), is(List.of(candidate2)));
    }
}
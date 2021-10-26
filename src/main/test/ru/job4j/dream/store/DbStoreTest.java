package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

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
        store.truncateTable("post");
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
        store.truncateTable("candidate");
    }

    @Test
    public void whenCreateCandidate() {
        Store store = PsqlStore.instOf();
        store.truncateTable("candidate");
        Candidate candidate = new Candidate(0, "Java Developer");
        store.saveCandidate(candidate);
        Candidate candidateInDb = store.findCandidateById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidateInDb.getName()));
        store.truncateTable("candidate");
    }

    @Test
    public void whenCreatePost() {
        Store store = PsqlStore.instOf();
        store.truncateTable("post");
        Post post = new Post(0, "Java Job");
        store.savePost(post);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
        store.truncateTable("post");
    }
}
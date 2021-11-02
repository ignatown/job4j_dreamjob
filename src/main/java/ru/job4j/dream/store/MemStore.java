package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore {

    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);
    private final Map<Integer, City> cities = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job"));
        posts.put(2, new Post(2, "Middle Java Job"));
        posts.put(3, new Post(3, "Senior Java Job"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(POST_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public void delete(int id) {
        candidates.remove(id);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public Collection<Post> findLastDayPosts() { return posts.values(); }

    public Collection<Candidate> findLastDayCandidates() { return candidates.values(); }

    public Collection<City> findAllCities() { return cities.values(); }
}
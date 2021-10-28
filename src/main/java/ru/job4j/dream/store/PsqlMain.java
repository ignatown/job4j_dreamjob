package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

public class PsqlMain {
    public static void main(String[] args) {
        Store store1 = PsqlStore.instOf();
        store1.savePost(new Post(0, "1Java Job"));
        store1.savePost(new Post(0, "2Java Job"));
        store1.savePost(new Post(0, "3Java Job"));
        System.out.println("Old id=3 post: " + store1.findPostById(2).getName());
        store1.savePost(new Post(3, "new3Java Job"));
        for (Post post : store1.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println("new id=3 post: " + store1.findPostById(3).getName());
        System.out.println("-----------");
        Store store2 = PsqlStore.instOf();
        store2.saveCandidate(new Candidate(0, "1Java Developer"));
        store2.saveCandidate(new Candidate(0, "2Java Developer"));
        store2.saveCandidate(new Candidate(0, "3Java Developer"));
        System.out.println("Old id=3 candidate: " + store2.findCandidateById(2).getName());
        store2.saveCandidate(new Candidate(3, "new3Java Developer"));
        for (Candidate candidate : store2.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println("new id=3 candidate: " + store2.findCandidateById(3).getName());
        System.out.println("-----------");
        Store store3 = PsqlStore.instOf();
        store3.saveUser(new User(0, "user1", "email1", "password1"));
        store3.saveUser(new User(0, "user2", "email2", "password2"));
        store3.saveUser(new User(0, "user3", "email3", "password3"));

        System.out.println("Old id=3 user: " + store3.findUserById(3).getName());
        store3.saveUser(new User(0, "NEW user3", "email3", "password3"));
        for (User user : store3.findAllUsers()) {
            System.out.println(user.getId() + " " + user.getName());
        }
        System.out.println("new id=3 user: " + store3.findUserById(3).getName());
    }
}
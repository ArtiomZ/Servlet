package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;

// Stub
public class PostRepository {

  private final static List<Post> posts = Collections.synchronizedList(new ArrayList<>());
  private final static Queue<Integer> freeIndexes = new LinkedList<>();
  private static int IdCounter;
  private static long removeIdCounter = -1;

  public List<Post> all() {
    if (posts.isEmpty()) {
      throw new NotFoundException("not found");
    } else {
      return posts;
    }
  }

  public Optional<Post> getById(long id) {
    if (id >= posts.size()) {
      throw new NotFoundException("not found");
    } else {
      return Optional.ofNullable(posts.get((int) id));
    }
  }

  public synchronized Post save(Post post) throws NotFoundException {
    if (post.getId() == 0) {
      if (freeIndexes.peek() == null) {
        post.setId(IdCounter);
        posts.add(post);
        IdCounter++;
      } else {
        int ind = freeIndexes.poll();
        post.setId(ind);
        posts.set(ind, post);
      }
    } else {
      if (post.getId() <= posts.size()) {
        posts.set((int) post.getId(), post);
      } else {
        throw new NotFoundException("post not found");
      }
    }
    return post;
  }

  public void removeById(long id) {
    if (id >= posts.size()) {
      throw new NotFoundException("not found");
    } else {
      freeIndexes.offer((int) id);
      posts.remove((int) id);
      posts.add((int) id, new Post(removeIdCounter, "removed element"));
      removeIdCounter--;
    }
  }
}

package letscode.hnreader.api;

import letscode.hnreader.domain.Post;
import letscode.hnreader.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByIds(List<Integer> ids) {
        return postRepository.findByIdIn(ids);
    }

    public long count() {
        return postRepository.count();
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAsync(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        postRepository.saveAll(posts);
        log.info("Saved {} posts to database", posts.size());
    }
}

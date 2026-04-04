package letscode.hnreader.api;

import letscode.hnreader.api.dto.HnItem;
import letscode.hnreader.domain.Post;
import letscode.hnreader.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    /**
     * Convert DTO HnItem to Post.
     */
    public Post toEntity(HnItem dto) {
        return dto == null
            ? null
            : Post.builder()
            .id(dto.getId())
            .text(dto.getText())
            .by(dto.getBy())
            .score(dto.getScore())
            .time(dto.getTime())
            .title(dto.getTitle())
            .type(dto.getType())
            .url(dto.getUrl())
            .parent(dto.getParent())
            .descendants(dto.getDescendants())
            .build();

    }

    /**
     * Convert list of DTO to list of entities.
     */
    public List<Post> toEntityList(List<HnItem> dtos) {
        return dtos == null
            ? List.of()
            : dtos.stream()
            .map(this::toEntity)
            .toList();
    }

    /**
     * Convert entity Post to DTO HnItem.
     */
    public HnItem toDto(Post entity) {
        return entity == null
            ? null
            : HnItem.builder()
            .id(entity.getId())
            .text(entity.getText())
            .by(entity.getBy())
            .score(entity.getScore())
            .time(entity.getTime())
            .title(entity.getTitle())
            .type(entity.getType())
            .url(entity.getUrl())
            .parent(entity.getParent())
            .descendants(entity.getDescendants())
            .build();

    }

    /**
     * Find posts from DB by IDs.
     */
    public List<Post> findByIds(List<Integer> ids) {
        return postRepository.findByIdIn(ids);
    }

    /**
     * Async save posts into DB without blocking.
     */
    @Async
    @Transactional
    public void saveAsync(List<Post> posts) {
        if (posts == null || posts.isEmpty()) {
            return;
        }
        postRepository.saveAll(posts);
        log.info("Saved {} posts to database", posts.size());
    }

    /**
     * Find posts from DB with pagination.
     */
    public Page<Post> findPostsPaginated(List<Integer> ids, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        return postRepository.findAllByIdIn(ids, pageRequest);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}

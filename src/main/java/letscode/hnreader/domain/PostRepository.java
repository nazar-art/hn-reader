package letscode.hnreader.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByIdIn(List<Integer> ids);

    Page<Post> findAllByIdIn(List<Integer> ids, Pageable pageable);
}

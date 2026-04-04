package letscode.hnreader.api.mapper;

import letscode.hnreader.api.dto.HnItem;
import letscode.hnreader.domain.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    Post toEntity(HnItem dto);

    HnItem toDto(Post entity);

    List<Post> toEntityList(List<HnItem> dtos);

    List<HnItem> toDtoList(List<Post> entities);
}

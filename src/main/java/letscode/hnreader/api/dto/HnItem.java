package letscode.hnreader.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class HnItem {
    private Integer id;
    private Boolean deleted;
    private String type;
    private String by;
    private Long time;
    private String text;
    private Boolean dead;
    private Integer parent;
    private Integer poll;
    private Integer[] kids;
    private String url;
    private Integer score;
    private String title;
    private Integer[] parts;
    private Integer descendants;
}

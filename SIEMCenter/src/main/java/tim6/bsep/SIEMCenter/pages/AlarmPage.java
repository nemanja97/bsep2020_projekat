package tim6.bsep.SIEMCenter.pages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import tim6.bsep.SIEMCenter.model.drools.Alarm;

import java.util.ArrayList;
import java.util.List;

public class AlarmPage extends PageImpl<Alarm> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AlarmPage(@JsonProperty("content") List<Alarm> content,
                     @JsonProperty("number") int number,
                     @JsonProperty("size") int size,
                     @JsonProperty("totalElements") Long totalElements,
                     @JsonProperty("pageable") JsonNode pageable,
                     @JsonProperty("last") boolean last,
                     @JsonProperty("totalPages") int totalPages,
                     @JsonProperty("sort") JsonNode sort,
                     @JsonProperty("first") boolean first,
                     @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public AlarmPage(List<Alarm> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public AlarmPage(List<Alarm> content) {
        super(content);
    }

    public AlarmPage() {
        super(new ArrayList<>());
    }

}

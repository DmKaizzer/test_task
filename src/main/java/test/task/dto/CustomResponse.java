package test.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CustomResponse {

    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<?> errors;

    public CustomResponse(Boolean success) {
        this.success = success;
    }
}

package org.francis.kcb_assessment.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.francis.kcb_assessment.config.doc.SwaggerDocSamples;

import java.time.LocalDate;

@Getter
@Setter
@Schema(example = SwaggerDocSamples.CREATE_TASK_REQUEST)
public class CreateTaskDTO {

    @NotBlank
    @Size(min = 3, max = 250)
    private String title;

    @NotBlank
    @Size(min = 3, max = 500)
    private String description;

    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dueDate;
}

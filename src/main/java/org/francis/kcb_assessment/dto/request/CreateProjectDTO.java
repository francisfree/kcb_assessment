package org.francis.kcb_assessment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.francis.kcb_assessment.config.doc.SwaggerDocSamples;

@Getter
@Setter
@Schema(example = SwaggerDocSamples.CREATE_PROJECT_REQUEST)
public class CreateProjectDTO {

    @NotBlank
    @Size(min = 3, max = 250)
    private String name;

    @NotBlank
    @Size(min = 3, max = 500)
    private String description;
}

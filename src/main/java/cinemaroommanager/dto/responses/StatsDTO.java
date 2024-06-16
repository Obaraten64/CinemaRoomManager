package cinemaroommanager.dto.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record StatsDTO(int income, int available, int purchased) {
}

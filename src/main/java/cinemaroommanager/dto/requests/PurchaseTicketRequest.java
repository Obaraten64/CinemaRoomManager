package cinemaroommanager.dto.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record PurchaseTicketRequest(int row, int column) {
}

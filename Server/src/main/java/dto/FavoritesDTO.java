package dto;

import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FavoritesDTO implements Serializable {
    private String userId;
    private int apartTransactionId;

}

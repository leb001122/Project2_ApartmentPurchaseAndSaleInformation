package dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApartPriceIndicesDTO implements Serializable {
    private int id;
    private String date;
    private String region;
    private float index;
}
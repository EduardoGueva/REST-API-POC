package sycod.com.poc.model;

import com.redis.om.spring.annotations.Indexed;
import lombok.*;

@Builder
@RequiredArgsConstructor(staticName = "of")
@Data
public class RegionBolsa {

    @Indexed @NonNull
    private Integer region;
    @Indexed @NonNull
    private Long horaLDC;
    @NonNull
    private Double saldoInicial;
    @NonNull
    private Double saldoFinal;
    @Indexed @NonNull
    private Long fechaSaldo;
}

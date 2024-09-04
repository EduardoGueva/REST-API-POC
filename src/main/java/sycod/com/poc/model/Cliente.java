package sycod.com.poc.model;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Set;

@RequiredArgsConstructor(staticName = "of")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Document
public class Cliente {
    @Id
    @Indexed
    private String ulid;
    @Indexed
    private Integer idCliente;
    @Searchable @NonNull
    private String nombre;
    private String rfc;
    private Integer tipoBolsa;
    private String codigoCliente;
    private Set<RegionBolsa> regionBolsas;
}

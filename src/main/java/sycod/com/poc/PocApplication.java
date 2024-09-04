package sycod.com.poc;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sycod.com.poc.model.Cliente;
import sycod.com.poc.model.RegionBolsa;
import sycod.com.poc.repository.ClienteRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;

@EnableRedisDocumentRepositories(basePackages = "sycod.com.poc.*")
@SpringBootApplication
public class PocApplication {

	@Bean
	CommandLineRunner loadTestData(ClienteRepository repo) {
		return args -> {
			repo.deleteAll();
			Random rn = new Random();

			RegionBolsa regionBolsa1 = RegionBolsa.builder()
					.region(Math.abs(rn.nextInt()))
					.horaLDC(Timestamp.valueOf(LocalDateTime.now()).getTime())
					.saldoInicial(Math.abs(rn.nextDouble()))
					.saldoFinal(Math.abs(rn.nextDouble()))
					.fechaSaldo(Timestamp.valueOf(LocalDateTime.now()).getTime())
					.build();

			RegionBolsa regionBolsa2 = RegionBolsa.builder()
					.region(Math.abs(rn.nextInt()))
					.horaLDC(Timestamp.valueOf(LocalDateTime.now()).getTime())
					.saldoInicial(Math.abs(rn.nextDouble()))
					.saldoFinal(Math.abs(rn.nextDouble()))
					.fechaSaldo(Timestamp.valueOf(LocalDateTime.now()).getTime())
					.build();

			Cliente cliente = Cliente.builder()
					.idCliente(Math.abs(rn.nextInt()))
					.nombre("cliente")
					.codigoCliente("cliente")
					.rfc("rfc")
					.tipoBolsa(rn.nextInt(1,99))
					.regionBolsas(Set.of(regionBolsa1,regionBolsa2))
					.build();

			repo.save(cliente);

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(PocApplication.class, args);
	}

}

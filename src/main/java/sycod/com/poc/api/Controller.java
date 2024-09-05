package sycod.com.poc.api;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.bedrockruntime.endpoints.internal.Value;
import sycod.com.poc.model.Cliente;
import sycod.com.poc.model.RegionBolsa;
import sycod.com.poc.repository.ClienteRepository;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/clientes", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private void incrementJson(String key, String path, double increment){
        String command = "JSON.NUMINCRBY";
        redisTemplate.execute((RedisCallback<Object>) (connection)->{
            connection.execute(command,key.getBytes(),path.getBytes(),String.valueOf(increment).getBytes());
            return null;
        });
    }

    private void decrementJson(String key, String path, double increment){
        double decrement = increment*(-1);
        String command = "JSON.NUMINCRBY";
        redisTemplate.execute((RedisCallback<Object>) (connection)->{
            connection.execute(command,key.getBytes(),path.getBytes(),String.valueOf(decrement).getBytes());
            return null;
        });
    }

    @Autowired
    ClienteRepository repo;

    @GetMapping("ulid")
    Cliente getClienteByIDCliente(@RequestParam("ulid") String ulid){
        return repo.findById(ulid).orElse(Cliente.of("Vacio"));
    }

    @GetMapping("id-cliente")
    Cliente getClienteByID(@RequestParam("idCliente") Integer id){
        return repo.findByIdCliente(id);
    }

    @GetMapping("nombre")
    List<Cliente> getClienteByNombre(@RequestParam("nombre") String nombre){
        return repo.findByNombre(nombre);
    }

    @GetMapping("region-bolsas")
    Set<RegionBolsa> getRegionBolsasByIdCliente(@RequestParam("idCliente") Integer id){
        return repo.findByIdCliente(id).getRegionBolsas();
    }

    @GetMapping("region-bolsa")
    RegionBolsa getRegionBolsasByIdCliente(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region){
        return repo.findByIdCliente(id).getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null);
    }

    @GetMapping("region-bolsa/saldo")
    Double getRegionBolsasByIdClienteSaldo(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region){
        return repo.findByIdCliente(id).getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null).getSaldoInicial();
    }

    @PostMapping(path = "crear",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Cliente postCliente(@RequestBody @NonNull Cliente cliente){
        return repo.save(cliente);
    }

    @PostMapping(path = "crear/region-bolsa",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    Cliente postRegionBolsa(@RequestParam("idCliente") Integer id, @RequestBody @NonNull RegionBolsa regionBolsa){
        Cliente cliente = repo.findByIdCliente(id);
        cliente.getRegionBolsas().add(regionBolsa);
        return repo.save(cliente);
    }

    @PutMapping(path = "actualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    Cliente putCliente(@RequestParam("idCliente") Integer id, @RequestBody @NotNull Cliente newCliente){
        Cliente cliente = repo.findByIdCliente(id);
        cliente.setCodigoCliente(newCliente.getCodigoCliente());
        cliente.setRfc(newCliente.getRfc());
        cliente.setNombre(newCliente.getNombre());
        cliente.setTipoBolsa(newCliente.getTipoBolsa());
        return repo.save(cliente);
    }

    @PutMapping(path = "actualizar/region-bolsa", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    Cliente putRegionBolsa(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region, @RequestBody @NotNull RegionBolsa newRegionBolsa){
        Cliente cliente = repo.findByIdCliente(id);
        RegionBolsa regionBolsa = cliente.getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null);
        cliente.getRegionBolsas().remove(regionBolsa);
        regionBolsa.setFechaSaldo(newRegionBolsa.getFechaSaldo());
        regionBolsa.setHoraLDC(newRegionBolsa.getHoraLDC());
        regionBolsa.setSaldoInicial(newRegionBolsa.getSaldoInicial());
        regionBolsa.setSaldoFinal(newRegionBolsa.getSaldoFinal());
        cliente.getRegionBolsas().add(regionBolsa);
        return repo.save(cliente);
    }

    @PutMapping("actualizar/region-bolsa/saldo")
    Cliente putSaldoRegionBolsa(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region, @RequestParam("saldo") Double saldo){
        Cliente cliente = repo.findByIdCliente(id);
        RegionBolsa regionBolsa = cliente.getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null);
        cliente.getRegionBolsas().remove(regionBolsa);
        regionBolsa.setSaldoInicial(saldo);
        cliente.getRegionBolsas().add(regionBolsa);
        return repo.save(cliente);
    }

    @PutMapping("actualizar/region-bolsa/resta-saldo")
    Cliente putRestaSaldoRegionBolsa(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region, @RequestParam("saldo") Double saldo){
        Cliente cliente = repo.findByIdCliente(id);
        RegionBolsa regionBolsa = cliente.getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null);
        String key = "sycod.com.poc.model.Cliente:"+cliente.getUlid();
        decrementJson(key,".regionBolsas[1].saldoInicial",saldo);
        return repo.findByIdCliente(id);
    }

    @PutMapping("actualizar/region-bolsa/suma-saldo")
    Cliente putSumaSaldoRegionBolsa(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region, @RequestParam("saldo") Double saldo){
        Cliente cliente = repo.findByIdCliente(id);
        RegionBolsa regionBolsa = cliente.getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null);
        String key = "sycod.com.poc.model.Cliente:"+cliente.getUlid();
        incrementJson(key,".regionBolsas[1].saldoInicial",saldo);
        return repo.findByIdCliente(id);
    }
    @DeleteMapping("borrar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCliente(@RequestParam("idCliente") Integer id){
        repo.delete(repo.findByIdCliente(id));
    }

    @DeleteMapping("borrar/region-bolsa")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRegionBolsa(@RequestParam("idCliente") Integer id, @RequestParam("region") Integer region){
        Cliente cliente = repo.findByIdCliente(id);
        cliente.getRegionBolsas().remove(cliente.getRegionBolsas().stream().filter(rb->rb.getRegion().equals(region)).findFirst().orElse(null));
        repo.save(cliente);
    }
}

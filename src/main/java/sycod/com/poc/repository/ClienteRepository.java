package sycod.com.poc.repository;

import com.redis.om.spring.repository.RedisDocumentRepository;
import sycod.com.poc.model.Cliente;

import java.util.List;

public interface ClienteRepository extends RedisDocumentRepository<Cliente,String> {
    Cliente findByIdCliente(Integer idCliente);
    List<Cliente> findByNombre(String nombre);

}

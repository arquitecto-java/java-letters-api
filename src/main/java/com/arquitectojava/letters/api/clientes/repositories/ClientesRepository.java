package com.arquitectojava.letters.api.clientes.repositories;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientesRepository extends CrudRepository<Cliente, Integer> {

    @Query(value = "select c from Cliente c where (c.docId is not null and c.docId = ?1) or (c.phone is not null and c.phone = ?2) " +
            "or (c.instagram is not null and c.instagram = ?3) or (c.email is not null and c.email = ?4)")
    List<Cliente> findByDocOrPhoneOrInstagramOrEmail(String docId, String phone, String instagram, String email);
}

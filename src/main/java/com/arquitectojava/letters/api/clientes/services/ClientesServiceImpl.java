package com.arquitectojava.letters.api.clientes.services;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.clientes.exceptions.ClienteDuplicadoException;
import com.arquitectojava.letters.api.clientes.repositories.ClientesRepository;
import com.arquitectojava.letters.api.exceptions.DatosEntidadInsuficientesException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class ClientesServiceImpl implements ClientesService {

    protected ClientesRepository clientesRepository;

    protected ApplicationEventPublisher publisher;

    public ClientesServiceImpl(@Autowired ClientesRepository clientesRepository){
        this.clientesRepository = clientesRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clientesRepository.findAll();
    }

    @Override
    public Cliente save(Cliente cliente) {
        validateRequiredFields(cliente);

        validateDuplicates(cliente);

        Cliente existing = cliente.getId() != null ? clientesRepository.findById(cliente.getId()).orElse(null) : null;
        if (existing != null){
            existing.setFname(cliente.getFname());
            existing.setLname(cliente.getLname());
            existing.setDocId(cliente.getDocId());
            existing.setPhone(cliente.getPhone());
            existing.setInstagram(cliente.getInstagram());
            existing.setEmail(cliente.getEmail());
            existing.setAddress(cliente.getAddress());
            return clientesRepository.save(existing);
        } else {
            return clientesRepository.save(cliente);
        }
    }

    private void validateRequiredFields(Cliente cliente) throws DatosEntidadInsuficientesException {
        boolean fname = StringUtils.isNotBlank(cliente.getFname()) && !"null".equals(cliente.getFname());
        boolean lname = StringUtils.isNotBlank(cliente.getLname()) && !"null".equals(cliente.getLname());
        boolean doc = StringUtils.isNotBlank(cliente.getDocId()) && !"null".equals(cliente.getDocId());
        boolean phone = StringUtils.isNotBlank(cliente.getPhone()) && !"null".equals(cliente.getPhone());
        boolean instagram = StringUtils.isNotBlank(cliente.getInstagram()) && !"null".equals(cliente.getInstagram());
        boolean email = StringUtils.isNotBlank(cliente.getEmail()) && !"null".equals(cliente.getEmail());

        if (fname && (lname || doc || phone || instagram || email)) return;
        throw  new DatosEntidadInsuficientesException("Se requiere al menos un dato adicional al nombre del cliente");
    }

    private void validateDuplicates(Cliente cliente) {
        List<Cliente> existingClientes = clientesRepository.findByDocOrPhoneOrInstagramOrEmail(cliente.getDocId(),
                cliente.getPhone(), cliente.getInstagram(), cliente.getEmail());

        boolean duplicado = existingClientes.stream().anyMatch(c -> !c.getId().equals(cliente.getId()));
        if (duplicado){
            throw new ClienteDuplicadoException(existingClientes);
        }
    }
}

package com.arquitectojava.letters.api.clientes.services;

import com.arquitectojava.letters.api.exceptions.DatosEntidadInsuficientesException;
import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.clientes.exceptions.ClienteDuplicadoException;
import com.arquitectojava.letters.api.clientes.repositories.ClientesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientesServiceImplTest {

    @InjectMocks
    ClientesServiceImpl clientesService;

    @Mock
    private ClientesRepository clientesRepository;

    @Test
    @DisplayName("Validación de al menos un dato adicional al nombre es requerido")
    public void test_fname_and_other_are_required() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Andrés");

        // SUT and Assert
        Assertions.assertThrows(DatosEntidadInsuficientesException.class, () -> clientesService.save(received));

        //Assert

        //Verify
        verifyNoInteractions(clientesRepository);
    }

    @Test
    @DisplayName("Validación de duplicado por documento")
    public void test_client_already_exists_by_doc() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Mauricio");
        received.setDocId("80818313");

        when(clientesRepository.findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(buildExistingClient()));

        // SUT and Assert
        Assertions.assertThrows(ClienteDuplicadoException.class, () -> clientesService.save(received));

        //Assert

        //Verify
        verify(clientesRepository, times(1)).findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any());
        verify(clientesRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Validación de duplicado por teléfono")
    public void test_client_already_exists_by_phone() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Mauricio");
        received.setPhone("3017549852");

        when(clientesRepository.findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(buildExistingClient()));

        // SUT and Assert
        Assertions.assertThrows(ClienteDuplicadoException.class, () -> clientesService.save(received));

        //Assert

        //Verify
        verify(clientesRepository, times(1)).findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any());
        verify(clientesRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Validación de duplicado por instagram")
    public void test_client_already_exists_by_instagram() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Mauricio");
        received.setInstagram("adrz1co");

        when(clientesRepository.findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(buildExistingClient()));

        // SUT and Assert
        Assertions.assertThrows(ClienteDuplicadoException.class, () -> clientesService.save(received));

        //Assert

        //Verify
        verify(clientesRepository, times(1)).findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any());
        verify(clientesRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Validación de duplicado por email")
    public void test_client_already_exists_by_email() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Mauricio");
        received.setEmail("adrz1@hotmail.com");

        when(clientesRepository.findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any()))
                .thenReturn(Arrays.asList(buildExistingClient()));

        // SUT and Assert
        Assertions.assertThrows(ClienteDuplicadoException.class, () -> clientesService.save(received));

        //Assert

        //Verify
        verify(clientesRepository, times(1)).findByDocOrPhoneOrInstagramOrEmail(any(), any(), any(), any());
        verify(clientesRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Cliente es creado")
    public void test_client_created() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setFname("Andrés");
        received.setLname("Jiménez");

        when(clientesRepository.save(any())).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            if (c.getId() == null) c.setId(1);
            return c;
        });

        // SUT and Assert
        Cliente saved = clientesService.save(received);

        //Assert
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals(received.getFname(), saved.getFname());
        Assertions.assertEquals(received.getLname(), saved.getLname());

        //Verify
        verify(clientesRepository).save(received);
    }

    @Test
    @DisplayName("Cliente es actualizado")
    public void test_client_updated() throws Exception {
        //Behavior
        Cliente received = new Cliente();
        received.setId(1);
        received.setFname("Andrés");
        received.setLname("Jiménez");

        when(clientesRepository.findById(1)).thenReturn(Optional.of(received));
        when(clientesRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // SUT and Assert
        Cliente saved = clientesService.save(received);

        //Assert

        //Verify
        verify(clientesRepository).save(received);
    }

    private Cliente buildExistingClient() {
        Cliente existing = new Cliente();
        existing.setId(1);
        existing.setFname("Andrés");
        existing.setLname("Jiménez");
        existing.setDocId("80818313");
        existing.setPhone("3017549852");
        existing.setInstagram("adrz1.co");
        existing.setEmail("adrz1@hotmail.com");
        return existing;
    }
}
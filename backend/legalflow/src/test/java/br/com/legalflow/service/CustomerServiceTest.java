package br.com.agcapital.taskmanager.service;

import br.com.agcapital.taskmanager.dto.CustomerRequestDTO;
import br.com.agcapital.taskmanager.entity.Customer;
import br.com.agcapital.taskmanager.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerName("Customer 1");
    }

    @Test
    void saveCustomer_newCustomer_shouldSaveCustomer() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setCustomerName("Customer 1");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(dto);

        assertNotNull(savedCustomer);
        assertEquals("Customer 1", savedCustomer.getCustomerName());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void saveCustomer_existingCustomer_shouldUpdateCustomer() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setId(1L);
        dto.setCustomerName("Updated Customer 1");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = customerService.saveCustomer(dto);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Customer 1", updatedCustomer.getCustomerName());

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void findById_existingId_shouldReturnCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.findById(1L);

        assertNotNull(foundCustomer);
        assertEquals(1L, foundCustomer.getId());
        assertEquals("Customer 1", foundCustomer.getCustomerName());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void findById_nonExistingId_shouldReturnNull() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.findById(1L);

        assertNull(foundCustomer);

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void listCustomers_shouldReturnAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setCustomerName("Customer 1");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setCustomerName("Customer 2");

        List<Customer> customers = List.of(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> customerList = customerService.listCustomers();

        assertNotNull(customerList);
        assertEquals(2, customerList.size());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void deleteCustomer_existingId_shouldDeleteCustomer() {
        Long customerId = 1L;

        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }
}

package br.com.legalflow.service;

import br.com.legalflow.dto.CustomerRequestDTO;
import br.com.legalflow.entity.Customer;
import br.com.legalflow.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerRequestDTO dto) {
        if (dto.getId() != null) {
            Customer customer = findById(dto.getId());
            if (customer != null) {
                customer.setCustomerName(dto.getCustomerName());
                return customerRepository.save(customer);
            }
        }
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        return customerRepository.save(customer);
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> listCustomers() {
        return (List<Customer>) customerRepository.findAll();
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

}

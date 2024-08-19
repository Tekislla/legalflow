package br.com.agcapital.taskmanager.repository;

import br.com.agcapital.taskmanager.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}

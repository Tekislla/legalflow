package br.com.legalflow.old.repository;

import br.com.legalflow.old.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}

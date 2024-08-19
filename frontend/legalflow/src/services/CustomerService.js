import axios from "axios";

const CUSTOMERS_API_BASE_URL = "http://localhost:8080/manager/customers";

class CustomerService {
  saveCustomer(customer) {
    return axios.post(CUSTOMERS_API_BASE_URL, customer);
  }

  listCustomers() {
    return axios.get(CUSTOMERS_API_BASE_URL);
  }

  deleteCustomer(customerId) {
    return axios.delete(CUSTOMERS_API_BASE_URL + "/" + customerId);
  }
}

export default new CustomerService();

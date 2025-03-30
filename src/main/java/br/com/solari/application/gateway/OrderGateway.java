package br.com.solari.application.gateway;

import br.com.solari.application.domain.Order;

import java.util.Optional;

public interface OrderGateway {

	Order placeOrder(Order order);

}

package ask.pattern.example.messages;

public class OrderHistory {
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	Order order;
	Address address;

	public OrderHistory(Order inOrder, Address inAddress) {
		order = inOrder;
		address = inAddress;
	}

	@Override
	public String toString(){return order.toString();}
}

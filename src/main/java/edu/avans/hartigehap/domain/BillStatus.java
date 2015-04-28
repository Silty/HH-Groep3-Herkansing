package edu.avans.hartigehap.domain;

public enum BillStatus implements State {
	CREATED (new StatusCreated()), 
	SUBMITTED (new StatusSubmitted()), 
	PAID (new StatusPaid());
	
	private final State state;
	
	BillStatus(State state) {
		this.state = state;
	}
	
	@Override 
	public BillStatus statusCreated(Bill bill) {
		return state.statusCreated(bill);
	}
	
	@Override
	public BillStatus statusPaid(Bill bill) {
		return state.statusPaid(bill);
	}
	
	@Override
	public BillStatus statusSubmitted(Bill bill) {
		return state.statusSubmitted(bill);
	}
}

package edu.avans.hartigehap.domain;

public class StatusPaid implements State {

	@Override 
	public BillStatus statusCreated(Bill bill) {
		// Without exception, so return PAID state
		return BillStatus.PAID;
	}
	
	@Override
	public BillStatus statusPaid(Bill bill) {
		// Without exception, so return PAID state
		return BillStatus.PAID;
	}
	
	@Override
	public BillStatus statusSubmitted(Bill bill) {
		// Without exception, so return PAID state
		return BillStatus.PAID;
	}
}

package edu.avans.hartigehap.domain;

public class StatusSubmitted implements State {

	@Override 
	public BillStatus statusCreated(Bill bill) {
		// Without exception, so return SUBMITTED state
		return BillStatus.SUBMITTED;
	}
	
	@Override
	public BillStatus statusPaid(Bill bill) {
		return BillStatus.PAID;
	}
	
	@Override
	public BillStatus statusSubmitted(Bill bill) {
		// Without exception, so return SUBMITTED state
		return BillStatus.SUBMITTED;
	}
}

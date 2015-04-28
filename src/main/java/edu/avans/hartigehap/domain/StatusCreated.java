package edu.avans.hartigehap.domain;

public class StatusCreated implements State {

	@Override 
	public BillStatus statusCreated(Bill bill) {
		// Without exception, so return CREATED state
		return BillStatus.CREATED;
	}
	
	@Override
	public BillStatus statusPaid(Bill bill) {
		// Without exception, so return CREATED state
		return BillStatus.CREATED;
	}
	
	@Override
	public BillStatus statusSubmitted(Bill bill) {
		return BillStatus.SUBMITTED;
	}
}

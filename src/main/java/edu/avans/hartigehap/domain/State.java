package edu.avans.hartigehap.domain;

public interface State {
	BillStatus statusCreated(Bill bill);
	BillStatus statusPaid(Bill bill);
	BillStatus statusSubmitted(Bill bill);
}

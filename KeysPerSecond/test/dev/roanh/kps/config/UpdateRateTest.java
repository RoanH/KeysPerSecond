package dev.roanh.kps.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UpdateRateTest{

	@Test
	public void testDivisible(){
		for(UpdateRate rate : UpdateRate.values()){
			assertEquals(0, 1000 % rate.getRate());
		}
	}
}

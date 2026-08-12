package it.baral.sec10.assignment.groupby;

import com.github.javafaker.Commerce;
import it.baral.common.Util;

public record PurchaseOrder(String item, String category, Integer price) {
	
	public static PurchaseOrder create() {
		Commerce commerce = Util.faker().commerce();
		return new PurchaseOrder(commerce.productName(), commerce.department(), Util.faker().random().nextInt(10, 100));
	}
}

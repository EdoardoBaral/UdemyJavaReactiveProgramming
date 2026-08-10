package it.baral.sec10.assignment.buffer;

import com.github.javafaker.Book;
import it.baral.common.Util;

public record BookOrder(String genre, String title, Integer price) {
	
	public static BookOrder create() {
		Book book = Util.faker().book();
		return new BookOrder(book.genre(), book.title(), Util.faker().random().nextInt(10, 100));
	}
}

package it.baral.sec01;

import it.baral.sec01.publisher.PublisherImpl;
import it.baral.sec01.subscriber.SubscriberImpl;

import java.time.Duration;

/**
 * Classe dimostrativa della sezione 1 del corso: usa {@link PublisherImpl} e
 * {@link SubscriberImpl} per illustrare i principi fondamentali del
 * protocollo Reactive Streams:
 * <ol>
 *     <li>il publisher non produce dati finche' il subscriber non li richiede;</li>
 *     <li>il publisher produce al massimo tanti elementi quanti richiesti (anche 0);</li>
 *     <li>il subscriber puo' annullare la sottoscrizione, interrompendo la produzione;</li>
 *     <li>il publisher puo' notificare un segnale di errore.</li>
 * </ol>
 */
public class Demo {

	/**
	 * Esegue in sequenza le quattro demo che illustrano il comportamento del
	 * protocollo Reactive Streams (richiesta, cancellazione, errore).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 * @throws Exception se l'attesa tra le richieste viene interrotta
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(">>> DEMO 1: No request from subscriber");
		demo1();
		System.out.println();
		
		System.out.println(">>> DEMO 2: Subscriber requests data from publisher");
		demo2();
		System.out.println();
		
		System.out.println(">>> DEMO 3: Subscriber cancels subscription");
		demo3();
		System.out.println();
		
		System.out.println(">>> DEMO 4: Error when publisher sends data requested by subscriber");
		demo4();
		System.out.println();
	}
	
	/**
	 * Dimostra che il publisher non produce alcun dato se il subscriber non
	 * effettua richieste esplicite.
	 */
	private static void demo1() {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
	}

	/**
	 * Dimostra che il publisher produce dati man mano che il subscriber li
	 * richiede, con piu' richieste successive nel tempo.
	 *
	 * @throws Exception se l'attesa tra le richieste viene interrotta
	 */
	private static void demo2() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
	
	/**
	 * Dimostra che, una volta annullata la sottoscrizione, il publisher non
	 * produce piu' alcun dato anche a fronte di ulteriori richieste.
	 *
	 * @throws Exception se l'attesa tra le richieste viene interrotta
	 */
	private static void demo3() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().cancel();
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
	
	/**
	 * Dimostra che, se il subscriber richiede piu' elementi di quanti il
	 * publisher possa produrne (oltre {@code MAX_ITEMS}), il publisher
	 * notifica un segnale di errore e interrompe la produzione.
	 *
	 * @throws Exception se l'attesa tra le richieste viene interrotta
	 */
	private static void demo4() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(20);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
}

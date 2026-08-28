package it.baral.sec10.assignment.buffer;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Rappresenta un report periodico dei ricavi generato dall'esercizio sull'operatore
 * {@code buffer} (sezione sec10), aggregando i ricavi per genere di libro.
 *
 * @param time istante di generazione del report
 * @param revenue mappa che associa il genere del libro al ricavo totale registrato nel periodo
 */
public record RevenueReport(LocalDateTime time, Map<String, Integer> revenue) {
}

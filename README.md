# Mastering Java Reactive Programming — Note del corso

Questo repository raccoglie il codice sviluppato seguendo il corso Udemy **"Mastering Java Reactive Programming [From Scratch]"** (Vinoth Selvaraj), incentrato sulla programmazione reattiva in Java tramite [Project Reactor](https://projectreactor.io/).

Il codice è organizzato in un package per ogni sezione del corso (`it.baral.sec01`, `it.baral.sec02`, ...); ogni package corrisponde a una lezione su uno specifico argomento e contiene classi dimostrative eseguibili (metodi `main`), i relativi test JUnit 5 (con `StepVerifier` di `reactor-test`) e, dove presente, un sottopackage `assignment` con gli esercizi proposti a fine lezione.

## Indice

- [Introduzione](#introduzione)
- [Prerequisiti](#prerequisiti)
- [Package comune (`it.baral.common`)](#package-comune-itbaralcommon)
- [sec01 — Il protocollo Reactive Streams da zero](#sec01--il-protocollo-reactive-streams-da-zero)
- [sec02 — Creazione di Mono e I/O reattivo non bloccante](#sec02--creazione-di-mono-e-io-reattivo-non-bloccante)
- [sec03 — Flux e sorgenti dati multiple](#sec03--flux-e-sorgenti-dati-multiple)
- [sec04 — Creazione avanzata di Flux: create e generate](#sec04--creazione-avanzata-di-flux-create-e-generate)
- [sec05 — Gestione di errori, timeout ed empty-flow](#sec05--gestione-di-errori-timeout-ed-empty-flow)
- [sec06 — Publisher cold vs hot e condivisione di flussi](#sec06--publisher-cold-vs-hot-e-condivisione-di-flussi)
- [sec07 — Scheduling: subscribeOn, publishOn, parallelismo, virtual thread](#sec07--scheduling-subscribeon-publishon-parallelismo-virtual-thread)
- [sec08 — Backpressure e controllo del flusso](#sec08--backpressure-e-controllo-del-flusso)
- [sec09 — Combinazione e trasformazione asincrona di flussi](#sec09--combinazione-e-trasformazione-asincrona-di-flussi)
- [sec10 — Suddivisione di flussi in gruppi: buffer, window, groupBy](#sec10--suddivisione-di-flussi-in-gruppi-buffer-window-groupby)
- [sec11 — Resilienza: retry e repeat verso servizi esterni](#sec11--resilienza-retry-e-repeat-verso-servizi-esterni)
- [sec12 — Sinks: produzione imperativa di segnali reattivi](#sec12--sinks-produzione-imperativa-di-segnali-reattivi)
- [sec13 — Context reattivo e rate limiting](#sec13--context-reattivo-e-rate-limiting)

---

## Introduzione

Il corso parte dai principi del protocollo **Reactive Streams** (implementati a mano, senza librerie) per poi passare a **Project Reactor**, la libreria reattiva usata come riferimento (alla base anche di Spring WebFlux). Gli argomenti trattati, lezione dopo lezione, sono:

- i quattro principi del protocollo Reactive Streams (`Publisher`, `Subscriber`, `Subscription`, backpressure via `request(n)`);
- i due tipi fondamentali di Reactor, **`Mono`** (0..1 elemento) e **`Flux`** (0..N elementi), e i loro numerosi metodi factory;
- l'uso di `Mono`/`Flux` per rendere non bloccante l'I/O (file, chiamate HTTP);
- la creazione avanzata di flussi push (`Flux.create`) e pull (`Flux.generate`), con relativa gestione dello stato e della thread-safety;
- la gestione degli errori, dei timeout e dei flussi vuoti (`onError*`, `timeout`, `switchIfEmpty`, `handle`);
- la differenza fra publisher **cold** e **hot** e gli operatori per condividere una sorgente (`share`, `publish`, `replay`);
- il modello di concorrenza di Reactor: `subscribeOn`, `publishOn`, `parallel`, scheduler e virtual thread;
- il meccanismo di **backpressure** e le strategie per gestirlo (`onBackpressureBuffer/Drop/Latest/Error`, `limitRate`);
- la combinazione di più flussi asincroni (`flatMap`, `concatMap`, `merge`, `zip`, `startWith`, `then`);
- il raggruppamento di flussi (`buffer`, `window`, `groupBy`);
- pattern di resilienza verso servizi esterni (`retry`, `repeat`);
- la produzione imperativa di segnali con l'API **`Sinks`** (unicast/multicast/replay) e la relativa thread-safety;
- il **`Context`** reattivo per propagare informazioni contestuali lungo la catena di operatori.

Ogni lezione è verificata sia con demo eseguibili (log su console) sia con test automatici basati su `StepVerifier`, spesso con `withVirtualTime()` per simulare il passare del tempo senza attese reali.

## Prerequisiti

### Java e build tool

- **JDK 21** (il progetto usa esplicitamente feature/API di Java 21, incluso il modello dei *virtual thread* in [sec07](#sec07--scheduling-subscribeon-publishon-parallelismo-virtual-thread)). Il [pom.xml](pom.xml) fissa `<java.version>21</java.version>` e configura `maven-compiler-plugin` con `source`/`target` a 21.
- **Maven** come build tool (nessun `mvnw` incluso: serve un'installazione di Maven locale). Comandi principali:

```bash
mvn compile
mvn test
```

### Dipendenze del POM e a cosa servono

| Dipendenza | Scopo |
|---|---|
| `io.projectreactor:reactor-core` (via `reactor-bom`, versione `2025.0.4`) | Libreria reattiva core: `Mono`, `Flux`, `Sinks`, `Context`, `Schedulers`. |
| `io.projectreactor.netty:reactor-netty-core` / `reactor-netty-http` | Client HTTP reattivo e non bloccante, usato da `it.baral.common.AbstractHttpClient` in tutte le lezioni che interrogano il servizio esterno di esempio. |
| `ch.qos.logback:logback-classic` | Implementazione di logging (backend di SLF4J), configurata in [`src/main/resources/logback.xml`](src/main/resources/logback.xml) per stampare su console con thread e logger nel formato dei log. |
| `com.github.javafaker:javafaker` | Generazione di dati fittizi (nomi, email, testi...) usata pervasivamente nelle demo per popolare flussi di esempio. |
| `org.projectlombok:lombok` | Riduzione del boilerplate (`@Getter`, `@RequiredArgsConstructor`, ecc.) nelle classi di supporto. |
| `org.springframework:spring-context` + `jakarta.annotation:jakarta.annotation-api` | Presenti nel POM ma non ancora utilizzate dal codice delle sezioni attuali: dipendenze predisposte per lezioni successive del corso su dependency injection. |
| `org.junit.jupiter:junit-jupiter-engine` *(test)* | Motore di esecuzione dei test JUnit 5. |
| `io.projectreactor:reactor-test` *(test)* | Fornisce `StepVerifier`, usato in tutti i test per asserire in modo dichiarativo la sequenza di segnali emessi da `Mono`/`Flux` (incluso il testing a tempo virtuale). |

### Servizio esterno mock (`external-services.jar`)

Diverse lezioni (sec02, sec03, sec05, sec06, sec07, sec09, sec11, sec13) interrogano via HTTP un servizio esterno di esempio, fornito dal corso come applicazione Spring Boot già impacchettata: [`external-services.jar`](external-services.jar), nella root del progetto (`Start-Class: com.vinsguru.externalservices.ExternalServicesApplication`).

Per eseguirlo serve un JDK (21+) disponibile a riga di comando:

```bash
java -jar external-services.jar
```

Il servizio si avvia sulla porta **7070** (`http://localhost:7070`, la stessa configurata in `it.baral.common.AbstractHttpClient`) ed espone anche una Swagger UI di consultazione degli endpoint su `http://localhost:7070/swagger-ui.html`.

> Il servizio esterno serve solo per eseguire le **demo `main`** che effettuano chiamate HTTP reali. I **test automatici** (`StepVerifier`) delle stesse lezioni non richiedono il servizio avviato: usano stub/sorgenti locali equivalenti.

## Package comune (`it.baral.common`)

Contiene le utility riusate trasversalmente da tutte le lezioni, non legate a un argomento specifico:

- **`AbstractHttpClient.java`** — classe base per i client HTTP di esempio: incapsula un `HttpClient` di Reactor Netty configurato con un `LoopResources` dedicato e l'URL base del servizio esterno (`http://localhost:7070`).
- **`Util.java`** — factory per `DefaultSubscriber`, accesso all'istanza condivisa di `Faker`, metodi di sleep bloccante per le demo, e `fluxLogger(String)` per loggare in modo uniforme sottoscrizione/cancellazione/completamento di un `Flux`.
- **`DefaultSubscriber.java`** — `Subscriber` generico riutilizzabile che richiede subito `Long.MAX_VALUE` elementi (nessun controllo di backpressure) e logga ogni evento ricevuto.

---

## sec01 — Il protocollo Reactive Streams da zero

**Argomento della lezione**: prima di introdurre Project Reactor, il corso fa implementare manualmente (senza alcuna libreria reattiva) le interfacce del protocollo [Reactive Streams](https://www.reactive-streams.org/) — `Publisher`, `Subscriber`, `Subscription` — per far toccare con mano i quattro principi fondamentali: (1) il publisher non produce nulla finché non richiesto; (2) produce al più quanto richiesto; (3) il subscriber può cancellare la sottoscrizione in ogni momento; (4) il publisher può notificare un errore.

**Costrutti usati**: `org.reactivestreams.Publisher`, `Subscriber`, `Subscription` (le interfacce nude della specifica, non ancora l'API di Reactor).

**Concetti dimostrati**: richiesta esplicita di elementi (`request(n)`), cancellazione (`cancel()`), limite massimo di produzione con conseguente `onComplete`, errore per richiesta eccessiva (`onError`).

**Classi**:
- `Demo.java` — esegue in sequenza quattro scenari: nessuna richiesta, richieste multiple nel tempo, cancellazione, richiesta eccessiva che genera un errore.
- `publisher/PublisherImpl.java` — implementazione manuale di `Publisher<String>`: ad ogni sottoscrizione crea una nuova `SubscriptionImpl` dedicata.
- `publisher/SubscriptionImpl.java` — implementazione manuale di `Subscription`: produce fino a 10 indirizzi email fittizi (JavaFaker), solo nella quantità richiesta ad ogni `request(n)`, e fallisce se la richiesta supera il limite.
- `subscriber/SubscriberImpl.java` — implementazione manuale di `Subscriber<String>` che logga elementi, errori e completamento.

**Note particolari**: nessuna dipendenza esterna richiesta; è la base concettuale propedeutica a tutte le sezioni successive, dove le stesse responsabilità saranno gestite automaticamente da Reactor.

## sec02 — Creazione di Mono e I/O reattivo non bloccante

**Argomento della lezione**: introduce il tipo `Mono` (publisher reattivo di 0 o 1 elemento) e le sue principali modalità di creazione — valore immediato, vuoto, errore, wrapping di codice bloccante (`Supplier`, `Callable`, `Runnable`), adattamento di una `CompletableFuture` esistente, creazione differita con `defer`. Nella seconda parte il focus si sposta sull'applicazione pratica: incapsulare I/O potenzialmente bloccante (file system, chiamate HTTP) in un `Mono` per ottenere I/O non bloccante, confrontando esplicitamente l'approccio bloccante (`block()`) con quello reattivo (`subscribe()`).

**Costrutti Reactor usati**: `Mono`, `Subscriber`/`Subscription` custom (da `it.baral.common`).

**Operatori Reactor studiati**:
- `Mono.just(Object)` — emette subito il valore fornito e completa.
- `Mono.empty()` — completa immediatamente senza emettere alcun valore.
- `Mono.error(Throwable)` — termina subito propagando l'errore.
- `Mono.fromSupplier(Supplier)` — esegue in modo lazy (alla sottoscrizione) un `Supplier`, propagando le `RuntimeException` come errore.
- `Mono.fromCallable(Callable)` — come `fromSupplier` ma accetta anche eccezioni **checked**, propagate come segnale di errore.
- `Mono.fromRunnable(Runnable)` — esegue un'azione con solo effetto collaterale, senza produrre un valore.
- `Mono.fromFuture(CompletableFuture)` — adatta una `CompletableFuture` già esistente a un `Mono`.
- `Mono.defer(Supplier<Mono>)` — rimanda la costruzione del `Mono` al momento della sottoscrizione, rieseguendo il supplier per ogni subscriber.
- `map` — trasformazione, usata anche per mostrare la propagazione di eccezioni come errore.
- `subscribe(...)` — nelle sue varianti (solo consumer, +errore, +completamento, gestione manuale della `Subscription`).

**Classi**:
- `LazyStream.java` — confronta (senza Reactor) la valutazione lazy degli Stream Java, come premessa concettuale alla lazyness reattiva.
- `MonoJust.java` — comportamento base di `Mono.just`.
- `MonoSubscribe.java` — le diverse overload di `subscribe`, inclusa la propagazione di un errore sollevato in `map`.
- `DemoDefaultSubscriber.java` — sottoscrizioni multiple con `Util.subscriber(name)` per distinguerle nei log.
- `MonoEmptyError.java` — i tre esiti possibili di un `Mono` (valore, `empty()`, `error()`).
- `MonoFromSupplier.java` / `MonoFromCallable.java` — stesso calcolo bloccante incapsulato con `fromSupplier` e `fromCallable`, per evidenziarne la differenza sulla gestione delle eccezioni checked.
- `MonoFromRunnable.java` — notifica un effetto collaterale senza emettere un valore.
- `MonoFromFuture.java` — adatta una `CompletableFuture` asincrona a `Mono`.
- `MonoDefer.java` — mostra come `defer` posticipi la creazione (costosa) del publisher alla sottoscrizione.
- `client/ExternalServiceClient.java` — client HTTP (`AbstractHttpClient`) che espone `getProductName(int)` interrogando `/demo01/product/{id}`.
- `NonBlockingIO.java` — confronta 50 chiamate HTTP bloccanti (`block()`) con la versione non bloccante (`subscribe()`).
- `FileService.java` / `FileServiceImpl.java` — contratto reattivo (`read`/`write`/`delete`) e relativa implementazione basata su `java.nio.file.Files`, con le chiamate bloccanti incapsulate in `fromCallable`/`fromRunnable`.
- `FileServiceAssignment.java` — esercizio che scrive, legge ed elimina 5 file di esempio in `src/main/resources/sec02`.

**Note particolari**: `NonBlockingIO` e `client/ExternalServiceClient` richiedono il [servizio esterno](#servizio-esterno-mock-external-servicesjar) in ascolto su `localhost:7070`. `FileServiceImpl`/`FileServiceAssignment` operano su file reali in `src/main/resources/sec02`. Pattern architetturale interfaccia + implementazione + assignment, ricorrente in tutto il corso.

## sec03 — Flux e sorgenti dati multiple

**Argomento della lezione**: introduce il tipo `Flux` (publisher reattivo di 0..N elementi) e i vari modi per costruirlo (valori letterali, iterabili/array, Stream Java, range numerici, `interval` periodico), la relazione fra `Mono` e `Flux`, la natura **cold** dei Flux qui creati (ogni subscriber riesegue l'intera sequenza in modo indipendente) e l'uso di `log()` per il debug dei segnali. Chiude con un confronto pratico eager (List) vs reattivo (Flux) e un esercizio di trading su uno stream di prezzi in tempo reale.

**Costrutti Reactor usati**: `Flux`, `Mono`, `Subscriber`/`Subscription` custom.

**Operatori Reactor studiati**:
- `Flux.just(Object...)` — emette in sequenza i valori forniti e completa.
- `Flux.fromIterable(Iterable)` / `Flux.fromArray(Object[])` — creano un `Flux` da una collezione o array esistenti.
- `Flux.fromStream(Stream)` — crea un `Flux` da uno Stream Java (consumabile una sola volta: va ricreato per ogni sottoscrizione).
- `Flux.range(int, int)` — genera una sequenza di interi consecutivi.
- `Flux.interval(Duration)` — emette un long incrementale a intervalli regolari.
- `Flux.empty()` / `Flux.error(Throwable)` — completamento vuoto immediato / terminazione immediata in errore.
- `Flux.defer(Supplier<Flux>)` — equivalente di `Mono.defer` per i Flux.
- `Flux.from(Publisher)` / `.next()` — conversione Mono→Flux e Flux→Mono (primo elemento).
- `filter`, `map` — trasformazioni standard.
- `log()` / `log(String categoria)` — logga i segnali (subscribe, request, onNext, onComplete) attraversati dalla catena.

**Classi**:
- `FluxJust.java` — comportamento base di `Flux.just`.
- `MultipleSubscribers.java` — un Flux "freddo" riesegue l'intera emissione per ogni sottoscrizione, anche con catene di operatori diverse.
- `FluxFromIterable.java` — creazione da `List` e da array.
- `FluxFromStream.java` — creazione da `Stream`, con l'errore da riutilizzo di uno stream già consumato e la relativa soluzione.
- `FluxFromRange.java` — creazione da `range`, con mapping opzionale a dati fittizi.
- `Log.java` — l'operatore `log()`/`log(categoria)` su una catena con `map`.
- `helper/NameGenerator.java` — genera nomi fittizi in modo eager (List, con attesa simulata) o lazy (Flux), per il confronto in `FluxVSList`.
- `NonBlockingStreamingMessages.java` — due sottoscrizioni concorrenti indipendenti a uno stream di nomi via HTTP.
- `FluxInterval.java` — `Flux.interval(500ms)` mappato a nomi fittizi ad ogni tick.
- `FluxEmptyError.java` — `Flux.empty()` e `Flux.error()`.
- `FluxDefer.java` — confronta `Flux.fromIterable` diretto con `Flux.defer(...)`.
- `MonoFlux.java` — conversione Mono↔Flux.
- `client/ExternalServiceClient.java` — client HTTP con due stream continui: nomi (`/demo02/name/stream`) e variazioni di prezzo (`/demo02/stock/stream`, mappato a `Integer`).
- `assignment/StockPriceObserver.java` — `Subscriber<Integer>` custom con strategia di trading (compra sotto 90, vende sopra 110 e annulla la sottoscrizione).
- `assignment/StockAssignment.java` — collega lo stream di prezzi allo `StockPriceObserver`.
- `FluxVSList.java` — confronta generazione eager (List, bloccante) vs reattiva (Flux) di 10 nomi fittizi.

**Note particolari**: `NonBlockingStreamingMessages`, `client/ExternalServiceClient` e `StockAssignment` richiedono il servizio esterno in streaming su `localhost:7070`. I test di `FluxInterval` usano `StepVerifier.withVirtualTime` per non attendere realmente gli intervalli.

## sec04 — Creazione avanzata di Flux: create e generate

**Argomento della lezione**: approfondisce due modalità avanzate di creazione di `Flux` per scenari push (`Flux.create`) e pull sincroni (`Flux.generate`), inclusa la gestione dello stato tra invocazioni successive, la thread-safety del `FluxSink`, il rispetto (o meno) della domanda a valle, e gli operatori di troncamento (`take`, `takeWhile`, `takeUntil`). Chiude con un esercizio che applica `Flux.generate` con stato alla lettura riga per riga di un file.

**Costrutti Reactor usati**: `Flux`, `FluxSink`, `SynchronousSink`, `Subscription` custom.

**Operatori Reactor studiati**:
- `Flux.create(Consumer<FluxSink>)` — genera eventi in modalità push, permettendo emissione manuale/asincrona (`next`, `complete`, `error`) anche da thread esterni.
- `FluxSink.onRequest(LongConsumer)` — callback per produrre valori solo in risposta alla domanda effettiva a valle.
- `FluxSink.isCancelled()` — permette di interrompere la produzione se il subscriber ha cancellato.
- `Flux.generate(Consumer<SynchronousSink>)` — genera un valore per volta in modo sincrono e pull-based; senza `complete()` prosegue indefinitamente.
- `Flux.generate(Supplier<S>, BiFunction<S, SynchronousSink, S>)` — variante con stato mutabile passato/restituito ad ogni invocazione (es. contatori, risorse aperte).
- `Flux.generate(Supplier, BiFunction, Consumer<S>)` — variante a tre argomenti con callback di cleanup dello stato a fine emissione.
- `take(n)` / `takeWhile(Predicate)` / `takeUntil(Predicate)` — troncano il flusso rispettivamente dopo n elementi, finché il predicato è vero (escluso l'elemento che lo fa fallire), finché il predicato non si verifica (incluso l'elemento che lo soddisfa).
- `log(String)` — osservazione dei segnali nelle demo di `take`/`takeWhile`/`takeUntil`.

**Classi**:
- `FluxCreate.java` — tre esempi di `Flux.create`: valori fissi, generazione casuale in loop, loop che termina a condizione.
- `FluxCreateRefactor.java` — estrae la generazione in `helper/NameGenerator.java`, `Consumer<FluxSink<String>>` che mantiene il riferimento al sink per emissioni successive su comando esterno.
- `FluxSinkThreadSafety.java` — confronta l'accesso concorrente non sincronizzato a una `ArrayList` (perdita di elementi da più thread) con `FluxSink`, che serializza automaticamente le emissioni multi-thread.
- `FluxCreateDownstreamDemand.java` — confronta produzione rispettosa della domanda (`onRequest` + `isCancelled()`) con produzione che ignora il backpressure a valle.
- `TakeOperator.java` — confronta `take(3)`, `takeWhile(i<6)`, `takeUntil(i==6)` su `Flux.range(1,10)`.
- `FluxGenerate.java` — loop infinito senza `complete()`, limitazione con `take(4)` a valle, emissione singola con `complete()` esplicito.
- `FluxGenerateUntil.java` — confronta terminazione gestita nel generatore vs delegata a `takeUntil` a valle.
- `FluxGenerateWithState.java` — `Flux.generate` con stato (contatore) che termina sia per condizione sul valore sia per numero massimo di elementi.
- `assignment/FileReaderService.java` / `FileReaderServiceImpl.java` — lettura reattiva riga per riga di un file con `Flux.generate` a tre argomenti (apre `BufferedReader` come stato, legge una riga per invocazione, chiude il reader a fine flusso).
- `assignment/Assignment.java` — legge `src/main/resources/sec04/file.txt` riga per riga tramite `FileReaderServiceImpl`.

**Note particolari**: `FluxSinkThreadSafety` usa esplicitamente i *thread virtuali* di Java 21 (`Thread.ofPlatform()`/virtual) per dimostrare la serializzazione interna del `FluxSink`. `FluxCreateDownstreamDemand` è l'unico punto dove si osserva la gestione manuale della domanda (`Subscription.request(n)`/`cancel()`) combinata con `onRequest`. `FileReaderServiceImpl` dipende dal file reale `src/main/resources/sec04/file.txt`.

## sec05 — Gestione di errori, timeout ed empty-flow

**Argomento della lezione**: approfondisce gli operatori Reactor per gestire condizioni "anomale" di un flusso: assenza di elementi, errori a runtime, timeout, oltre a callback di side-effect e composizione riutilizzabile di catene di operatori. Un assignment finale integra `timeout` e `switchIfEmpty` in un client HTTP con fallback a cascata.

**Costrutti Reactor usati**: `Mono`, `Flux`, `SynchronousSink`, Reactor Netty `HttpClient`.

**Operatori Reactor studiati**:
- `defaultIfEmpty` — fornisce un valore di fallback singolo quando il Flux a monte completa vuoto.
- `switchIfEmpty` — sostituisce l'intero Flux a monte con un Publisher di fallback in caso di flusso vuoto.
- `timeout` (semplice, con Mono di fallback, e in cascata) — fa scattare un errore/fallback se non arriva un elemento entro una durata data.
- `onErrorReturn` (con/senza filtro sul tipo di eccezione) — sostituisce l'errore con un valore fisso.
- `onErrorResume` (con/senza filtro sul tipo) — sostituisce l'errore con un Publisher di fallback.
- `onErrorComplete` — trasforma un errore in completamento silenzioso.
- `onErrorContinue` — scarta l'elemento che ha causato l'errore e prosegue con i successivi.
- `handle` — trasforma/filtra/termina elemento per elemento tramite `SynchronousSink` esplicito.
- `transform` — applica in modo condizionale, a tempo di composizione, una catena di operatori riutilizzabile.
- `delayElements` — ritarda l'emissione di ogni elemento di una durata fissa.
- callback `do*` (`doOnNext`, `doOnComplete`, `doOnError`, `doOnSubscribe`, `doOnRequest`, `doOnTerminate`, `doOnCancel`, `doOnDiscard`, `doFirst`, `doFinally`) — side-effect osservativi senza alterare il flusso.

**Classi**:
- `DefaultIfEmpty.java` — `defaultIfEmpty` su Flux vuoto vs Flux con elementi.
- `Delay.java` — `delayElements` per distanziare temporalmente le emissioni.
- `DoCallbacks.java` — callback `do*` in punti diversi della catena, per confrontarne l'ordine di invocazione.
- `ErrorHandling.java` — confronta `onErrorReturn`, `onErrorResume`, `onErrorComplete`, `onErrorContinue`.
- `Handle.java` — `handle` per trasformare/scartare/far fallire elementi, incluso un caso di generazione infinita di paesi casuali interrotta al valore "Canada".
- `Subscribe.java` — `subscribe()` senza argomenti in combinazione con i callback `do*`.
- `SwitchIfEmpty.java` — `switchIfEmpty` su Flux vuoto vs Flux con elementi.
- `Timeout.java` — quattro scenari di `timeout` (nessun timeout, timeout con errore, con fallback, cascata di timeout multipli).
- `Transform.java` — `transform` per applicare condizionalmente un "debugger" riutilizzabile a Flux di record diversi.
- `assignment/ExternalServiceClient.java` — combina `timeout` (con Mono di fallback) e `switchIfEmpty` per recuperare il nome di un prodotto da un servizio primario con doppio fallback.
- `assignment/Assignment.java` — invoca il client per tre prodotti che innescano rispettivamente il percorso primario, il fallback per risposta vuota e quello per timeout.

**Note particolari**: `assignment/ExternalServiceClient` richiede il [servizio esterno](#servizio-esterno-mock-external-servicesjar) (endpoint `/demo03/product/{id}`, `/demo03/timeout-fallback/product/{id}`, `/demo03/empty-fallback/product/{id}`). `TimeoutTest` usa `StepVerifier.withVirtualTime` per non attendere realmente i timeout.

## sec06 — Publisher cold vs hot e condivisione di flussi

**Argomento della lezione**: spiega la differenza tra publisher **cold** (ogni sottoscrizione riesegue la sorgente da capo) e **hot** (la sorgente è condivisa tra i sottoscrittori), esplorando gli operatori che convertono un Flux cold in hot con diverse semantiche di connessione e replay. Un assignment finale applica questi concetti a uno scenario realistico di elaborazione ordini condivisa tra due servizi indipendenti.

**Costrutti Reactor usati**: `Flux`, `ConnectableFlux` (`publish()`), Reactor Netty `HttpClient`.

**Operatori Reactor studiati**:
- `share()` — rende hot un Flux, condividendo un'unica sottoscrizione a monte tra tutti i subscriber, senza replay per i tardivi.
- `publish().autoConnect(0)` — variante hot che avvia l'emissione immediatamente (zero subscriber richiesti per connettersi).
- `publish().refCount(n)` — rende hot un Flux subordinando l'avvio dell'emissione alla presenza di almeno `n` sottoscrittori attivi.
- `replay(n).autoConnect(0)` — mantiene in cache gli ultimi `n` elementi e li ritrasmette ai sottoscrittori tardivi.

**Classi**:
- `ColdPublisher.java` — ogni sottoscrizione a un `Flux.create` innesca una nuova esecuzione indipendente della sorgente.
- `FluxCreateIssueFix.java` — usa `share()` per condividere i valori tra più sottoscrittori di un Flux.create basato su generazione manuale.
- `HotPublisher.java` — `share()` su uno stream di "scene di film": un sottoscrittore tardivo perde gli elementi già emessi.
- `HotPublisherAutoConnect.java` — confronta `publish().autoConnect(0)` con `share()`.
- `HotPublisherCache.java` — `replay(10).autoConnect(0)` su uno stream di prezzi azionari: i sottoscrittori tardivi recuperano dalla cache.
- `assignment/ExternalServiceClient.java` — stream HTTP di ordini reso hot con `publish().refCount(2)`.
- `assignment/Order.java` — record di un ordine (categoria, prezzo, quantità).
- `assignment/OrderProcessor.java` — interfaccia comune per i servizi che elaborano ordini ed espongono il proprio stato aggregato.
- `assignment/InventoryService.java` — traccia la giacenza residua per categoria.
- `assignment/RevenueService.java` — accumula i ricavi totali per categoria.
- `assignment/Assignment.java` — collega lo stream hot di ordini a `RevenueService` e `InventoryService`, osservandone lo stato in tempo reale.

**Note particolari**: `assignment/ExternalServiceClient` richiede il servizio esterno (endpoint `/demo04/orders/stream`). Package cardine della distinzione hot/cold del corso. `FluxCreateIssueFix` riusa `it.baral.sec04.helper.NameGenerator`. I test usano `StepVerifier.withVirtualTime` per simulare il tempo senza attese reali.

## sec07 — Scheduling: subscribeOn, publishOn, parallelismo, virtual thread

**Argomento della lezione**: dedicata al modello di concorrenza di Reactor: la differenza tra `subscribeOn` (sposta l'esecuzione della sorgente) e `publishOn` (sposta l'esecuzione degli operatori a valle), il comportamento di default senza scheduler espliciti, l'elaborazione parallela con `parallel()`/`runOn()`, e l'uso dei *virtual thread* Java come backing per gli scheduler Reactor. Include anche un caso pratico di correzione del blocco dell'event loop Netty in un client HTTP.

**Costrutti Reactor usati**: `Flux`, `Mono`, `Scheduler`/`Schedulers` (`boundedElastic`, `parallel`, `immediate`, `newParallel`, `newSingle`, `fromExecutorService`), `ParallelFlux`, Reactor Netty `HttpClient`.

**Operatori Reactor studiati**:
- `subscribeOn(Scheduler)` — sposta l'esecuzione della sorgente sullo scheduler indicato, indipendentemente dalla posizione nella catena; con più occorrenze vince la prima (più vicina alla sorgente).
- `publishOn(Scheduler)` — sposta l'esecuzione degli operatori a valle dal punto in cui è dichiarato; con più occorrenze ogni chiamata sposta ulteriormente gli operatori successivi.
- `parallel(n)` / `runOn(Scheduler)` / `sequential()` — suddivide un Flux in `n` "rail" paralleli, li elabora concorrentemente e ricongiunge i risultati in un unico flusso sequenziale.
- `doFirst` — marcatore di logging per osservare l'ordine/il thread di esecuzione delle fasi della catena.

**Classi**:
- `client/ExternalServiceClient.java` — usa `publishOn(Schedulers.boundedElastic())` per non bloccare l'event loop Netty.
- `DefaultBehaviorDemo.java` — senza scheduler espliciti ogni `subscribe()` viene eseguito interamente sul thread chiamante.
- `EventLoopIssueFix.java` — correzione del blocco dell'event loop tramite `publishOn(boundedElastic)`.
- `MultipleSubscribeOn.java` — con più `subscribeOn` in catena vince sempre il primo applicato.
- `Parallel.java` — elaborazione parallela con `parallel(4)`/`runOn(Schedulers.parallel())`/`sequential()`.
- `PublishOn.java` — due `publishOn` in sequenza, ciascuno sposta gli operatori successivi sul proprio scheduler.
- `SubscribeOn.java` — `subscribeOn(boundedElastic)` sottoscritto da thread differenti.
- `SubscribeOnPublishOn.java` — combina `publishOn` e `subscribeOn` nella stessa catena.
- `VirtualThreads.java` — abilita i virtual thread come backing di `boundedElastic` tramite la property `reactor.schedulers.defaultBoundedElasticOnVirtualThreads`.

**Note particolari**: `client/ExternalServiceClient` richiede il servizio esterno (endpoint `/demo01/product/{id}`). I test verificano anche il nome/tipo del thread di esecuzione (`assertTrue(name.startsWith("parallel-"))`, `Thread.currentThread().isVirtual()`), confrontando `Executors.newVirtualThreadPerTaskExecutor()` con il `boundedElastic` di default (che non usa virtual thread se la property non è abilitata).

## sec08 — Backpressure e controllo del flusso

**Argomento della lezione**: tratta il meccanismo di backpressure di Reactor (protocollo request-n della specifica Reactive Streams), mostrando come interagiscono un produttore veloce e un consumatore lento, quali strategie sono disponibili quando il produttore supera la domanda, e come limitare esplicitamente la richiesta con `limitRate`. Chiude con un confronto tra più sottoscrittori indipendenti su un Flux cold con velocità di consumo diverse.

**Costrutti Reactor usati**: `Flux`, `FluxSink`/`FluxSink.OverflowStrategy`, `Schedulers` (`parallel`, `boundedElastic`).

**Operatori Reactor studiati**:
- `onBackpressureBuffer()` / `onBackpressureBuffer(n)` — bufferizza (illimitatamente o fino a una dimensione fissa) gli elementi in eccesso; oltre la capacità fissa il flusso termina con errore di overflow.
- `onBackpressureError()` — termina il flusso con un errore non appena il produttore supera la domanda.
- `onBackpressureDrop()` — scarta silenziosamente gli elementi in eccesso non richiesti.
- `onBackpressureLatest()` — mantiene solo l'ultimo elemento emesso, scartando i precedenti non richiesti.
- `Flux.create(consumer, FluxSink.OverflowStrategy.BUFFER)` — imposta la strategia di overflow direttamente in fase di creazione.
- `limitRate(n)` — suddivide la richiesta (anche illimitata) del subscriber in lotti di dimensione `n` verso la sorgente.
- `doOnRequest` — osserva le quantità richieste alla sorgente.

**Classi**:
- `BackPressureHandling.java` — meccanismo di backpressure di default (buffer di richiesta ridotto via property `reactor.bufferSize.small`) tra produttore veloce (`Flux.generate`) e consumatore lento.
- `BackPressureStrategies.java` — confronta tutte le strategie `onBackpressureXxx` e l'`OverflowStrategy` impostata a tempo di creazione.
- `FluxCreate.java` — generazione a raffica (fino a 500 elementi) e consumo lento, per osservare l'effetto della richiesta senza strategia esplicita.
- `LimitRate.java` — `limitRate(5)` su un produttore infinito, per mostrare le richieste a piccoli lotti.
- `MultipleSubscribers.java` — due subscriber indipendenti sullo stesso Flux cold generato, con velocità di richiesta diverse.

**Note particolari**: non richiede il servizio esterno — tutte le demo si basano su `Flux.generate`/`Flux.create` sintetici, con `Schedulers.parallel()` per la produzione e `Schedulers.boundedElastic()` per il consumo lento simulato. I test usano `StepVerifier.create(flux, 0)` con richiesta iniziale zero e `thenRequest(n)` per pilotare manualmente la domanda e verificare deterministicamente ogni strategia.

## sec09 — Combinazione e trasformazione asincrona di flussi

**Argomento della lezione**: copre gli operatori che compongono più `Publisher`: trasformazione asincrona "appiattente" (`flatMap`/`concatMap`/`flatMapMany`), unione di sorgenti multiple (`merge`, `zip`, `concatWith`, `startWith`), sequenziamento di side-effect (`then`). Vengono confrontati concorrenza vs ordine (flatMap vs concatMap), esecuzione simultanea vs sequenziale (merge vs concat) e propagazione degli errori nelle concatenazioni. Il filo conduttore è: recuperare/aggregare dati da più fonti asincrone (utenti, ordini, saldi, servizi HTTP, compagnie aeree) mantenendo controllo su ordine, concorrenza ed errori.

**Costrutti Reactor usati**: `Mono`, `Flux`, `Tuple2`/`Tuple3`.

**Operatori Reactor studiati**:
- `flatMap` (Flux e Mono) — trasforma ogni elemento in un nuovo Publisher e ne appiattisce i risultati eseguendoli in concorrenza, senza garanzia d'ordine; overload con limite di concorrenza (`flatMap(mapper, concurrency)`).
- `flatMapMany` (Mono) — trasforma il singolo valore di un Mono in un Flux di più elementi.
- `concatMap` — come `flatMap` ma sottoscrive le sorgenti generate in sequenza, preservando l'ordine.
- `concatWith` / `Flux.concat` / `concatWithValues` — concatena sequenze sottoscrivendo la successiva solo al completamento della precedente.
- `Flux.concatDelayError` — come concat ma posticipa la propagazione dell'errore a dopo l'esecuzione di tutte le sorgenti.
- `merge` / `mergeWith` — sottoscrive più sorgenti contemporaneamente ed emette nell'ordine di arrivo effettivo.
- `startWith` (valori, Iterable, Publisher) — antepone elementi/una sequenza al Flux originale.
- `then` — ignora gli elementi emessi e, al completamento, passa l'esecuzione a un secondo Mono.
- `Mono.zip` / `Flux.zip` — combina l'ennesimo elemento di più sorgenti in una tupla, al ritmo della più lenta.
- `collectList` — raccoglie tutti gli elementi di un Flux in una List pubblicata come Mono.

**Classi**:
- `CollectList.java` — `collectList` raccogliendo gli ordini di 3 utenti in un'unica lista.
- `ConcatError.java` — confronta `concatWith` (si interrompe alla prima sorgente in errore) con `Flux.concatDelayError`.
- `ConcatMap.java` — recupera in sequenza i dettagli di 10 prodotti con `concatMap`, mantenendo l'ordine.
- `ConcatWith.java` — varianti `concatWithValues`, `concatWithPublisher`, concatenazioni multiple, `Flux.concat`.
- `FluxFlatMap.java` — recupera in concorrenza il flusso ordini di ogni utente con `flatMap`.
- `FluxFlatMapAssignment.java` — dettagli di 10 prodotti con `flatMap` limitando la concorrenza a 3 chiamate simultanee.
- `Merge.java` — confronta `Flux.merge`/`mergeWith` su tre sorgenti, prelevando solo i primi 2 valori con `take`.
- `MergeUseCase.java` — caso d'uso reale: `helper/Kayak.java` aggrega in tempo reale le offerte di più compagnie aeree con `merge`.
- `MonoFlatMap.java` / `MonoFlatMapMany.java` — trasformano l'id utente rispettivamente in un messaggio (`flatMap`) e in un Flux di ordini (`flatMapMany`).
- `StartWith.java` / `StartWithUseCase.java` — varianti di `startWith`; caso d'uso in cui `helper/NameGenerator` ripropone ai nuovi subscriber i nomi già generati.
- `Then.java` — salva una lista di record e solo al termine invia una notifica con `then`.
- `Zip.java` — combina tre sorgenti (body, engine, tires) con `Flux.zip` per assemblare oggetti `Car`.
- `applications/*` — mini-applicazione in-memory (`User`, `Order`, `UserService`, `OrderService`, `PaymentService`) usata trasversalmente dagli esempi di `flatMap`/`zip`/`collectList`.
- `helper/*` (`Flight`, `AmericanAirlines`, `Emirates`, `QatarAirways`, `Kayak`, `NameGenerator`) — simulano servizi di ricerca voli con latenza casuale, aggregati da `Kayak` con `Flux.merge` entro una finestra di 2 secondi.
- `assignment/ExternalServiceClient.java` / `Product.java` / `Assignment.java` — client verso il servizio esterno che compone nome/recensione/prezzo di un prodotto con `Mono.zip`.

**Note particolari**: `assignment/ExternalServiceClient` richiede il servizio esterno (endpoint `/demo05/product/{id}`, `/demo05/review/{id}`, `/demo05/price/{id}`) per l'esecuzione reale; i test usano stub locali.

## sec10 — Suddivisione di flussi in gruppi: buffer, window, groupBy

**Argomento della lezione**: tratta gli operatori che suddividono un flusso continuo in sotto-insiemi elaborabili: `buffer` (raggruppa in liste), `window` (raggruppa in sotto-Flux reattivi) e `groupBy` (partiziona in base a una chiave). Vengono esplorate le varianti basate su conteggio, tempo o entrambi, e tre casi d'uso applicativi realistici: report periodici di ricavi, elaborazione differenziata per categoria di ordine, scrittura di finestre di eventi su file.

**Costrutti Reactor usati**: `Flux`, `GroupedFlux`.

**Operatori Reactor studiati**:
- `buffer()` — raccoglie tutti gli elementi in un'unica lista emessa al completamento.
- `buffer(int)` — raggruppa in liste di dimensione fissa.
- `buffer(Duration)` — raggruppa in liste emesse a intervalli di tempo fissi.
- `bufferTimeout(int, Duration)` — raggruppa al raggiungimento della dimensione o del timeout, quale si verifica prima.
- `window(int)` — suddivide il flusso in sotto-Flux di dimensione fissa (a differenza di `buffer`, che produce `List`).
- `groupBy(keySelector)` — suddivide il Flux in più `GroupedFlux` associati a una chiave.
- `flatMap` — elabora in parallelo i sotto-flussi prodotti da `groupBy`/`window`.

**Classi**:
- `Buffer.java` — varianti `buffer()`, `buffer(int)`, `buffer(Duration)`, `bufferTimeout(int, Duration)`.
- `GroupBy.java` — raggruppa numeri per parità (pari/dispari) in due `GroupedFlux`, elaborati con `flatMap`.
- `Window.java` — suddivide un flusso infinito di eventi in finestre di 5 elementi.
- `assignment/buffer/*` (`BookOrder`, `BufferAssignment`, `RevenueReport`) — filtra ordini per genere e genera un report ricavi ogni 5 secondi con `buffer(Duration)`.
- `assignment/groupby/*` (`Assignment`, `OrderProcessingService`, `PurchaseOrder`) — raggruppa ordini per categoria applicando una trasformazione specifica per gruppo (es. sovrapprezzo "Automotive", omaggio "Kids").
- `assignment/window/*` (`FileWriter`, `WindowAssignment`) — suddivide un flusso di eventi in finestre di 5 elementi e scrive ciascuna finestra su un file numerato progressivamente.

**Note particolari**: package autosufficiente, nessuna dipendenza dal servizio esterno. `WindowAssignment` produce file reali in `src/main/resources/sec10/`.

## sec11 — Resilienza: retry e repeat verso servizi esterni

**Argomento della lezione**: si concentra sulla gestione degli errori e sulla resilienza nelle chiamate a servizi esterni: `repeat`/`repeatWhen` per ri-sottoscrivere una sorgente terminata con successo, `retry`/`retryWhen` per ri-sottoscrivere una sorgente terminata con errore, con varianti a conteggio fisso, a ritardo fisso e con filtro sul tipo di eccezione. Il caso applicativo distingue errori client (HTTP 400, non da ritentare) da errori server (da ritentare automaticamente).

**Costrutti Reactor usati**: `Mono`, `Flux`, `reactor.util.retry.Retry`, Reactor Netty `HttpClient`.

**Operatori Reactor studiati**:
- `repeat(int)` — ripete la sottoscrizione un numero fisso di volte dopo la prima emissione, trasformando un Mono in Flux.
- `repeat()` — ripete indefinitamente, tipicamente combinato con `takeUntil` per fermarsi a una condizione.
- `repeat(BooleanSupplier)` — decide a ogni iterazione, tramite un supplier esterno, se ripetere.
- `repeatWhen(Function<Flux<Long>, Publisher>)` — guida la ripetizione tramite un Flux di trigger.
- `retry(long)` — ri-sottoscrive fino a un numero massimo di tentativi in caso di errore.
- `retryWhen(Retry)` — strategia configurabile con `Retry.fixedDelay(...)`, `Retry.max(...)`, `.filter(...)` (per ritentare solo certi tipi di eccezione), `.doBeforeRetry(...)`.

**Classi**:
- `ClientError.java` / `ServerError.java` — eccezioni runtime che rappresentano rispettivamente un errore HTTP 400 (non da ritentare) e un errore server generico (da ritentare).
- `ExternalServiceClient.java` — mappa lo status code della risposta su `ClientError`/`ServerError` o sul corpo in caso di successo.
- `ExternalServiceDemo.java` — `repeat()`+`takeUntil` finché `getCountry()` non risponde "Canada"; `retryWhen` con strategia `fixedDelay`, filtro su `ServerError`, max 20 tentativi.
- `Repeat.java` — tutte le varianti di `repeat`/`repeatWhen` su un Mono che genera un nome di paese casuale.
- `Retry.java` — `retry(long)` e `retryWhen` (con/senza filtro) su un Mono che fallisce nelle prime chiamate per poi avere successo.

**Note particolari**: `ExternalServiceClient` richiede il servizio esterno (endpoint `/demo06/product/{id}`, `/demo06/country`) per l'esecuzione reale di `ExternalServiceDemo`. I test usano `Mono.fromSupplier` con contatori per simulare fallimenti transitori e `StepVerifier.withVirtualTime` per verificare le attese dei retry senza rallentare l'esecuzione.

## sec12 — Sinks: produzione imperativa di segnali reattivi

**Argomento della lezione**: introduce l'API `Sinks`, che permette di produrre segnali reattivi (valori, completamento, errore) in modo imperativo, disaccoppiato dal ciclo di vita di sottoscrizione classico. Vengono confrontate le semantiche di distribuzione ai subscriber (unicast, multicast diretto vs con buffer, replay), il comportamento in presenza di subscriber lenti (best-effort vs all-or-nothing) e la thread-safety delle emissioni concorrenti (`tryEmitNext` vs `emitNext`). Il caso applicativo finale è una chat room in stile Slack costruita su un sink multicast a replay.

**Costrutti Reactor usati**: `Sinks.Many`, `Sinks.One`, `Sinks.EmitResult`, `Sinks.EmitFailureHandler`.

**Operatori Reactor studiati**:
- `Sinks.many().unicast().onBackpressureBuffer()` — sink con un solo subscriber consentito, bufferizza gli elementi emessi prima della sottoscrizione.
- `Sinks.many().multicast().onBackpressureBuffer()` — sink multicast che instrada a tutti i subscriber attivi, con buffer per i subscriber lenti.
- `Sinks.many().multicast().directBestEffort()` — multicast diretto senza buffer: se un subscriber non tiene il passo, l'emissione verso quel subscriber viene scartata senza far fallire il sink.
- `Sinks.many().multicast().directAllOrNothing()` — multicast diretto "tutto o niente": un subscriber lento fa fallire l'emissione per tutti.
- `Sinks.many().replay().all()` — sink multicast che memorizza tutti gli elementi emessi e li ritrasmette a ogni nuovo subscriber.
- `Sinks.one()` — sink a singolo segnale (`tryEmitValue`, `tryEmitEmpty`, `tryEmitError`, `emitValue`).
- `tryEmitNext`/`tryEmitValue`/`tryEmitError`/`tryEmitEmpty` — varianti "try" senza garanzia di serializzazione tra thread concorrenti.
- `emitNext`/`emitValue` con `EmitFailureHandler` — varianti thread-safe che gestiscono i conflitti di emissione concorrente.
- `asFlux()`/`asMono()` — espongono il sink come Publisher standard.

**Classi**:
- `SinkMulticast.java` — un multicast recapita solo ai subscriber già presenti al momento dell'emissione.
- `SinkMulticastDirectAllOrNothing.java` — un subscriber lento causa il fallimento dell'emissione per tutti con `directAllOrNothing()`.
- `SinkMulticastDirectBestEffort.java` — confronta il fallimento per buffer esaurito con la consegna best-effort che scarta solo verso il subscriber lento.
- `SinkMulticastReplay.java` — un subscriber tardivo riceve comunque tutti gli elementi già emessi grazie al replay.
- `SinkOne.java` — varianti di emissione di `Sinks.One`: valore, vuoto, errore, più subscriber, `emitValue` con handler.
- `SinkThreadSafety.java` — confronta `tryEmitNext` (perde elementi sotto concorrenza) con `emitNext` + handler di retry (garantisce tutte le 1000 emissioni concorrenti).
- `SinkUnicast.java` — buffering pre-sottoscrizione e rifiuto di un secondo subscriber su un sink unicast.
- `assignment/SlackRoom.java` — smista i messaggi tra i membri iscritti usando `Sinks.many().replay().all()`, garantendo che i nuovi membri ricevano lo storico.
- `assignment/SlackMember.java` / `SlackMessage.java` / `Assignment.java` — membro della chat, record del messaggio, e simulazione di una chat room con membri che si uniscono in momenti diversi.

**Note particolari**: package autosufficiente, nessuna dipendenza dal servizio esterno. Alcune demo impostano `System.setProperty("reactor.bufferSize.small", "16")` per ridurre il buffer interno e rendere osservabile il comportamento sotto pressione con un subscriber lento artificialmente rallentato.

## sec13 — Context reattivo e rate limiting

**Argomento della lezione**: introduce il `Context` di Project Reactor, un meccanismo di propagazione di dati "a ritroso" lungo la catena reattiva (dal `subscribe` verso l'alto), utile per informazioni contestuali come l'utente autenticato. Vengono esplorati lettura (`deferContextual`) e scrittura (`contextWrite`) del Context, le regole di composizione tra scritture multiple (append, non-cancellazione con Context vuoto, sovrascrittura/rimozione di chiavi) e la propagazione attraverso operatori che coinvolgono più scheduler (`merge`, `subscribeOn`). Il caso applicativo finale è un rate limiter per categoria di utente, implementato sfruttando il Context per portare l'informazione fino al punto di verifica del limite.

**Costrutti Reactor usati**: `Mono`, `Flux`, `reactor.util.context.Context`, `Schedulers` (`boundedElastic`, `parallel`), Reactor Netty `HttpClient`.

**Operatori Reactor studiati**:
- `Mono.deferContextual(Function<ContextView, Mono>)` — costruisce il Mono leggendo il Context reattivo disponibile in quel punto della catena.
- `contextWrite(Context)` — scrive/arricchisce il Context visibile agli operatori a monte.
- `contextWrite(Function<Context, Context>)` — scrittura funzionale del Context (es. `ctx.delete(...)`, arricchimento condizionale).
- composizione di più `contextWrite` in catena — append di chiavi da scritture diverse, non-cancellazione con `Context.empty()`, sovrascrittura di una chiave da parte di una scrittura più a monte.
- `concatWith` / `Flux.merge` combinati con `subscribeOn(Schedulers...)` — verificano che il Context scritto a valle raggiunga tutti i producer indipendentemente dallo scheduler di esecuzione.
- `startWith(Mono)` — usato nel rate limiter per anteporre il controllo del limite alla chiamata HTTP vera e propria.

**Classi**:
- `Context.java` — lettura/scrittura base del Context: successo con chiave `"user"` presente, fallimento se manca.
- `ContextAppendUpdate.java` — composizione di più `contextWrite`: append, non-cancellazione, sovrascrittura/rimozione di una chiave.
- `Propagation.java` — il Context scritto a valle è visibile anche a producer eseguiti su scheduler diversi, combinati con `concatWith`/`merge`.
- `RateLimiter.java` — limitatore di frequenza basato sul Context: legge la chiave `"category"` per decrementare in modo thread-safe i tentativi disponibili, ricaricati ogni 5 secondi (`standard`: 2, `prime`: 3 tentativi).
- `RateLimiterDemo.java` — simula 20 chiamate consecutive (1 al secondo) verso il servizio esterno, propagando l'utente tramite `contextWrite`, per osservare esaurimento e ripristino del rate limit.
- `client/ExternalServiceClient.java` — applica `RateLimiter.limitCalls()` (via `startWith`) prima della chiamata HTTP reale.
- `client/UserService.java` — associa utenti noti (`sam` → standard, `mike` → prime) alla propria categoria, arricchendo il Context a partire dalla chiave `"user"`.

**Note particolari**: `RateLimiterDemo` richiede il servizio esterno (endpoint `/demo07/book`) per l'esecuzione reale. I test (`RateLimiterTest`, `ContextTest`, `ContextAppendUpdateTest`, `PropagationTest`) verificano `RateLimiter` e la logica del Context in isolamento, senza chiamate HTTP reali.

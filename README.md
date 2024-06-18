# MyNews
## Introduzione
My news è una applicazione sviluppata in java per dispotivi android che ha l'obiettivo di permettere all'utente di visualizzare diverse pagine di giornali online proprio smartphone android. All'interno dell'applicazione
sono disponibili le maggiori testate giornalistiche italiane (Repubblica, Corriere della Sera, Gazzetta dello sport, ...), ma qualora non dovessero bastare l'utente puo' ricevere suggerimenti rispetto ad altri giornali. L'applicazione fornisce la possibilita' di salvare gli articoli preferiti in un comodo database locale e controlla regolarmente la presenza di nuovi contenuti all'interno dei siti web che sono disponibili dall'applicazione. Visualizzando i giornali attraverso l'applicazione l'utente evita qualsiasi tipo di pubblicita'. Questo perche' nativamente l'applicazione non esegue alcun codice javascript presente nei siti e il risultato finale e' quella di una navigazione senza alcun tipo di cooky di sessione, pubblicita', spot da visualizzare per 10 secondi per poter leggere il nostro articolo preferito. 

All'avvio dell'applicazione ci si trova all'interno all'interno della pagina di uno dei giornali online disponibili. Da questo sito possiamo spostarsi all'interno degli altri giornali attraverso un menù presente in alto a sinitra. In basso a destra è sempre presente un'icona che permette di aggiungere l'articolo di giornale che si sta leggendo alla lista di articoli preferiti. Qualora l'utente volesse decidere di eliminare l'attuale lista di preferita bastera' accedere alle impostazioni presenti in alto a destra.

![screenshot_1]([https://github.com/MaxBubblegum47/MyNews/blob/main/res/clean_favorites_articles.jpg](https://github.com/MaxBubblegum47/MyNews/blob/main/res/navigation_bar.jpg))

## Approfondimento tecnico
### WebView
L'applicazione fa intenso uso di webview per andare a visualizzare le pagine dei giornali. La webview permette di visualizzare siti e di poterli navigare, come se venisse usato un browser web. La differenza piu' grande e' che non puo' eseguire tutto il codice che compone la pagina web, in particolare codice Java Script, a meno che non venga esplicitamente espresso dal programmatore:
```
    WebSettings webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setDisplayZoomControls(false);
    webSettings.setSupportZoom(true);
    webSettings.setDefaultTextEncodingName("utf-8");
```
Questo porta la webview a non essere suscettibile a tutte le pubblicita' presenti sul sito ed evitando qualsiasi sistema di subscription presente su di esso. Il risultato finale e' che e' come se navigassimo sul nostro browser con un adblocker abilitato estremamente restrittivo. A livello di implementazione in Java, le webview che permettono la visualizzazione dei giornali sono tutte formate come segue:

```
public class GazzettaFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = binding.webViewNews;
        webView.loadUrl("https://gazzetta.it");
        webView.setWebViewClient(new WebViewController());

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```
Di seguito il relativo codice XML:
```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.GazzettaSky.GazzettaFragment">

    <WebView
        android:id="@+id/webViewNews"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</FrameLayout>
```

Le webview non solo solamente state usate solamente per visualizzare i giornali, ma anche per la UI degli articoli suggeriti e per gli articoli preferiti. Questo perche' mi permettono di visualizzare a schermo link cliccabili dall'utente senza bisogno di creare una textview con bottoni e action listener. Tutte le webview sono inserite all'interno di fragment che vengono navigati attarverso AndroidX.

![screenshot_2](https://github.com/MaxBubblegum47/MyNews/blob/main/res/internazionale_example.jpg)

### Database
Il database è locale ed e' stata usata la libreria SQLite. Al suo interno vengono inseriti tutti gli articoli che l'utente aggiunge premendo il floating button sempre presente a schermo in basso a destra. La classe java relativa al database lo istanzia e fornisce diversi metodi per interagire con esso: addArticle, getAllArticles, deleteAllArticles, getAllArticleTitle.
```
    public void addArticle(String title, String url) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_URL, url);
        db.insert(TABLE_ARTICLES, null, values);
        db.close();
    }

    public Cursor getAllArticles() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_ARTICLES, null, null, null, null, null, null);
    }

    public int deleteAllArticles() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_ARTICLES, null, null);
    }

    public ArrayList<String> getAllArticleTitles() {
        ArrayList<String> articleTitles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, new String[]{COLUMN_TITLE}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                articleTitles.add(title);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return articleTitles;
    }
```
Questi metodi sono legati a funzionalita' come l'aggiunta di un articolo alla lista dei preferiti, l'eliminazione di tale lista, o la visualizzazione degli articoli salvati all'interno della pagina preposta. Gli oggetti del database presentano la seguente struttura:
- COLUMN_ID = INTEGER PRIMARY KEY
- COLUMN_TITLE = TEXT
- COLUMN_URL = TEXT

![screenshot_3](https://github.com/MaxBubblegum47/MyNews/blob/main/res/favorites_articles.jpg)
![screenshot_4](https://github.com/MaxBubblegum47/MyNews/blob/main/res/clean_favorites_articles.jpg)

### Threads
All'interno dell'applicazione sono stati inseriti dei threads che controllano la presenza di nuovo sui giornali ogni 5 secondi dal primo avvio dell'applicazione. Ogni giornale ha il suo thread dedicato che viene instanziato all'interno del main:
```
String websiteUrlRepubblica = "https://www.repubblica.it"; // Replace with your website URL for main page
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlRepubblica);
        contentCheckerThread.start();

        String websiteUrlGazzetta = "https://gazzetta.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlGazzetta);
        contentCheckerThread.start();

        String websiteUrlCorriere = "https://corriere.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlCorriere);
        contentCheckerThread.start();

        String websiteUrlSkyTg24 = "https://tg24.sky.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlSkyTg24);
        contentCheckerThread.start();

        String websiteUrlInternazionale = "https://internazionale.it";
        contentCheckerThread = new WebsiteContentCheckerThread(this, websiteUrlInternazionale);
        contentCheckerThread.start();
```

Il thread esegue un dump periodico della pagina web del sito e controlla che il contenuto della pagina non sia diverso da quello scaricato 5 secondi prima (se disponibile). In caso vi siano delle differenze, e quindi del nuovo contenuto, allora l'applicazione provvede a fornire una notifica all'utente:
```
InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    String newContent = content.toString();
                    if (lastContent == null || !lastContent.equals(newContent)) {
                        // Content has changed, send notification
                        sendNotification();
                    }
                    lastContent = newContent;
```

Di seguito il codice relativo alla gestione delle notifica
```
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "WebsiteContentNotification")
                .setSmallIcon(R.drawable.icons8_google_news)
                .setContentTitle("Website Content Checker")
                .setContentText("New content detected on news websites!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the intent to be triggered when the notification is clicked. The user will be brought the the main page
                .setAutoCancel(true); // Automatically dismiss the notification when clicked

        /* Show notification */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, builder.build());
```
![screenshot_5](https://github.com/MaxBubblegum47/MyNews/blob/main/res/notifications.jpg)

### Articoli Suggeriti
Il funzionamento degli articoli suggeriti si basa su una serie di bottoni che se premuti dall'utente indicano una sua preferenza rispetto ad un determinato tema. Combinando i valori di questi bottoni vengono poi visualizzati a schermo le testate giornalistiche che l'applicazione suggerisce all'utente. Questi giornali sono salvati all'interno dell'applicazione e presentano dei tag connessi ai bottoni sopracitati. In base alla combinazione di preferenze indicate dall'utente verranno visualizzati specifici giornali. 
```
public static List<Article> getAllArticles() {
            List<Article> allArticles = new ArrayList<>();

            allArticles.add(new Article("https://repubblica.it", Arrays.asList("politics")));
            allArticles.add(new Article("https://www.nytimes.com/", Arrays.asList("politics")));
            allArticles.add(new Article("https://charliehebdo.fr/", Arrays.asList("politics")));
            allArticles.add(new Article("https://www.aljazeera.com/", Arrays.asList("politics")));

            allArticles.add(new Article("https://www.hwupgrade.it/", Arrays.asList("tech")));
            allArticles.add(new Article("https://www.notebookcheck.net/", Arrays.asList("tech")));
            allArticles.add(new Article("https://www.tomshardware.com/", Arrays.asList("tech")));
            allArticles.add(new Article("https://gamersnexus.net/", Arrays.asList("tech")));

            allArticles.add(new Article("https://www.automoto.it/", Arrays.asList("automoto")));
            allArticles.add(new Article("https://www.auto.it/", Arrays.asList("automoto")));
            allArticles.add(new Article("https://www.alvolante.it/", Arrays.asList("automoto")));
            allArticles.add(new Article("https://www.inmoto.it/", Arrays.asList("automoto")));
            allArticles.add(new Article("https://www.insella.it/", Arrays.asList("automoto")));

            allArticles.add(new Article("https://rollingstones.com/", Arrays.asList("music")));
            allArticles.add(new Article("https://www.giornaledellamusica.it/", Arrays.asList("music")));
            allArticles.add(new Article("https://www.essemagazine.it/", Arrays.asList("music")));
            allArticles.add(new Article("https://pitchfork.com/news/", Arrays.asList("music")));

            return allArticles;
        }


        /*

         * This method check the preferences that the user has set and displays
         * the best matching news paper that could be interesting for the user.
         */
        public static List<Article> suggestArticles(boolean politicsPref, boolean techPref, boolean autoPref, boolean musicPref) {
            List<Article> suggestedArticles = new ArrayList<>();
            List<Article> allArticles = getAllArticles();

            for (Article article : allArticles) {
                if ((politicsPref && article.getTags().contains("politics"))) {
                    suggestedArticles.add(article);
                }

                if ((techPref && article.getTags().contains("tech"))) {
                    suggestedArticles.add(article);
                }

                if ((musicPref && article.getTags().contains("music"))) {
                    suggestedArticles.add(article);
                }

                if ((autoPref && article.getTags().contains("automoto"))) {
                    suggestedArticles.add(article);
                }
            }

            return suggestedArticles;
        }
```

![screenshot_6](https://github.com/MaxBubblegum47/MyNews/blob/main/res/preferences_2.jpg)

## Futuri Sviluppi
L'applicazione allo stato attuale e' funzionante, ma necessita di alcune rifiniture per quel che riguarda l'aspetto grafico: la pagina degli articoli salvati andrebbe implementata con delle textview che migliorino la lettura della pagina e diano un aspetto piu' gradevole all'utente. Ritengo sia necessario aggiungere una funzionalita' per la quale l'utente possa aggiungere altri giornali online secondo i suoi gusti: immagino una textbox all'interno della quale l'utente inserisca l'indirizzo di un giornale online e questo venga aggiunto all'applicazione. Le notifiche potrebbero essere piu' precise e verbose, indicando il sito su cui e' presente il nuovo contenuto e che tipo di contenuto si tratta. Anche per quel che riguarda la funzionalita' dei suggerimenti si potrebbe espanderla ed aggiungere molti piu' giornali da suggerire all'utente.

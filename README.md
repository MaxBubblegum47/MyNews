# MyNews
## Introduzione
My news è una applicazione sviluppata in java per android che ha l'obiettivo di permettere all'utente di visualizzare diverse pagine di giornali online comodamente dal suo smartphone android.
L'utente riceve notifiche ogni qual volta vi sono dei nuovi contenuti all'interno dei siti web che sono supportati di defualt dall'applicazione:
- corriere della sera
- repubblica
- gazzetta dello sport
- internazionale
- sky tg24
  
ed è possibile salvare localmente gli articoli preferiti. All'interno dell'applicazione è inoltre presente una pagina che suggerisce all'utente altri giornali da poter leggere, basandosi su alcune preferenze che l'utente può definire all'interno della stessa pagina.

All'avvio dell'applicazione ci si trova all'interno della pagina principale, ovvero il sito di repubblica. Da questo sito possiamo spostarci all'interno degli altri giornali online attraverso un menù a tendina presente in alto a sinitra sinistra. Aprendo tale menù è possibile visualizzare tutti gli altri giornali che sono visualizzabili all'interno dell'applicazione. In basso a destra è sempre presente un'icona che permette di aggiungere l'articolo di giornale che si sta attualmente leggendo alla lista di articoli preferiti. Questa lista viene salvata in un database locale all'interno dell'applicazione stessa ed è sempre disponibile (anche offline).


## Approfondimento tecnico
Tutta l'applicazione è stata scritta in java e utilizza AndroidX come libreria principale, in particolare per quel che concerne la parte di navigazione tra le varie schermate. Il programma main conteine una classe che estente AppCompatActivity. All'interno di questa classe possiamo trovare la gestione di due componenti molto importanti per il corretto funzionamento dell'applicazione: la gestione dei preferiti e la gestione dei threads per la verifica di nuovi contenuti all'interno dei siti delle testate giornalistiche. 

### Gestione Database
Per quel che concerne il database è stata utilizzata la libreria sqlite. Il database è locale e al suo interno vengono inseriti tutti gli articoli che l'utente aggiunge premendo il bottone relativo a tale funzione. La classe java relativa al database lo istanzia e fornisce i metodi per l'ottenimento di tutte le informazioni in esso contenuto. Questa ultima funzionalità è utile al fine di poter stampare a schermo la lista di tutti gli articoli preferiti. Di seguito il codice relativo al database:
```
public class ArticleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ARTICLES = "articles";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_ARTICLES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TITLE + " TEXT," +
                    COLUMN_URL + " TEXT)";

    public ArticleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database migration if needed
    }

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
}
```

### Threads
Per quel che rigurda la gestione dei threads relativi al controllo di nuovi contenuti sui giornali online è stata utilizzata la classe java Thread. Questa viene estesa e corredata di metodi che permettono la notifica all'utente qualora vi sia qualche cambiamento all'interno dei contenuti del sito. Ogni pagina web ha il suo thread dedicato che viene instanziato all'interno del main:
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

Una volta lanciato il thread questi fa un dump periodico della pagina web del sito e controlla che il contenuto della pagina non sia diverso da quello precedentemente scaricato. In caso vi siano delle discrepanze, e quindi del nuovo contenuto, allora l'applicazione provvede a lanciare una notifica all'utente:
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
                .setSmallIcon(R.drawable.bbc_logo_2021)
                .setContentTitle("Website Content Checker")
                .setContentText("New content detected on the website!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the intent to be triggered when the notification is clicked
                .setAutoCancel(true); // Automatically dismiss the notification when clicked

        // Show notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, builder.build());
```
### WebView
Per quel che riguarda la visualizzazione delle pagine online è stata fatto larghissimo uso di webview. Queste permettono di poter visualizzare pagine online all'interno dell'applicazione e la particolirità è che non eseguono codice javascript di alcun tipo, quindi non permettono il caricamento di pubblicità o di altri script bloccanti che richiedono che ci si debba iscrivere al sito per poter visualizzare il contenuto. Tutto il codice relativo alle webview è riportato di seguito:
```
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView = binding.webViewBBC;
        webView.loadUrl("https://repubblica.it");
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
E questo è il relativo codice xml:
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.Repubblica.HomeFragment" >

    <WebView
        android:id="@+id/webViewBBC"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

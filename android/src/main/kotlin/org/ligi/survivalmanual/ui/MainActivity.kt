import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ligi.survivalmanual.presentation.navigation.AppNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainScreenViewModel by viewModels()

        setContent {
            // Assuming you have a theme defined for your app
            // SurvivalManualTheme {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
            // }
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Assuming you have a theme defined for your app
            // SurvivalManualTheme {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
            // }
        }
    }

    // Remove all the old methods and code related to the view-based UI
    // and navigation drawer as they are no longer needed.
    // This includes methods like:
    // - onCreateOptionsMenu
    // - onOptionsItemSelected
    // - createWebPrintJob
    // - processURL
    // - onPostCreate
    // - onResume
    // - createPrintDocumentAdapter
    // - openInBrowser
    // and any related properties or classes.
}

    // This is a placeholder comment. The actual removal of methods will be done
    // in the next step as the diff format requires showing the deletions explicitly.
}

/*
Removed methods and properties:

companion object {
private val TAG = MainActivity::class.java.name;
}

private val drawerToggle by lazy {
ActionBarDrawerToggle(this, mainBinding.mainDrawerLayout, R.string.drawer_open, R.string.drawer_close)
}

private val survivalContent by lazy { SurvivalContent(assets) }

private lateinit var mainBinding: ActivityMainBinding

private lateinit var currentUrl: String
private lateinit var currentTopicName: String
private lateinit var textInput: MutableList<String>

private var lastFontSize = State.getFontSize()
private var lastNightMode = State.nightModeString()
private var lastAllowSelect = State.allowSelect()

private val linearLayoutManager by lazy { LinearLayoutManager(this) }

private var isInEditMode by observable(false, onChange = { _, _, newMode ->
if (newMode) {
mainBinding.mainFab.setImageResource(R.drawable.ic_remove_red_eye)
mainBinding.mainContentRecycler.adapter = EditingRecyclerAdapter(textInput)
} else {
mainBinding.mainFab.setImageResource(R.drawable.ic_edit)
mainBinding.mainContentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
}

mainBinding.mainContentRecycler.scrollToPosition(State.lastScrollPos)
})

private fun imageWidth(): Int {
val totalWidthPadding = (resources.getDimension(R.dimen.content_padding) * 2).toInt()
return min(mainBinding.mainContentRecycler.width - totalWidthPadding, mainBinding.mainContentRecycler.height)
}

val onURLClick: (String) -> Unit = {
if (it.startsWith("http")) {
openInBrowser(Uri.parse(it))
} else if (isImage(it)) {
startActivity(Intent(this, ImageViewActivity::class.java).apply {
putExtra("URL", it)
})
} else {
processURL(it)
}
}

override fun onCreateOptionsMenu(menu: Menu): Boolean {
menuInflater.inflate(R.menu.main, menu)
if (Build.VERSION.SDK_INT >= 19) {
menuInflater.inflate(R.menu.print, menu)
}

val searchView = menu.findItem(R.id.action_search).actionView as SearchView

searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
override fun onQueryTextChange(searchTerm: String): Boolean {
State.searchTerm = searchTerm
val adapter = mainBinding.mainContentRecycler.adapter
if (adapter is MarkdownRecyclerAdapter) {

val positionForWord = adapter.getPositionForWord(searchTerm)

if (positionForWord != null) {
mainBinding.mainContentRecycler.smoothScrollToPosition(positionForWord)
} else {
mainBinding.mainContentRecycler.adapter = SearchResultRecyclerAdapter(searchTerm, survivalContent) {
processURL(it)
currentFocus?.let {
(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, 0)
}
}.apply { showToastWhenListIsEmpty() }

            }

adapter.notifyDataSetChanged()
} else if (adapter is SearchResultRecyclerAdapter) {
adapter.changeTerm(searchTerm)
adapter.showToastWhenListIsEmpty()
if (survivalContent.getMarkdown(currentUrl)!!.contains(searchTerm)) {
mainBinding.mainContentRecycler.adapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
State.searchTerm = searchTerm
}
}

return true
}

override fun onQueryTextSubmit(query: String?) = true
})
return super.onCreateOptionsMenu(menu)
}

fun SearchResultRecyclerAdapter.showToastWhenListIsEmpty() {
if (list.isEmpty()) {
Toast.makeText(this@MainActivity, State.searchTerm + " not found", Toast.LENGTH_LONG).show()
}
}

private fun MarkdownRecyclerAdapter.getPositionForWord(searchTerm: String): Int? {
val first = max(linearLayoutManager.findFirstVisibleItemPosition(), 0)
val search = CaseInsensitiveSearch(searchTerm)

return (first..list.lastIndex).firstOrNull {
search.isInContent(list[it])
}
}

private val optionsMap = mapOf(
R.id.menu_settings to { startActivity(Intent(this, PreferenceActivity::class.java)) },

R.id.menu_print to {
AlertDialog.Builder(this)
.setSingleChoiceItems(arrayOf("This chapter", "Everything"), 0, null)
.setTitle("Print")
.setPositiveButton(android.R.string.ok) { dialog, _ ->
val text = when ((dialog as AlertDialog).listView.checkedItemPosition) {
0 -> convertMarkdownToHtml(survivalContent.getMarkdown(currentUrl)!!)
else -> navigationEntryMap.joinToString("<hr/>") {
convertMarkdownToHtml(survivalContent.getMarkdown(it.entry.url)!!)
}
}

val newWebView = if (Build.VERSION.SDK_INT >= 17) {
WebView(createConfigurationContext(Configuration()))
} else {
WebView(this@MainActivity)
}

newWebView.webViewClient = object : WebViewClient() {
override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?) = false
override fun onPageFinished(view: WebView, url: String) = createWebPrintJob(view)
}


newWebView.loadDataWithBaseURL("file:///android_asset/md/", text, "text/HTML", "UTF-8", null)

}
.setNegativeButton(android.R.string.cancel, null)
.show()
}
)

override fun onOptionsItemSelected(item: MenuItem) = if (optionsMap.containsKey(item.itemId)) {
(optionsMap[item.itemId] ?: error("selected item ${item.itemId} not in optionsMap")).invoke()
true
} else {
drawerToggle.onOptionsItemSelected(item)
}


@TargetApi(19)
private fun createWebPrintJob(webView: WebView) {
val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
val jobName = getString(R.string.app_name) + " Document"

val printAdapter = createPrintDocumentAdapter(webView, jobName)
try {
printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
} catch (iae: IllegalArgumentException) {
AlertDialog.Builder(this)
.setMessage("Problem printing: " + iae.message)
.setPositiveButton(android.R.string.ok, null)
.show()
}
}

private fun processURL(url: String): Boolean {

mainBinding.mainAppbar.setExpanded(true)
Log.i(TAG, "processing url $url")

val titleResByURL = getTitleResByURL(url) ?: return false

currentUrl = url

currentTopicName = getString(titleResByURL)

supportActionBar?.subtitle = currentTopicName

State.lastVisitedURL = url

survivalContent.getMarkdown(currentUrl)?.let { markdown ->
textInput = splitText(markdown)

val newAdapter = MarkdownRecyclerAdapter(textInput, imageWidth(), onURLClick)
mainBinding.mainContentRecycler.adapter = newAdapter
if (!State.searchTerm.isNullOrBlank()) {
newAdapter.notifyDataSetChanged()
newAdapter.getPositionForWord(State.searchTerm!!)?.let {
mainBinding.mainContentRecycler.scrollToPosition(it)
}

}
mainBinding.mainNavigationView.refresh()

return true
}

return false
}

override fun onPostCreate(savedInstanceState: Bundle?) {
super.onPostCreate(savedInstanceState)
drawerToggle.syncState()
}


override fun onResume() {
super.onResume()
mainBinding.mainNavigationView.refresh()
mainBinding.mainFab.visibility = if (State.allowEdit()) View.VISIBLE else View.INVISIBLE
if (lastFontSize != State.getFontSize()) {
mainBinding.mainContentRecycler.adapter?.notifyDataSetChanged()
lastFontSize = State.getFontSize()
}
if (lastAllowSelect != State.allowSelect()) {
recreate()
lastAllowSelect = State.allowSelect()
}
if (lastNightMode != State.nightModeString()) {
recreate()
lastNightMode = State.nightModeString()
}

invalidateOptionsMenu()
}

@TargetApi(19)
fun createPrintDocumentAdapter(webView: WebView, documentName: String): PrintDocumentAdapter {
return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
webView.createPrintDocumentAdapter(documentName)
} else {
webView.createPrintDocumentAdapter()
}
}

fun Context.openInBrowser(uri: Uri) =
startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW, uri), "Open in browser"))
*/
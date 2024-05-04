package com.example.geminiwithclaude.Activities

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

/*class DeckPickerActivity {
    /** Called when the activity is first created.  */
    @Throws(SQLException::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (showedActivityFailedScreen(savedInstanceState)) {
            return
        }
        exportingDelegate = ActivityExportingDelegate(this) { getColUnsafe }
        customStudyDialogFactory = CustomStudyDialogFactory({ getColUnsafe }, this).attachToActivity(this)

        // Then set theme and content view
        super.onCreate(savedInstanceState)

        // handle the first load: display the app introduction
        if (!hasShownAppIntro()) {
            Timber.i("Displaying app intro")
            val appIntro = Intent(this, IntroductionActivity::class.java)
            appIntro.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(appIntro)
            finish() // calls onDestroy() immediately
            return
        } else {
            Timber.d("Not displaying app intro")
        }
        if (intent.hasExtra(INTENT_SYNC_FROM_LOGIN)) {
            Timber.d("launched from introduction activity login: syncing")
            syncOnResume = true
        }

        setContentView(R.layout.homescreen)
        handleStartup()
        val mainView = findViewById<View>(android.R.id.content)

        // check, if tablet layout
        studyoptionsFrame = findViewById(R.id.studyoptions_fragment)
        // set protected variable from NavigationDrawerActivity
        fragmented = studyoptionsFrame != null && studyoptionsFrame!!.visibility == View.VISIBLE

        // Open StudyOptionsFragment if in fragmented mode
        if (fragmented && !startupError) {
            loadStudyOptionsFragment(false)
        }
        registerExternalStorageListener()

        // create inherited navigation drawer layout here so that it can be used by parent class
        initNavigationDrawer(mainView)
        title = resources.getString(R.string.app_name)

        deckPickerContent = findViewById(R.id.deck_picker_content)
        recyclerView = findViewById(R.id.files)
        noDecksPlaceholder = findViewById(R.id.no_decks_placeholder)

        deckPickerContent.visibility = View.GONE
        noDecksPlaceholder.visibility = View.GONE

        // specify a LinearLayoutManager and set up item dividers for the RecyclerView
        recyclerViewLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = recyclerViewLayoutManager
        val ta = this.obtainStyledAttributes(intArrayOf(R.attr.deckDivider))
        val divider = ta.getDrawable(0)
        ta.recycle()
        val dividerDecorator = DividerItemDecoration(this, recyclerViewLayoutManager.orientation)
        dividerDecorator.setDrawable(divider!!)
        recyclerView.addItemDecoration(dividerDecorator)

        // Add background to Deckpicker activity
        val view = if (fragmented) findViewById(R.id.deckpicker_xl_view) else findViewById<View>(R.id.root_layout)

        var hasDeckPickerBackground = false
        try {
            hasDeckPickerBackground = applyDeckPickerBackground()
        } catch (e: OutOfMemoryError) { // 6608 - OOM should be catchable here.
            Timber.w(e, "Failed to apply background - OOM")
            showThemedToast(this, getString(R.string.background_image_too_large), false)
        } catch (e: Exception) {
            Timber.w(e, "Failed to apply background")
            showThemedToast(this, getString(R.string.failed_to_apply_background_image, e.localizedMessage), false)
        }
        exportingDelegate.onRestoreInstanceState(savedInstanceState)

        // create and set an adapter for the RecyclerView
        deckListAdapter = DeckAdapter(layoutInflater, this).apply {
            setDeckClickListener(deckClickListener)
            setCountsClickListener(countsClickListener)
            setDeckExpanderClickListener(deckExpanderClickListener)
            setDeckLongClickListener(deckLongClickListener)
            enablePartialTransparencyForBackground(hasDeckPickerBackground)
        }
        recyclerView.adapter = deckListAdapter

        pullToSyncWrapper = findViewById<SwipeRefreshLayout?>(R.id.pull_to_sync_wrapper).apply {
            setDistanceToTriggerSync(SWIPE_TO_SYNC_TRIGGER_DISTANCE)
            setOnRefreshListener {
                Timber.i("Pull to Sync: Syncing")
                pullToSyncWrapper.isRefreshing = false
                sync()
            }
            viewTreeObserver.addOnScrollChangedListener {
                pullToSyncWrapper.isEnabled = recyclerViewLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        }
        // Setup the FloatingActionButtons, should work everywhere with min API >= 15
        floatingActionMenu = DeckPickerFloatingActionMenu(this, view, this)

        reviewSummaryTextView = findViewById(R.id.today_stats_text_view)

        shortAnimDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        Onboarding.DeckPicker(this, recyclerViewLayoutManager).onCreate()

        launchShowingHidingEssentialFileMigrationProgressDialog()
        if (BuildConfig.DEBUG) {
            checkWebviewVersion()
        }

        supportFragmentManager.setFragmentResultListener(DeckPickerContextMenu.REQUEST_KEY_CONTEXT_MENU, this) { requestKey, arguments ->
            when (requestKey) {
                DeckPickerContextMenu.REQUEST_KEY_CONTEXT_MENU -> handleContextMenuSelection(
                    arguments.getSerializableCompat<DeckPickerContextMenuOption>(DeckPickerContextMenu.CONTEXT_MENU_DECK_OPTION)
                        ?: error("Unable to retrieve selected context menu option"),
                    arguments.getLong(DeckPickerContextMenu.CONTEXT_MENU_DECK_ID, -1)
                )
                else -> error("Unexpected fragment result key! Did you forget to update DeckPicker?")
            }
        }
    }
}*/
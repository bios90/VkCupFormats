package com.example.vkcupformats.screens.act_format_details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import com.example.vkcupformats.base.BaseActivity
import com.example.vkcupformats.base.setNavBarLightDark
import com.example.vkcupformats.base.setStatusLightDark
import com.example.vkcupformats.base.subscribeState
import com.example.vkcupformats.base.vm.createViewModelFactory
import com.example.vkcupformats.data.TypeTab
import com.example.vkcupformats.databinding.ActFormatDetailsBinding
import com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions.SubScreenPairsQuestion
import com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions.SubScreenPairsQuestionVm
import com.example.vkcupformats.screens.act_format_details.subscreens.questions.SubScreenQuestions
import com.example.vkcupformats.screens.act_format_details.subscreens.questions.SubScreenQuestionsVm
import com.example.vkcupformats.screens.act_format_details.subscreens.ratings.SubScreenRatingsCompose
import com.example.vkcupformats.screens.act_format_details.subscreens.ratings.SubScreenRatingsVm
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles.SubScreenTextWithBubbles
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles.SubScreenTextWithBubblesVm
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs.SubScreenTextWithInputs
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs.SubScreenTextWithInputsVm
import com.example.vkcupformats.ui.adapters.AdapterVpUniversal
import com.example.vkcupformats.ui.theme.AppTheme

class ActFormatDetails : BaseActivity() {

    private val vmActFormatDetails: ActFormatDetailsVm by viewModels {
        createViewModelFactory<ActFormatDetailsVm>()
    }

    private val adapterPager by lazy { AdapterVpUniversal() }

    private val bndActFormatDetails: ActFormatDetailsBinding by lazy {
        ActFormatDetailsBinding.inflate(
            layoutInflater,
            null,
            false
        )
    }

    private val pagerViews: List<ComposeView> by lazy { TypeTab.values().map { ComposeView(this) } }

    private val vmSubScreenRatings: SubScreenRatingsVm by viewModels {
        createViewModelFactory<SubScreenRatingsVm>()
    }
    private val vmSubScreenQuestions: SubScreenQuestionsVm by viewModels {
        createViewModelFactory<SubScreenQuestionsVm>()
    }
    private val vmSubScreenPairsQuestion: SubScreenPairsQuestionVm by viewModels {
        createViewModelFactory<SubScreenPairsQuestionVm>()
    }
    private val vmSubScreenTextWithBubbles: SubScreenTextWithBubblesVm by viewModels {
        createViewModelFactory<SubScreenTextWithBubblesVm>()
    }
    private val vmSubScreenTextWithInputs: SubScreenTextWithInputsVm by viewModels {
        createViewModelFactory<SubScreenTextWithInputsVm>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBars()
        setContentView(bndActFormatDetails.root)
        setupMainView()
        setupSubviews()
    }

    private fun setupBars() {
        with(window) {
            setStatusLightDark(isLight = true)
            setNavBarLightDark(isLight = true)
            statusBarColor = AppTheme.color.Surface.toArgb()
            navigationBarColor = AppTheme.color.Surface.toArgb()
        }
    }

    private fun setupMainView() {
        adapterPager.setViews(pagerViews)
        bndActFormatDetails.vp.adapter = adapterPager
        bndActFormatDetails.bottomBar.onTabSelected = vmActFormatDetails.UiListener()::onTabClicked
        subscribeState(
            act = this,
            vm = vmActFormatDetails,
            stateConsumer = { state ->
                bndActFormatDetails.bottomBar.selectedTab = state.selectedTab
                val pos = TypeTab.values().indexOf(state.selectedTab)
                bndActFormatDetails.vp.currentItem = pos
            }
        )
    }

    private fun setupSubviews() {
        setRatingsSubScreen()
        setQuestionsScreen()
        setPairsQuestionScreen()
        setTextsWithBubblesScreen()
        setTextsWithInputsScreen()
    }

    private fun setRatingsSubScreen() {
        subscribeState(
            act = this,
            vm = vmSubScreenRatings,
            stateConsumer = { state ->
                getViewForTab(TypeTab.RATINGS).setContent {
                    SubScreenRatingsCompose(
                        items = state.items,
                        onRatingChanged = vmSubScreenRatings.UiListener()::onItemRatingChanged,
                        onScrolledToBottom = vmSubScreenRatings.UiListener()::onScrolledToBottom
                    )
                }
            }
        )
    }

    private fun setQuestionsScreen() {
        subscribeState(
            act = this,
            vm = vmSubScreenQuestions,
            stateConsumer = { state ->
                getViewForTab(TypeTab.QUESTION).setContent {
                    SubScreenQuestions(
                        items = state.items,
                        onAnswerSelected = vmSubScreenQuestions.UiListener()::onQuestionAnswerClicked,
                        onScrolledToBottom = vmSubScreenQuestions.UiListener()::onScrolledToBottom
                    )
                }
            }
        )
    }

    private fun setPairsQuestionScreen() {
        subscribeState(
            act = this,
            vm = vmSubScreenPairsQuestion,
            stateConsumer = { state ->
                getViewForTab(TypeTab.PAIRS).setContent {
                    SubScreenPairsQuestion(
                        items = state.items,
                        onElementClick = vmSubScreenPairsQuestion.UiListener()::onPairsElementClicked,
                        onConnectionAnimationDrawn = vmSubScreenPairsQuestion.UiListener()::onPairsAnimationDrawn,
                        onScrolledToBottom = vmSubScreenPairsQuestion.UiListener()::onScrolledToBottom
                    )
                }
            }
        )
    }

    private fun setTextsWithBubblesScreen() {
        subscribeState(
            act = this,
            vm = vmSubScreenTextWithBubbles,
            stateConsumer = { state ->
                getViewForTab(TypeTab.BUBBLES).setContent {
                    SubScreenTextWithBubbles(
                        items = state.items,
                        onBubbleClicked = vmSubScreenTextWithBubbles.UiListener()::onBubbleClicked,
                        onMissingWordClicked = vmSubScreenTextWithBubbles.UiListener()::onMissingWordClicked,
                        onScrolledToBottom = vmSubScreenTextWithBubbles.UiListener()::onScrolledToBottom
                    )
                }
            }
        )
    }

    private fun setTextsWithInputsScreen() {
        subscribeState(
            act = this,
            vm = vmSubScreenTextWithInputs,
            stateConsumer = { state ->
                getViewForTab(TypeTab.INPUTS).setContent {
                    SubScreenTextWithInputs(
                        items = state.items,
                        onItemInputChanged = vmSubScreenTextWithInputs.UiListener()::onInputTextChanged,
                        onItemAnimationShown = vmSubScreenTextWithInputs.UiListener()::onInputAnimationShown,
                        onScrolledToBottom = vmSubScreenTextWithInputs.UiListener()::onScrolledToBottom
                    )
                }
            }
        )
    }

    private fun getViewForTab(tab: TypeTab): ComposeView =
        pagerViews.get(TypeTab.values().indexOf(tab))

}